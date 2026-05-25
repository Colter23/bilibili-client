package top.colter.bilibili.api

import io.ktor.client.request.forms.FormDataContent
import io.ktor.http.Parameters
import io.ktor.http.ParametersBuilder
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCookiesStorage
import top.colter.bilibili.exception.BiliAuthException

internal fun formBody(block: ParametersBuilder.() -> Unit): FormDataContent = FormDataContent(Parameters.build(block))

internal suspend fun BiliClient.requireCsrf(csrf: String?): String {
    if (!csrf.isNullOrBlank()) return csrf
    return storage.getCookies(BiliCookiesStorage.DEFAULT_URL)
        .firstOrNull { it.name == "bili_jct" }
        ?.value
        ?: throw BiliAuthException("缺少 CSRF Token，请先登录或手动传入 csrf")
}

internal fun Iterable<Long>.joinIds(): String = joinToString(",")
