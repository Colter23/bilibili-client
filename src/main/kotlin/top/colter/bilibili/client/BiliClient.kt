package top.colter.bilibili.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import top.colter.bilibili.data.EditCookie
import top.colter.bilibili.tools.json

public open class BiliClient(private val timeout: Long = 15_000L): AbstractKtorClient() {

    public open val storage: BiliCookiesStorage = BiliCookiesStorage()

    override fun initClient(): HttpClient = HttpClient(OkHttp) {
        defaultRequest {
            header(HttpHeaders.Origin, "https://t.bilibili.com")
            header(HttpHeaders.Referrer, "https://t.bilibili.com")
        }
        install(HttpTimeout) {
            socketTimeoutMillis = timeout
            connectTimeoutMillis = timeout
            requestTimeoutMillis = null
        }
        install(HttpCookies) {
            storage = this@BiliClient.storage
        }
        expectSuccess = true
        Json { json }
        BrowserUserAgent()
        ContentEncoding()
    }

    public suspend fun importCookies(
        cookies: List<Cookie>,
        replace: Boolean = true,
        requestUrl: Url = BiliCookiesStorage.DEFAULT_URL
    ) {
        storage.importCookies(cookies, replace, requestUrl)
    }

    public suspend fun importEditCookies(
        cookies: List<EditCookie>,
        replace: Boolean = true,
        requestUrl: Url = BiliCookiesStorage.DEFAULT_URL
    ) {
        storage.importEditCookies(cookies, replace, requestUrl)
    }

    public suspend fun importEditCookiesJson(
        cookiesJson: String,
        replace: Boolean = true,
        requestUrl: Url = BiliCookiesStorage.DEFAULT_URL
    ) {
        storage.importEditCookiesJson(cookiesJson, replace, requestUrl)
    }

    public fun exportCookies(): List<Cookie> = storage.exportCookies()

    public fun exportEditCookies(): List<EditCookie> = storage.exportEditCookies()

    public fun exportEditCookiesJson(): String = storage.exportEditCookiesJson()
}

public suspend inline fun <reified T> BiliClient.getData(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T{
    return getData<BiliCommonResult, T>(url, block)
}

/**
 * ## POST 请求并解析 data
 *
 * 使用 [BiliCommonResult] 校验状态码后，将 data/result 字段反序列化为 [T]。
 */
public suspend inline fun <reified T> BiliClient.postData(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
    return postData<BiliCommonResult, T>(url, block)
}

/**
 * ## GET 请求并返回完整通用响应
 *
 * 适用于接口成功时没有 data/result 字段的场景。
 */
public suspend fun BiliClient.getResult(url: String, block: HttpRequestBuilder.() -> Unit = {}): BiliCommonResult {
    return getResult<BiliCommonResult>(url, block)
}

/**
 * ## POST 请求并返回完整通用响应
 *
 * 适用于接口成功时没有 data/result 字段的场景。
 */
public suspend fun BiliClient.postResult(url: String, block: HttpRequestBuilder.() -> Unit = {}): BiliCommonResult {
    return postResult<BiliCommonResult>(url, block)
}
