package top.colter.bilibili.client

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import top.colter.bilibili.api.getWbi
import top.colter.bilibili.data.user.WbiImg
import top.colter.bilibili.exception.BiliRequestException
import top.colter.bilibili.tools.md5
import java.net.URLEncoder
import java.time.LocalDate
import java.util.WeakHashMap

private val wbiCaches: MutableMap<BiliClient, WbiCache> = WeakHashMap()

internal suspend inline fun <reified T> BiliClient.getDataWithWbi(
    url: String,
    crossinline block: HttpRequestBuilder.() -> Unit = {}
): T {
    val builder = HttpRequestBuilder()
    builder.block()
    val wts = System.currentTimeMillis() / 1000
    val mixinKey = getVerifyString()
    val allParams = mutableMapOf<String, String>()
    builder.url.parameters.build().forEach { key, values ->
        allParams[key] = values.firstOrNull() ?: ""
    }
    allParams["wts"] = wts.toString()
    val sortedQuery = allParams.entries
        .sortedBy { it.key }
        .joinToString("&") { "${it.key.encodeWbiParam()}=${it.value.encodeWbiParam()}" }
    val wrid = "$sortedQuery$mixinKey".md5()
    return getData(url) {
        block()
        parameter("w_rid", wrid)
        parameter("wts", wts)
    }
}

@PublishedApi
internal suspend fun BiliClient.getVerifyString(): String {
    val wbi = getWbiImg()
    val r = splitUrl(wbi.imgUrl) + splitUrl(wbi.subUrl)
    val array = intArrayOf(
        46, 47, 18, 2, 53, 8, 23, 32, 15, 50, 10, 31, 58,
        3, 45, 35, 27, 43, 5, 49, 33, 9, 42, 19, 29, 28,
        14, 39, 12, 38, 41, 13, 37, 48, 7, 16, 24, 55,
        40, 61, 26, 17, 0, 1, 60, 51, 30, 4, 22, 25, 54,
        21, 56, 59, 6, 63, 57, 62, 11, 36, 20, 34, 44, 52
    )
    return buildString {
        array.forEach { t -> if (t < r.length) append(r[t]) }
    }.slice(IntRange(0, 31))
}

private suspend fun BiliClient.getWbiImg(): WbiImg {
    val now = LocalDate.now()
    val cache = synchronized(wbiCaches) {
        wbiCaches.getOrPut(this) { WbiCache() }
    }
    if (now.isAfter(cache.lastWbiTime) || cache.wbiImg == null) {
        cache.lastWbiTime = now
        cache.wbiImg = getWbi()
    }
    return cache.wbiImg ?: throw BiliRequestException("无法获取WBI信息")
}

private fun String.encodeWbiParam(): String {
    return URLEncoder.encode(this, Charsets.UTF_8.name()).replace("+", "%20")
}

private class WbiCache {
    var lastWbiTime: LocalDate = LocalDate.now()
    var wbiImg: WbiImg? = null
}

private fun splitUrl(url: String): String {
    return url.removeSuffix("/").split("/").last().split(".").first()
}
