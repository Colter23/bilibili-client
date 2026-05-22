package top.colter.bilibili.client

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.request.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.isActive
import kotlinx.coroutines.supervisorScope
import top.colter.bilibili.exception.BiliEmptyException
import top.colter.bilibili.tools.decode

public interface KtorClient : Closeable {
    public val clientCount: Int
    public val clients: MutableList<HttpClient>
    public val proxys: MutableList<ProxyConfig> get() = mutableListOf()

    public fun initClient(): HttpClient
}

public abstract class AbstractKtorClient : KtorClient {

    override val clientCount: Int = 3

    override val clients: MutableList<HttpClient>
        get() = MutableList(clientCount) { initClient() }

    public suspend inline fun <reified T> get(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
        return useHttpClient<String> {
            it.get(url) { block() }.body()
        }.decode()
    }

    public suspend inline fun <reified T> post(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
        return useHttpClient<String> {
            it.post(url) { block() }.body()
        }.decode()
    }

    public open val retry: Int = 3

    private var clientIndex = 0
    private var proxyIndex = 0
    private var retryIndex = 0

    public suspend fun <R> useHttpClient(block: suspend (HttpClient) -> R): R = supervisorScope {
        while (isActive) {
            try {
                val client = clients[clientIndex]
                if (proxys.isNotEmpty()) {
                    client.engineConfig.proxy = proxys[proxyIndex]
                    proxyIndex = (proxyIndex + 1) % proxys.size
                }
                return@supervisorScope block(client).apply { retryIndex = 0 }
            } catch (e: Exception) {
                if (isActive && retryIndex < retry && (e is IOException)) {
                    retryIndex ++
                    clientIndex = (clientIndex + 1) % clients.size
                } else {
                    retryIndex = 0
                    throw e
                }
            }
        }
        throw CancellationException()
    }

    override fun close(): Unit = clients.forEach { it.close() }

}

public suspend inline fun <reified T: BaseResult, reified R> AbstractKtorClient.getData(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): R {
     return (get<T>(url, block).apply { handleStatus() }.data ?: throw BiliEmptyException()).decode()
}

/**
 * ## GET 请求并返回完整响应
 *
 * 执行状态码校验，但不强制要求响应中存在 data/result 字段。
 */
public suspend inline fun <reified T: BaseResult> AbstractKtorClient.getResult(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
    return get<T>(url, block).apply { handleStatus() }
}

/**
 * ## POST 请求并解析 data
 *
 * 执行状态码校验后，将 data/result 字段反序列化为 [R]。
 */
public suspend inline fun <reified T: BaseResult, reified R> AbstractKtorClient.postData(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): R {
    return (post<T>(url, block).apply { handleStatus() }.data ?: throw BiliEmptyException()).decode()
}

/**
 * ## POST 请求并返回完整响应
 *
 * 执行状态码校验，但不强制要求响应中存在 data/result 字段。
 */
public suspend inline fun <reified T: BaseResult> AbstractKtorClient.postResult(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
    return post<T>(url, block).apply { handleStatus() }
}

//    val ss = S::class
//    val state = ss.sealedSubclasses.mapNotNull { it.objectInstance }
//    val s = state.find { it.code == res.code }
//    if (s == null) state.first().unknownCode(res) else s.handle()
