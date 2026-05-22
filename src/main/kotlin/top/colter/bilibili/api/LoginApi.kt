package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.BiliAuthClient
import top.colter.bilibili.client.getData
import top.colter.bilibili.data.login.GeeTestData
import top.colter.bilibili.data.login.LoginCaptcha
import top.colter.bilibili.data.login.QrCodeLoginData
import top.colter.bilibili.data.login.QrCodeLoginResult


/////////////////////////////////////////////
//                 扫码登录                 //
/////////////////////////////////////////////

/**
 * ## 获取登录链接 编码为二维码后供b站app扫描登录
 *
 * **Method:** GET
 *
 * **Response:** [BiliCommonResult] / [LoginData]
 *
 * **Example:** https://passport.bilibili.com/x/passport-login/web/qrcode/generate
 */
public const val LOGIN_QRCODE: String = "$BASE_PASSPORT/x/passport-login/web/qrcode/generate"

/**
 * ## 验证登录链接是否被扫描
 *
 * **Method:** GET
 *
 * **Params:** qrcode_key=[LOGIN_QRCODE]获取的key
 *
 * **Response:** [BiliCommonResult] / [LoginResult]
 *
 * **Example:** https://passport.bilibili.com/x/passport-login/web/qrcode/poll?qrcode_key=xxxx
 */
public const val LOGIN_VERIFY: String = "$BASE_PASSPORT/x/passport-login/web/qrcode/poll"


/////////////////////////////////////////////
//                 人机验证                 //
/////////////////////////////////////////////

public const val CAPTCHA: String = "$BASE_PASSPORT/x/passport-login/captcha?source=main_web"

/**
 * ## 获取扫码登录二维码数据
 *
 * @return 二维码内容链接与后续轮询所需的 qrcode_key
 *
 * @see LOGIN_QRCODE
 */
public suspend fun BiliAuthClient.getLoginQrCode(): QrCodeLoginData {
    return getData(LOGIN_QRCODE)
}

/**
 * ## 轮询扫码登录状态
 *
 * @param qrcodeKey [getLoginQrCode] 获取的扫码登录密钥
 *
 * @see LOGIN_VERIFY
 */
public suspend fun BiliAuthClient.pollLoginQrCode(qrcodeKey: String): QrCodeLoginResult {
    return getData(LOGIN_VERIFY) {
        parameter("qrcode_key", qrcodeKey)
    }
}

/**
 * ## 获取登录人机验证信息
 *
 * 扫码登录不需要人机验证，本接口主要供密码登录、短信登录等方式使用。
 *
 * @see CAPTCHA
 */
public suspend fun BiliAuthClient.getCaptcha(): LoginCaptcha {
    return getData(CAPTCHA)
}

/**
 * ## 获取极验人机验证参数
 *
 * @see getCaptcha
 */
public suspend fun BiliAuthClient.getGeeTestData(): GeeTestData {
    return getCaptcha().geetest
}
