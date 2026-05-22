package top.colter.bilibili.auth

import top.colter.bilibili.api.getGeeTestData
import top.colter.bilibili.client.BiliAuthClient
import top.colter.bilibili.data.login.GeeTestData
import top.colter.bilibili.data.login.GeeTestResult


/**
 * ## 人机验证
 *
 * 获取极验参数并交给调用者完成人机验证。
 *
 * @param data 极验参数处理回调
 */
public suspend fun BiliAuthClient.captcha(data: (GeeTestData) -> String): GeeTestResult {

    data(getGeeTestData())
    return GeeTestResult("", "")
}
