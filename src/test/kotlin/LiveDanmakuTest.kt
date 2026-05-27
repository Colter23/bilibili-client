import kotlinx.serialization.json.JsonObject
import top.colter.bilibili.data.live.danmaku.LiveDanmakuMessage
import top.colter.bilibili.exception.BiliDanmakuAuthException
import top.colter.bilibili.live.danmaku.LiveDanmakuProtocol
import top.colter.bilibili.live.danmaku.LiveDanmakuMessageParser
import java.nio.ByteBuffer
import java.util.zip.Deflater
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class LiveDanmakuTest {
    @Test
    fun `build auth and heartbeat packets`() {
        val authBody = """{"uid":0}"""
        val authPacket = LiveDanmakuProtocol.buildAuthPacket(authBody)
        val authBuffer = ByteBuffer.wrap(authPacket)

        assertEquals(authPacket.size, authBuffer.int)
        assertEquals(LiveDanmakuProtocol.HEADER_SIZE, authBuffer.short.toInt())
        assertEquals(LiveDanmakuProtocol.PROTOCOL_ZLIB, authBuffer.short.toInt())
        assertEquals(LiveDanmakuProtocol.OP_AUTH, authBuffer.int)
        assertEquals(1, authBuffer.int)
        assertEquals(authBody, authPacket.copyOfRange(LiveDanmakuProtocol.HEADER_SIZE, authPacket.size).decodeToString())

        val heartbeatPacket = LiveDanmakuProtocol.buildHeartbeatPacket()
        val heartbeatBuffer = ByteBuffer.wrap(heartbeatPacket)
        assertEquals(LiveDanmakuProtocol.HEADER_SIZE, heartbeatBuffer.int)
        assertEquals(LiveDanmakuProtocol.HEADER_SIZE, heartbeatBuffer.short.toInt())
        assertEquals(LiveDanmakuProtocol.PROTOCOL_ZLIB, heartbeatBuffer.short.toInt())
        assertEquals(LiveDanmakuProtocol.OP_HEARTBEAT, heartbeatBuffer.int)
    }

    @Test
    fun `decode single multi and zlib packets`() {
        val first = packet("""{"cmd":"LIVE"}""".encodeToByteArray())
        val second = packet("""{"cmd":"PREPARING"}""".encodeToByteArray())

        val single = LiveDanmakuProtocol.decodePackets(first).single()
        assertEquals(LiveDanmakuProtocol.OP_MESSAGE, single.operation)
        assertEquals("""{"cmd":"LIVE"}""", single.body.decodeToString())

        val multi = LiveDanmakuProtocol.decodePackets(first + second)
        assertEquals(2, multi.size)
        assertEquals("""{"cmd":"PREPARING"}""", multi[1].body.decodeToString())

        val compressed = packet(deflate(first + second), protocol = LiveDanmakuProtocol.PROTOCOL_ZLIB)
        val zlib = LiveDanmakuProtocol.decodePackets(compressed)
        assertEquals(2, zlib.size)
        assertEquals("""{"cmd":"LIVE"}""", zlib[0].body.decodeToString())
        assertEquals("""{"cmd":"PREPARING"}""", zlib[1].body.decodeToString())
    }

    @Test
    fun `handle auth reply`() {
        val success = LiveDanmakuProtocol.decodePackets(
            packet("""{"code":0}""".encodeToByteArray(), operation = LiveDanmakuProtocol.OP_AUTH_REPLY)
        ).single()
        LiveDanmakuProtocol.requireAuthSuccess(success)

        val failed = LiveDanmakuProtocol.decodePackets(
            packet("""{"code":-101}""".encodeToByteArray(), operation = LiveDanmakuProtocol.OP_AUTH_REPLY)
        ).single()
        assertFailsWith<BiliDanmakuAuthException> {
            LiveDanmakuProtocol.requireAuthSuccess(failed)
        }
    }

    @Test
    fun `parse common messages`() {
        val roomId = 21811136L

        val danmu = assertIs<LiveDanmakuMessage.Danmu>(
            LiveDanmakuMessageParser.parseMessage(danmuJson, roomId)
        )
        assertEquals("hello", danmu.content)
        assertEquals(12345L, danmu.user.uid)
        assertEquals("tester", danmu.user.name)
        assertEquals(10, danmu.fansMedal?.medalLevel)
        assertEquals("emote.png", danmu.danmuInfo.emoticon?.url?.substringAfterLast("/"))

        val guard = assertIs<LiveDanmakuMessage.Guard>(
            LiveDanmakuMessageParser.parseMessage(guardJson, roomId)
        )
        assertEquals(3, guard.guardLevel)
        assertEquals("guarder", guard.username)

        val superChat = assertIs<LiveDanmakuMessage.SuperChat>(
            LiveDanmakuMessageParser.parseMessage(superChatJson, roomId)
        )
        assertEquals(30, superChat.price)
        assertEquals("sc message", superChat.message)
        assertEquals("tester", superChat.user.name)

        val gift = assertIs<LiveDanmakuMessage.Gift>(
            LiveDanmakuMessageParser.parseMessage(giftJson, roomId)
        )
        assertEquals(1_0001L, gift.giftId)
        assertEquals("辣条", gift.giftName)

        val liveStart = assertIs<LiveDanmakuMessage.LiveStart>(
            LiveDanmakuMessageParser.parseMessage("""{"cmd":"LIVE","live_key":"abc","live_time":1710000000}""", roomId)
        )
        assertEquals("abc", liveStart.liveKey)
        assertEquals(1710000000L, liveStart.liveTime)

        val liveStop = assertIs<LiveDanmakuMessage.LiveStop>(
            LiveDanmakuMessageParser.parseMessage("""{"cmd":"PREPARING"}""", roomId)
        )
        assertIs<JsonObject>(liveStop.raw)
    }

    @Test
    fun `ignore unsupported message`() {
        assertNull(LiveDanmakuMessageParser.parseMessage("""{"cmd":"UNKNOWN_CMD","data":{}}""", 1L))
    }

    private fun packet(
        body: ByteArray,
        operation: Int = LiveDanmakuProtocol.OP_MESSAGE,
        protocol: Int = LiveDanmakuProtocol.PROTOCOL_JSON
    ): ByteArray {
        val buffer = ByteBuffer.wrap(ByteArray(body.size + LiveDanmakuProtocol.HEADER_SIZE))
        buffer.putInt(body.size + LiveDanmakuProtocol.HEADER_SIZE)
        buffer.putShort(LiveDanmakuProtocol.HEADER_SIZE.toShort())
        buffer.putShort(protocol.toShort())
        buffer.putInt(operation)
        buffer.putInt(1)
        buffer.put(body)
        return buffer.array()
    }

    private fun deflate(data: ByteArray): ByteArray {
        val deflater = Deflater()
        deflater.setInput(data)
        deflater.finish()

        val buffer = ByteArray(data.size + 128)
        val count = deflater.deflate(buffer)
        deflater.end()
        return buffer.copyOf(count)
    }

    private val danmuJson: String = """
        {
          "cmd": "DANMU_MSG",
          "info": [
            [1, 0, 25, 16777215, 1710000000, 0, 0, 0, 0, 0, 0, 0, 0, {"width": 64, "height": 64, "url": "https://i0.hdslb.com/emote.png"}],
            "hello",
            [12345, "tester", 0, 0, 0, 0, 0, "#ffffff"],
            [10, "牌子", "anchor", 100, 0, 0, 3, 0, 0, 0, 0, 0, 98765]
          ]
        }
    """.trimIndent()

    private val guardJson: String = """
        {
          "cmd": "GUARD_BUY",
          "data": {
            "uid": 23456,
            "username": "guarder",
            "guard_level": 3,
            "num": 1,
            "start_time": 1710000001,
            "price": 198000,
            "role_name": "舰长"
          }
        }
    """.trimIndent()

    private val superChatJson: String = """
        {
          "cmd": "SUPER_CHAT_MESSAGE",
          "data": {
            "id": 34567,
            "price": 30,
            "message": "sc message",
            "message_trans": "",
            "time": 60,
            "start_time": 1710000002,
            "end_time": 1710000062,
            "uid": 12345,
            "user_info": {
              "uname": "tester",
              "face": "https://example.com/face.jpg",
              "guard_level": 1,
              "name_color": "#ffffff"
            },
            "medal_info": {
              "anchor_roomid": 100,
              "anchor_uname": "anchor",
              "guard_level": 1,
              "medal_level": 10,
              "medal_name": "牌子",
              "target_id": 98765
            },
            "gift": {
              "gift_id": 12000,
              "gift_name": "醒目留言",
              "num": 1
            }
          }
        }
    """.trimIndent()

    private val giftJson: String = """
        {
          "cmd": "SEND_GIFT",
          "data": {
            "uid": 45678,
            "uname": "gifter",
            "giftId": 10001,
            "giftName": "辣条",
            "giftType": 0,
            "num": 3,
            "price": 100,
            "timestamp": 1710000003,
            "medal_info": {
              "anchor_roomid": 100,
              "anchor_uname": "anchor",
              "guard_level": 0,
              "medal_level": 5,
              "medal_name": "牌子",
              "target_id": 98765
            }
          }
        }
    """.trimIndent()
}
