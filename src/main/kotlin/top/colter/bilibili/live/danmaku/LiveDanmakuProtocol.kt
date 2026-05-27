package top.colter.bilibili.live.danmaku

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull
import top.colter.bilibili.exception.BiliDanmakuAuthException
import top.colter.bilibili.exception.BiliDanmakuProtocolException
import top.colter.bilibili.exception.BiliDanmakuUnsupportedProtocolException
import top.colter.bilibili.tools.json
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.zip.Inflater

internal data class LiveDanmakuPacket(
    val protocol: Int,
    val operation: Int,
    val sequence: Int,
    val body: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LiveDanmakuPacket

        if (protocol != other.protocol) return false
        if (operation != other.operation) return false
        if (sequence != other.sequence) return false
        return body.contentEquals(other.body)
    }

    override fun hashCode(): Int {
        var result = protocol
        result = 31 * result + operation
        result = 31 * result + sequence
        result = 31 * result + body.contentHashCode()
        return result
    }
}

internal object LiveDanmakuProtocol {
    internal const val HEADER_SIZE: Int = 16

    internal const val PROTOCOL_JSON: Int = 0
    internal const val PROTOCOL_POPULARITY: Int = 1
    internal const val PROTOCOL_ZLIB: Int = 2
    internal const val PROTOCOL_BROTLI: Int = 3

    internal const val OP_HEARTBEAT: Int = 2
    internal const val OP_HEARTBEAT_REPLY: Int = 3
    internal const val OP_MESSAGE: Int = 5
    internal const val OP_AUTH: Int = 7
    internal const val OP_AUTH_REPLY: Int = 8

    private const val SEQUENCE_ID: Int = 1

    internal fun buildAuthPacket(body: String): ByteArray = buildPacket(body, OP_AUTH)

    internal fun buildHeartbeatPacket(): ByteArray = buildPacket("", OP_HEARTBEAT)

    internal fun buildPacket(
        body: String,
        operation: Int,
        protocol: Int = PROTOCOL_ZLIB
    ): ByteArray {
        val bodyBytes = body.toByteArray(Charsets.UTF_8)
        val buffer = ByteBuffer.wrap(ByteArray(bodyBytes.size + HEADER_SIZE))
        buffer.putInt(bodyBytes.size + HEADER_SIZE)
        buffer.putShort(HEADER_SIZE.toShort())
        buffer.putShort(protocol.toShort())
        buffer.putInt(operation)
        buffer.putInt(SEQUENCE_ID)
        buffer.put(bodyBytes)
        return buffer.array()
    }

    internal fun decodePackets(data: ByteArray): List<LiveDanmakuPacket> {
        val result = mutableListOf<LiveDanmakuPacket>()
        val buffer = ByteBuffer.wrap(data)

        while (buffer.remaining() > 0) {
            val start = buffer.position()
            if (buffer.remaining() < HEADER_SIZE) {
                throw BiliDanmakuProtocolException("直播弹幕数据包头不完整")
            }

            val packetLength = buffer.int
            val headerLength = buffer.short.toInt()
            val protocol = buffer.short.toInt()
            val operation = buffer.int
            val sequence = buffer.int

            if (headerLength !in HEADER_SIZE..packetLength) {
                throw BiliDanmakuProtocolException("直播弹幕数据包长度非法")
            }
            if (packetLength > data.size - start) {
                throw BiliDanmakuProtocolException("直播弹幕数据包内容不完整")
            }

            val bodyStart = start + headerLength
            val bodyEnd = start + packetLength
            val body = data.copyOfRange(bodyStart, bodyEnd)

            when (protocol) {
                PROTOCOL_JSON,
                PROTOCOL_POPULARITY -> result += LiveDanmakuPacket(protocol, operation, sequence, body)
                PROTOCOL_ZLIB -> result += decodePackets(decompressZlib(body))
                PROTOCOL_BROTLI -> throw BiliDanmakuUnsupportedProtocolException(protocol)
                else -> throw BiliDanmakuUnsupportedProtocolException(protocol)
            }

            buffer.position(bodyEnd)
        }

        return result
    }

    internal fun requireAuthSuccess(packet: LiveDanmakuPacket) {
        val body = packet.body.toString(Charsets.UTF_8)
        val code = runCatching {
            val element = json.parseToJsonElement(body)
            (element as? JsonObject)
                ?.get("code")
                ?.let { it as? JsonPrimitive }
                ?.intOrNull
        }.getOrNull()

        if (code != 0) {
            throw BiliDanmakuAuthException("直播弹幕鉴权失败：$body")
        }
    }

    private fun decompressZlib(data: ByteArray): ByteArray {
        val inflater = Inflater()
        inflater.setInput(data)

        return try {
            ByteArrayOutputStream(data.size).use { output ->
                val buffer = ByteArray(1024)
                while (!inflater.finished()) {
                    val count = inflater.inflate(buffer)
                    if (count == 0) {
                        if (inflater.needsInput() || inflater.needsDictionary()) break
                    } else {
                        output.write(buffer, 0, count)
                    }
                }
                output.toByteArray()
            }
        } catch (e: Exception) {
            throw BiliDanmakuProtocolException("直播弹幕数据包解压失败：${e.message}")
        } finally {
            inflater.end()
        }
    }
}
