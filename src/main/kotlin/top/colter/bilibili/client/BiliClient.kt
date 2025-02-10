package top.colter.bilibili.client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
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

}

//class BiliAccountClient(
//    override val info: AccountUser,
//    override val owner: User,
//    override val cookie: BiliCookiesStorage,
//    override val authApps: MutableList<BiliApplication>
//): AbstractKtorClient(), BiliAccount {
//
//    override fun initClient() = HttpClient(OkHttp) {
//        defaultRequest {
//            header(HttpHeaders.Origin, "https://t.bilibili.com")
//            header(HttpHeaders.Referrer, "https://t.bilibili.com")
//        }
//        install(HttpTimeout) {
//            socketTimeoutMillis = 10_000L
//            connectTimeoutMillis = 10_000L
//            requestTimeoutMillis = 10_000L
//        }
//        install(HttpCookies) {
//            storage = cookie
//        }
//        expectSuccess = true
//        Json { json }
//        BrowserUserAgent()
//        ContentEncoding()
//    }
//
//}

public suspend inline fun <reified T> BiliClient.getData(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T{
    return getData<BiliCommonResult, T>(url, block)
}


