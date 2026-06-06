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
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.serializer
import kotlinx.io.IOException
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

    override val clients: MutableList<HttpClient> by lazy {
        MutableList(clientCount) { initClient() }
    }

    public suspend inline fun <reified T> get(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
        return get(url, serializer<T>()) { block() }
    }

    public suspend inline fun <reified T> post(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
        return post(url, serializer<T>()) { block() }
    }

    public suspend fun <T> get(
        url: String,
        deserializer: DeserializationStrategy<T>,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T {
        return getString(url, block).decode(deserializer)
    }

    public suspend fun <T> post(
        url: String,
        deserializer: DeserializationStrategy<T>,
        block: HttpRequestBuilder.() -> Unit = {}
    ): T {
        return postString(url, block).decode(deserializer)
    }

    public suspend fun getString(url: String, block: HttpRequestBuilder.() -> Unit = {}): String {
        return useHttpClient<String> {
            it.get(url) { block() }.body()
        }
    }

    public suspend fun postString(url: String, block: HttpRequestBuilder.() -> Unit = {}): String {
        return useHttpClient<String> {
            it.post(url) { block() }.body()
        }
    }

    public open val retry: Int = 3

    private var clientIndex = 0
    private var proxyIndex = 0
    private var retryIndex = 0

    public suspend fun <R> useHttpClient(block: suspend (HttpClient) -> R): R = supervisorScope {
        while (isActive) {
            try {
                val client = clients[clientIndex]
                val proxyList = proxys
                if (proxyList.isNotEmpty()) {
                    client.engineConfig.proxy = proxyList[proxyIndex]
                    proxyIndex = (proxyIndex + 1) % proxyList.size
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

/**
 * ## GET 请求并解析 data
 *
 * 执行状态码校验后，将 data/result 字段反序列化为 [R]。
 */
public suspend inline fun <reified T: BaseResult, reified R> AbstractKtorClient.getData(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): R {
     return getData(url, serializer<T>(), serializer<R>()) { block() }
}

public suspend fun <T: BaseResult, R> AbstractKtorClient.getData(
    url: String,
    resultDeserializer: DeserializationStrategy<T>,
    dataDeserializer: DeserializationStrategy<R>,
    block: HttpRequestBuilder.() -> Unit = {}
): R {
    return (get(url, resultDeserializer, block).apply { handleStatus() }.data ?: throw BiliEmptyException())
        .decode(dataDeserializer)
}

/**
 * ## GET 请求并返回完整响应
 *
 * 执行状态码校验，但不强制要求响应中存在 data/result 字段。
 */
public suspend inline fun <reified T: BaseResult> AbstractKtorClient.getResult(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
    return getResult(url, serializer<T>()) { block() }
}

public suspend fun <T: BaseResult> AbstractKtorClient.getResult(
    url: String,
    resultDeserializer: DeserializationStrategy<T>,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return get(url, resultDeserializer, block).apply { handleStatus() }
}

/**
 * ## POST 请求并解析 data
 *
 * 执行状态码校验后，将 data/result 字段反序列化为 [R]。
 */
public suspend inline fun <reified T: BaseResult, reified R> AbstractKtorClient.postData(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): R {
    return postData(url, serializer<T>(), serializer<R>()) { block() }
}

public suspend fun <T: BaseResult, R> AbstractKtorClient.postData(
    url: String,
    resultDeserializer: DeserializationStrategy<T>,
    dataDeserializer: DeserializationStrategy<R>,
    block: HttpRequestBuilder.() -> Unit = {}
): R {
    return (post(url, resultDeserializer, block).apply { handleStatus() }.data ?: throw BiliEmptyException())
        .decode(dataDeserializer)
}

/**
 * ## POST 请求并返回完整响应
 *
 * 执行状态码校验，但不强制要求响应中存在 data/result 字段。
 */
public suspend inline fun <reified T: BaseResult> AbstractKtorClient.postResult(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
    return postResult(url, serializer<T>()) { block() }
}

public suspend fun <T: BaseResult> AbstractKtorClient.postResult(
    url: String,
    resultDeserializer: DeserializationStrategy<T>,
    block: HttpRequestBuilder.() -> Unit = {}
): T {
    return post(url, resultDeserializer, block).apply { handleStatus() }
}

//    val ss = S::class
//    val state = ss.sealedSubclasses.mapNotNull { it.objectInstance }
//    val s = state.find { it.code == res.code }
//    if (s == null) state.first().unknownCode(res) else s.handle()
