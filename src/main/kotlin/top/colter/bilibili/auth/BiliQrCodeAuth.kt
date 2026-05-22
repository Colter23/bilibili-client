package top.colter.bilibili.auth

import kotlinx.coroutines.delay
import top.colter.bilibili.api.getLoginQrCode
import top.colter.bilibili.api.pollLoginQrCode
import top.colter.bilibili.client.BiliAuthClient
import top.colter.bilibili.data.login.QrCodeLoginData
import top.colter.bilibili.data.login.QrCodeLoginResult
import top.colter.bilibili.data.login.QrCodeLoginStatus
import top.colter.bilibili.exception.BiliAuthException

public class BiliQrCodeAuth

/**
 * ## 二维码登录
 *
 * 获取扫码登录链接并持续轮询登录状态。调用者需要在 [onQrCode] 中把 [QrCodeLoginData.url]
 * 编码成二维码展示给用户；登录成功后，Ktor 的 CookieStorage 会保存服务端写入的登录 Cookie。
 *
 * @param intervalMillis 轮询间隔，默认 1 秒
 * @param timeoutMillis 登录等待超时时间，默认 180 秒；小于等于 0 表示不额外限制超时
 * @param onQrCode 二维码数据回调
 * @param onStatus 每次轮询状态回调
 */
public suspend fun BiliAuthClient.loginByQrCode(
    intervalMillis: Long = 1_000L,
    timeoutMillis: Long = 180_000L,
    onQrCode: suspend (data: QrCodeLoginData) -> Unit,
    onStatus: suspend (data: QrCodeLoginResult) -> Unit = {},
): QrCodeLoginResult {
    require(intervalMillis > 0) { "intervalMillis 必须大于 0" }

    val qrCode = getLoginQrCode()
    onQrCode(qrCode)

    val startTime = System.currentTimeMillis()
    while (true) {
        val result = pollLoginQrCode(qrCode.qrcodeKey)
        onStatus(result)

        when (result.status) {
            QrCodeLoginStatus.SUCCESS -> return result
            QrCodeLoginStatus.EXPIRED -> throw BiliAuthException("二维码已失效，请重新获取")
            QrCodeLoginStatus.SCANNED,
            QrCodeLoginStatus.WAITING -> {
                if (timeoutMillis > 0 && System.currentTimeMillis() - startTime >= timeoutMillis) {
                    throw BiliAuthException("二维码登录超时，请重新获取")
                }
                delay(intervalMillis)
            }
            QrCodeLoginStatus.UNKNOWN -> throw BiliAuthException("未知二维码登录状态: ${result.code} ${result.message}")
        }
    }
}

/**
 * ## 二维码登录
 *
 * [loginByQrCode] 的简短别名。
 *
 * @see loginByQrCode
 */
public suspend fun BiliAuthClient.qrCode(
    intervalMillis: Long = 1_000L,
    timeoutMillis: Long = 180_000L,
    onQrCode: suspend (data: QrCodeLoginData) -> Unit,
    onStatus: suspend (data: QrCodeLoginResult) -> Unit = {},
): QrCodeLoginResult {
    return loginByQrCode(intervalMillis, timeoutMillis, onQrCode, onStatus)
}
