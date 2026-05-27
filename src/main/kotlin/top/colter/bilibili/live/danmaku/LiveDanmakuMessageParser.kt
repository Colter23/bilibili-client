package top.colter.bilibili.live.danmaku

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import top.colter.bilibili.data.live.danmaku.LiveDanmakuEmoticon
import top.colter.bilibili.data.live.danmaku.LiveDanmakuFansMedal
import top.colter.bilibili.data.live.danmaku.LiveDanmakuGift
import top.colter.bilibili.data.live.danmaku.LiveDanmakuMessage
import top.colter.bilibili.data.live.danmaku.LiveDanmakuUser
import top.colter.bilibili.data.live.danmaku.LiveDanmuInfo
import top.colter.bilibili.tools.json

internal object LiveDanmakuMessageParser {
    internal fun parseMessage(text: String, roomId: Long): LiveDanmakuMessage? {
        val raw = json.parseToJsonElement(text) as? JsonObject ?: return null
        return parseJson(raw, roomId)
    }

    internal fun parseJson(raw: JsonObject, roomId: Long): LiveDanmakuMessage? {
        val cmd = raw["cmd"].stringOrNull() ?: return null
        return when (cmd.substringBefore(":")) {
            "DANMU_MSG" -> parseDanmu(raw, roomId, cmd)
            "GUARD_BUY",
            "USER_TOAST_MSG" -> parseGuard(raw, roomId, cmd)
            "SUPER_CHAT_MESSAGE",
            "SUPER_CHAT_MESSAGE_JPN" -> parseSuperChat(raw, roomId, cmd)
            "SEND_GIFT" -> parseGift(raw, roomId, cmd)
            "LIVE" -> parseLiveStart(raw, roomId, cmd)
            "PREPARING" -> LiveDanmakuMessage.LiveStop(roomId, cmd, raw)
            else -> null
        }
    }

    private fun parseDanmu(raw: JsonObject, roomId: Long, cmd: String): LiveDanmakuMessage? {
        val info = raw["info"] as? JsonArray ?: return null
        val danmuInfoData = info.getOrNull(0) as? JsonArray ?: JsonArray(emptyList())
        val userData = info.getOrNull(2) as? JsonArray ?: JsonArray(emptyList())
        val medalData = info.getOrNull(3) as? JsonArray ?: JsonArray(emptyList())

        val content = info.stringAt(1).orEmpty()
        val user = LiveDanmakuUser(
            uid = userData.longAt(0) ?: 0,
            name = userData.stringAt(1).orEmpty(),
            isAdmin = userData.intAt(2) == 1,
            nameColor = userData.stringAt(7)
        )
        val danmuInfo = LiveDanmuInfo(
            mode = danmuInfoData.intAt(0) ?: 0,
            fontSize = danmuInfoData.intAt(2) ?: 0,
            color = danmuInfoData.intAt(3) ?: 0,
            timestamp = danmuInfoData.longAt(4) ?: 0,
            emoticon = parseEmoticon(danmuInfoData.getOrNull(13))
        )

        return LiveDanmakuMessage.Danmu(
            roomId = roomId,
            cmd = cmd,
            raw = raw,
            content = content,
            user = user,
            danmuInfo = danmuInfo,
            fansMedal = parseMedalArray(medalData)
        )
    }

    private fun parseGuard(raw: JsonObject, roomId: Long, cmd: String): LiveDanmakuMessage? {
        val data = raw["data"] as? JsonObject ?: return null
        return LiveDanmakuMessage.Guard(
            roomId = roomId,
            cmd = cmd,
            raw = raw,
            uid = data.firstLong("uid") ?: 0,
            username = data.firstString("username", "uname", "user_name").orEmpty(),
            guardLevel = data.firstInt("guard_level") ?: 0,
            num = data.firstInt("num") ?: 0,
            startTime = data.firstLong("start_time", "timestamp") ?: 0,
            price = data.firstInt("price") ?: 0,
            roleName = data.firstString("role_name")
        )
    }

    private fun parseSuperChat(raw: JsonObject, roomId: Long, cmd: String): LiveDanmakuMessage? {
        val data = raw["data"] as? JsonObject ?: return null
        val userInfo = data["user_info"] as? JsonObject
        val uid = data.firstLong("uid") ?: 0
        val user = LiveDanmakuUser(
            uid = uid,
            name = userInfo?.firstString("uname", "name").orEmpty(),
            guardLevel = userInfo?.firstInt("guard_level") ?: 0,
            face = userInfo?.firstString("face"),
            nameColor = userInfo?.firstString("name_color")
        )

        return LiveDanmakuMessage.SuperChat(
            roomId = roomId,
            cmd = cmd,
            raw = raw,
            id = data.firstLong("id") ?: 0,
            uid = uid,
            user = user,
            price = data.firstInt("price") ?: 0,
            message = data.firstString("message").orEmpty(),
            messageTrans = data.firstString("message_trans").orEmpty(),
            time = data.firstInt("time") ?: 0,
            startTime = data.firstLong("start_time") ?: 0,
            endTime = data.firstLong("end_time") ?: 0,
            medal = parseMedalObject(data["medal_info"] as? JsonObject),
            gift = parseGiftObject(data["gift"] as? JsonObject)
        )
    }

