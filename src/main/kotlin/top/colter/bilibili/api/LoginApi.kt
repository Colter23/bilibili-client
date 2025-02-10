package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.client.BiliAuthClient
import top.colter.bilibili.data.login.GeeTestData


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

public fun BiliAuthClient.getCaptcha(): GeeTestData {
    return GeeTestData("", "")
}