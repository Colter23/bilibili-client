package top.colter.bilibili.auth

import top.colter.bilibili.api.getCaptcha
import top.colter.bilibili.client.BiliAuthClient
import top.colter.bilibili.data.login.GeeTestData
import top.colter.bilibili.data.login.GeeTestResult


public fun BiliAuthClient.captcha(data: (GeeTestData) -> String): GeeTestResult {

    data(getCaptcha())
    return GeeTestResult("", "")
}
