package top.colter.bilibili.data.live.danmaku

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * 直播弹幕 WebSocket 连接信息。
 *
 * @param token 弹幕服务器鉴权 token。
 * @param hosts 可用的弹幕服务器列表。
 */
@Serializable
public data class LiveDanmakuInfo(
    @SerialName("token")
    val token: String,
    @SerialName("host_list")
    val hosts: List<LiveDanmakuHost> = emptyList()
)

/**
 * 直播弹幕服务器地址。
 *
 * @param host 服务器域名。
 * @param port TCP 端口。
 * @param wssPort WebSocket TLS 端口。
 * @param wsPort WebSocket 明文端口。
 */
@Serializable
public data class LiveDanmakuHost(
    @SerialName("host")
    val host: String,
    @SerialName("port")
    val port: Int,
    @SerialName("wss_port")
    val wssPort: Int,
    @SerialName("ws_port")
    val wsPort: Int
)

/**
 * 直播弹幕连接选项。
 *
 * @param uid 鉴权使用的用户 ID；为空时优先从 Cookie 读取，读不到则使用匿名用户 0。
 * @param buvid 鉴权使用的 buvid；为空时优先从 Cookie 读取。
 * @param protover 请求的弹幕协议版本，默认 2 表示 zlib 压缩协议。
 * @param heartbeatIntervalMillis 心跳发送间隔，单位毫秒。
 * @param reconnectIntervalMillis 自动重连间隔，单位毫秒。
 * @param autoReconnect WebSocket 异常断开后是否自动重连。
 */
public data class LiveDanmakuOptions(
    val uid: Long? = null,
    val buvid: String? = null,
    val protover: Int = 2,
    val heartbeatIntervalMillis: Long = 30_000L,
    val reconnectIntervalMillis: Long = 5_000L,
    val autoReconnect: Boolean = true
) {
    init {
        require(protover > 0) { "弹幕协议版本必须大于 0" }
        require(heartbeatIntervalMillis > 0) { "心跳间隔必须大于 0" }
        require(reconnectIntervalMillis >= 0) { "重连间隔不能小于 0" }
    }
}

/**
 * 文字弹幕展示信息。
 *
 * @param mode 弹幕模式。
 * @param fontSize 字体大小。
 * @param color 颜色值。
 * @param timestamp 弹幕时间戳。
 * @param emoticon 表情弹幕信息。
 */
@Serializable
public data class LiveDanmuInfo(
    val mode: Int = 0,
    val fontSize: Int = 0,
    val color: Int = 0,
    val timestamp: Long = 0,
    val emoticon: LiveDanmakuEmoticon? = null
)

/**
 * 弹幕表情信息。
 *
 * @param width 表情宽度。
 * @param height 表情高度。
 * @param url 表情图片地址。
 */
@Serializable
public data class LiveDanmakuEmoticon(
    val width: Int = 0,
    val height: Int = 0,
    val url: String = ""
)

/**
 * 弹幕用户信息。
 *
 * @param uid 用户 ID。
 * @param name 用户昵称。
 * @param isAdmin 是否为房管。
 * @param guardLevel 大航海等级，0 表示无。
 * @param face 用户头像地址。
 * @param nameColor 用户名颜色。
 */
@Serializable
public data class LiveDanmakuUser(
    val uid: Long = 0,
    val name: String = "",
    val isAdmin: Boolean = false,
    val guardLevel: Int = 0,
    val face: String? = null,
    val nameColor: String? = null
)

/**
 * 粉丝勋章信息。
 *
 * @param anchorRoomId 勋章所属主播房间号。
 * @param anchorName 勋章所属主播昵称。
 * @param guardLevel 大航海等级，0 表示无。
 * @param medalLevel 勋章等级。
 * @param medalName 勋章名称。
 * @param targetId 勋章所属主播用户 ID。
 */
@Serializable
public data class LiveDanmakuFansMedal(
    val anchorRoomId: Long = 0,
    val anchorName: String? = null,
    val guardLevel: Int = 0,
    val medalLevel: Int = 0,
    val medalName: String = "",
    val targetId: Long = 0
)

/**
 * 礼物基础信息。
 *
 * @param giftId 礼物 ID。
 * @param giftName 礼物名称。
 * @param num 礼物数量。
 */
@Serializable
public data class LiveDanmakuGift(
    val giftId: Long = 0,
    val giftName: String = "",
    val num: Int = 0
)

/**
 * 直播弹幕消息。
 *
 * 目前只解析常用消息类型，未知消息不会从 Flow 中发出。
 */
public sealed class LiveDanmakuMessage {
    /**
     * 直播间 ID。
     */
    public abstract val roomId: Long