    private fun parseGift(raw: JsonObject, roomId: Long, cmd: String): LiveDanmakuMessage? {
        val data = raw["data"] as? JsonObject ?: return null
        return LiveDanmakuMessage.Gift(
            roomId = roomId,
            cmd = cmd,
            raw = raw,
            uid = data.firstLong("uid") ?: 0,
            username = data.firstString("uname", "username").orEmpty(),
            giftId = data.firstLong("giftId", "gift_id") ?: 0,
            giftName = data.firstString("giftName", "gift_name").orEmpty(),
            giftType = data.firstInt("giftType", "gift_type") ?: 0,
            num = data.firstInt("num") ?: 0,
            price = data.firstInt("price") ?: 0,
            timestamp = data.firstLong("timestamp") ?: 0,
            medal = parseMedalObject(data["medal_info"] as? JsonObject)
        )
    }

    private fun parseLiveStart(raw: JsonObject, roomId: Long, cmd: String): LiveDanmakuMessage {
        return LiveDanmakuMessage.LiveStart(
            roomId = roomId,
            cmd = cmd,
            raw = raw,
            liveKey = raw.firstString("live_key"),
            liveTime = raw.firstLong("live_time")
        )
    }

    private fun parseGiftObject(data: JsonObject?): LiveDanmakuGift? {
        if (data == null || data.isEmpty()) return null
        return LiveDanmakuGift(
            giftId = data.firstLong("gift_id", "giftId") ?: 0,
            giftName = data.firstString("gift_name", "giftName").orEmpty(),
            num = data.firstInt("num") ?: 0
        )
    }

    private fun parseMedalObject(data: JsonObject?): LiveDanmakuFansMedal? {
        if (data == null || data.isEmpty()) return null
        val medal = LiveDanmakuFansMedal(
            anchorRoomId = data.firstLong("anchor_roomid", "anchor_room_id") ?: 0,
            anchorName = data.firstString("anchor_uname", "anchor_name"),
            guardLevel = data.firstInt("guard_level") ?: 0,
            medalLevel = data.firstInt("medal_level") ?: 0,
            medalName = data.firstString("medal_name").orEmpty(),
            targetId = data.firstLong("target_id") ?: 0
        )
        return medal.takeUnless { it.isEmpty() }
    }

    private fun parseMedalArray(data: JsonArray): LiveDanmakuFansMedal? {
        if (data.isEmpty()) return null
        val medal = LiveDanmakuFansMedal(
            anchorRoomId = data.longAt(3) ?: 0,
            anchorName = data.stringAt(2),
            guardLevel = data.intAt(6) ?: 0,
            medalLevel = data.intAt(0) ?: 0,
            medalName = data.stringAt(1).orEmpty(),
            targetId = data.longAt(12) ?: 0
        )
        return medal.takeUnless { it.isEmpty() }
    }

    private fun parseEmoticon(data: JsonElement?): LiveDanmakuEmoticon? {
        val emoticon = when (data) {
            is JsonObject -> data
            is JsonPrimitive -> data.contentOrNull
                ?.takeIf { it.isNotBlank() && it != "{}" }
                ?.let { runCatching { json.parseToJsonElement(it) as? JsonObject }.getOrNull() }
            else -> null
        } ?: return null

        if (emoticon.isEmpty()) return null
        return LiveDanmakuEmoticon(
            width = emoticon.firstInt("width") ?: 0,
            height = emoticon.firstInt("height") ?: 0,
            url = emoticon.firstString("url").orEmpty()
        )
    }

    private fun LiveDanmakuFansMedal.isEmpty(): Boolean {
        return anchorRoomId == 0L &&
            anchorName == null &&
            guardLevel == 0 &&
            medalLevel == 0 &&
            medalName.isEmpty() &&
            targetId == 0L
    }

    private fun JsonObject.firstString(vararg names: String): String? {
        return names.firstNotNullOfOrNull { this[it].stringOrNull() }
    }

    private fun JsonObject.firstInt(vararg names: String): Int? {
        return names.firstNotNullOfOrNull { this[it].intOrNull() }
    }

    private fun JsonObject.firstLong(vararg names: String): Long? {
        return names.firstNotNullOfOrNull { this[it].longOrNull() }
    }

    private fun JsonArray.stringAt(index: Int): String? = getOrNull(index).stringOrNull()

    private fun JsonArray.intAt(index: Int): Int? = getOrNull(index).intOrNull()

    private fun JsonArray.longAt(index: Int): Long? = getOrNull(index).longOrNull()

    private fun JsonElement?.stringOrNull(): String? = (this as? JsonPrimitive)?.contentOrNull

    private fun JsonElement?.intOrNull(): Int? = (this as? JsonPrimitive)?.intOrNull

    private fun JsonElement?.longOrNull(): Long? = (this as? JsonPrimitive)?.longOrNull
}
