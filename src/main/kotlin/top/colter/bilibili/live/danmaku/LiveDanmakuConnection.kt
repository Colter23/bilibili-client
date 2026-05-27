package top.colter.bilibili.live.danmaku

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.wss
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.HttpHeaders
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import top.colter.bilibili.api.getLiveDanmakuInfo
import top.colter.bilibili.api.getLiveInfo
import top.colter.bilibili.client.BILI_BROWSER_USER_AGENT
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.data.live.danmaku.LiveDanmakuInfo
import top.colter.bilibili.data.live.danmaku.LiveDanmakuMessage
import top.colter.bilibili.data.live.danmaku.LiveDanmakuOptions
import top.colter.bilibili.exception.BiliDanmakuAuthException
import top.colter.bilibili.exception.BiliDanmakuProtocolException
import top.colter.bilibili.tools.json
import java.io.IOException
import java.util.UUID

internal fun BiliClient.createLiveDanmakuFlow(
    roomId: Long,
    options: LiveDanmakuOptions
): Flow<LiveDanmakuMessage> = flow {
    val realRoomId = getLiveInfo(roomId).roomId
    val websocketClient = createLiveDanmakuHttpClient()

    try {
        while (currentCoroutineContext().isActive) {
            try {
                connectOnce(this@createLiveDanmakuFlow, websocketClient, realRoomId, options)
                if (!options.autoReconnect) break
            } catch (e: CancellationException) {
                throw e
            } catch (e: BiliDanmakuAuthException) {
                throw e
            } catch (e: BiliDanmakuProtocolException) {
                throw e
            } catch (e: Exception) {
                if (!options.autoReconnect) throw e
            }

            if (options.autoReconnect) {
                delay(options.reconnectIntervalMillis)
            }
        }
    } finally {
        websocketClient.close()
    }
}.flowOn(Dispatchers.IO)

private fun BiliClient.createLiveDanmakuHttpClient(): HttpClient {
    return HttpClient(OkHttp) {
        install(WebSockets)
        install(HttpCookies) {
            storage = this@createLiveDanmakuHttpClient.storage.asCookiesStorage()
        }
        defaultRequest {
            header(HttpHeaders.Origin, "https://live.bilibili.com")
            header(HttpHeaders.Referrer, "https://live.bilibili.com")
            header(HttpHeaders.UserAgent, BILI_BROWSER_USER_AGENT)
            header(HttpHeaders.AcceptLanguage, "zh-CN")
            header(HttpHeaders.Accept, "*/*")
            header(HttpHeaders.Pragma, "no-cache")
            header(HttpHeaders.CacheControl, "no-cache")
        }
        expectSuccess = false
    }
}

private suspend fun FlowCollector<LiveDanmakuMessage>.connectOnce(
    client: BiliClient,
    websocketClient: HttpClient,
    roomId: Long,
    options: LiveDanmakuOptions
) {
    val info = client.getLiveDanmakuInfo(roomId)
    val host = info.hosts.firstOrNull { it.wssPort > 0 }
        ?: info.hosts.firstOrNull()
        ?: throw BiliDanmakuProtocolException("没有可用的直播弹幕服务器")
    val auth = client.createAuthData(roomId, info, options)
    val authPacket = LiveDanmakuProtocol.buildAuthPacket(json.encodeToString(auth))
    val url = "wss://${host.host}:${host.wssPort}/sub"

    websocketClient.wss(url) {
        send(Frame.Binary(true, authPacket))
        handleFrame(incoming.receive(), roomId)

        val heartbeatJob = launch {
            while (isActive) {
                send(Frame.Binary(true, LiveDanmakuProtocol.buildHeartbeatPacket()))
                delay(options.heartbeatIntervalMillis)
            }
        }

        try {
            for (frame in incoming) {
                handleFrame(frame, roomId)
            }
        } finally {
            heartbeatJob.cancel()
            close()
        }
    }
}

private suspend fun FlowCollector<LiveDanmakuMessage>.handleFrame(frame: Frame, roomId: Long) {
    when (frame) {
        is Frame.Binary -> handlePackets(LiveDanmakuProtocol.decodePackets(frame.data), roomId)
        is Frame.Text -> runCatching {
            LiveDanmakuMessageParser.parseMessage(frame.readText(), roomId)
        }.getOrNull()?.let { emit(it) }
        is Frame.Close -> throw LiveDanmakuClosedException()
        else -> {}
    }
}

private suspend fun FlowCollector<LiveDanmakuMessage>.handlePackets(
    packets: List<LiveDanmakuPacket>,
    roomId: Long
) {
    packets.forEach { packet ->
        when (packet.operation) {
            LiveDanmakuProtocol.OP_AUTH_REPLY -> LiveDanmakuProtocol.requireAuthSuccess(packet)
            LiveDanmakuProtocol.OP_MESSAGE -> {
                val text = packet.body.toString(Charsets.UTF_8)
                runCatching {
                    LiveDanmakuMessageParser.parseMessage(text, roomId)
                }.getOrNull()?.let { emit(it) }
            }
            LiveDanmakuProtocol.OP_HEARTBEAT_REPLY -> {}
            else -> {}
        }
    }
}

private fun BiliClient.createAuthData(
    roomId: Long,
    info: LiveDanmakuInfo,
    options: LiveDanmakuOptions
): LiveDanmakuAuthData {
    val cookies = storage.exportCookies()
    val uid = options.uid ?: cookies
        .firstOrNull { it.name == "DedeUserID" }
        ?.value
        ?.toLongOrNull()
        ?: 0L
    val buvid = options.buvid ?: cookies.firstValue("LIVE_BUVID", "buvid3", "buvid4").orEmpty()

    return LiveDanmakuAuthData(
        uid = uid,
        roomId = roomId,
        protover = options.protover,
        buvid = buvid,
        key = info.token
    )
}

private fun List<io.ktor.http.Cookie>.firstValue(vararg names: String): String? {
    return names.firstNotNullOfOrNull { name -> firstOrNull { it.name == name }?.value }
}

private class LiveDanmakuClosedException : IOException("直播弹幕 WebSocket 已关闭")

@Serializable
private data class LiveDanmakuAuthData(
    val uid: Long,
    @SerialName("roomid")
    val roomId: Long,
    val protover: Int,
    val buvid: String,
    @SerialName("support_ack")
    val supportAck: Boolean = true,
    @SerialName("queue_uuid")
    val queueUuid: String = UUID.randomUUID().toString().substring(0, 8),
    val scene: String = "room",
    val platform: String = "web",
    val type: Int = 2,
    val key: String
)