    /**
     * 原始消息命令。
     */
    public abstract val cmd: String

    /**
     * 原始 JSON 消息体。
     */
    public abstract val raw: JsonObject

    /**
     * 文字弹幕消息。
     *
     * @param roomId 直播间 ID。
     * @param cmd 原始消息命令。
     * @param raw 原始 JSON 消息体。
     * @param content 弹幕正文。
     * @param user 发送用户。
     * @param danmuInfo 弹幕展示信息。
     * @param fansMedal 发送用户佩戴的粉丝勋章。
     */
    public data class Danmu(
        override val roomId: Long,
        override val cmd: String,
        override val raw: JsonObject,
        val content: String,
        val user: LiveDanmakuUser,
        val danmuInfo: LiveDanmuInfo,
        val fansMedal: LiveDanmakuFansMedal? = null
    ) : LiveDanmakuMessage()

    /**
     * 上舰或续费大航海消息。
     *
     * @param roomId 直播间 ID。
     * @param cmd 原始消息命令。
     * @param raw 原始 JSON 消息体。
     * @param uid 用户 ID。
     * @param username 用户昵称。
     * @param guardLevel 大航海等级，1 总督、2 提督、3 舰长。
     * @param num 购买数量。
     * @param startTime 开始时间戳。
     * @param price 价格，单位沿用 B 站原始字段。
     * @param roleName 大航海身份名称。
     */
    public data class Guard(
        override val roomId: Long,
        override val cmd: String,
        override val raw: JsonObject,
        val uid: Long,
        val username: String,
        val guardLevel: Int,
        val num: Int,
        val startTime: Long,
        val price: Int = 0,
        val roleName: String? = null
    ) : LiveDanmakuMessage()

    /**
     * SuperChat 消息。
     *
     * @param roomId 直播间 ID。
     * @param cmd 原始消息命令。
     * @param raw 原始 JSON 消息体。
     * @param id SuperChat ID。
     * @param uid 用户 ID。
     * @param user 发送用户。
     * @param price 价格，单位元。
     * @param message SuperChat 正文。
     * @param messageTrans 翻译正文。
     * @param time 展示时长，单位秒。
     * @param startTime 开始时间戳。
     * @param endTime 结束时间戳。
     * @param medal 粉丝勋章。
     * @param gift SuperChat 礼物信息。
     */
    public data class SuperChat(
        override val roomId: Long,
        override val cmd: String,
        override val raw: JsonObject,
        val id: Long,
        val uid: Long,
        val user: LiveDanmakuUser,
        val price: Int,
        val message: String,
        val messageTrans: String,
        val time: Int,
        val startTime: Long,
        val endTime: Long,
        val medal: LiveDanmakuFansMedal? = null,
        val gift: LiveDanmakuGift? = null
    ) : LiveDanmakuMessage()

    /**
     * 礼物消息。
     *
     * @param roomId 直播间 ID。
     * @param cmd 原始消息命令。
     * @param raw 原始 JSON 消息体。
     * @param uid 用户 ID。
     * @param username 用户昵称。
     * @param giftId 礼物 ID。
     * @param giftName 礼物名称。
     * @param giftType 礼物类型。
     * @param num 礼物数量。
     * @param price 单价或总价，单位沿用 B 站原始字段。
     * @param timestamp 时间戳。
     * @param medal 粉丝勋章。
     */
    public data class Gift(
        override val roomId: Long,
        override val cmd: String,
        override val raw: JsonObject,
        val uid: Long,
        val username: String,
        val giftId: Long,
        val giftName: String,
        val giftType: Int,
        val num: Int,
        val price: Int,
        val timestamp: Long,
        val medal: LiveDanmakuFansMedal? = null
    ) : LiveDanmakuMessage()

    /**
     * 开播消息。
     *
     * @param roomId 直播间 ID。
     * @param cmd 原始消息命令。
     * @param raw 原始 JSON 消息体。
     * @param liveKey 开播标识。
     * @param liveTime 开播时间戳。
     */
    public data class LiveStart(
        override val roomId: Long,
        override val cmd: String,
        override val raw: JsonObject,
        val liveKey: String? = null,
        val liveTime: Long? = null
    ) : LiveDanmakuMessage()

    /**
     * 下播消息。
     *
     * @param roomId 直播间 ID。
     * @param cmd 原始消息命令。
     * @param raw 原始 JSON 消息体。
     */
    public data class LiveStop(
        override val roomId: Long,
        override val cmd: String,
        override val raw: JsonObject
    ) : LiveDanmakuMessage()
}
