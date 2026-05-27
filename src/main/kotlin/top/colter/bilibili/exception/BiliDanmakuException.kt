package top.colter.bilibili.exception

/**
 * 直播弹幕通用异常。
 */
public open class BiliDanmakuException(
    message: String = "BiliBili 直播弹幕异常"
) : BiliException(message)

/**
 * 直播弹幕 WebSocket 鉴权异常。
 */
public class BiliDanmakuAuthException(
    message: String = "BiliBili 直播弹幕鉴权失败"
) : BiliDanmakuException(message)

/**
 * 直播弹幕协议解析异常。
 */
public open class BiliDanmakuProtocolException(
    message: String = "BiliBili 直播弹幕协议异常"
) : BiliDanmakuException(message)

/**
 * 直播弹幕协议版本不支持。
 *
 * @param protocol 服务端返回的协议版本。
 */
public class BiliDanmakuUnsupportedProtocolException(
    public val protocol: Int
) : BiliDanmakuProtocolException("不支持的 BiliBili 直播弹幕协议版本：$protocol")
