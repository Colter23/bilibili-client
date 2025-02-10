package top.colter.bilibili.auth

import io.ktor.http.*
import top.colter.bilibili.api.getCaptcha
import top.colter.bilibili.client.BiliAuthClient
import top.colter.bilibili.data.login.GeeTestData
import top.colter.bilibili.data.login.GeeTestResult

public class BiliQrCodeAuth {
}

public fun BiliAuthClient.qrCode(block: (data: GeeTestData) -> String) {
    val geeTestData = getCaptcha()
    val res = block(geeTestData)
//    var validate = ""
//    var seccode = ""
    res.split("&").forEach {

    }

}

public suspend fun BiliAuthClient.qrCode1(block: suspend (data: String) -> Unit) {
    val geeTestData = getCaptcha()
    block(geeTestData.challenge)

    while (true) {

    }

}