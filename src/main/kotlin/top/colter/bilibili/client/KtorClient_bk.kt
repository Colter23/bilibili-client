package top.colter.mirai.plugin.bilibili.client

//interface KtorClient<R : BaseResult> : Closeable {
//
//    val clientCount: Int
////    val baseResult: BaseResult
//
//    fun initClient(): HttpClient
//
//    val codeState: KClass<out CodeState>
//
//    val codeStates: List<CodeState>
////
//////    fun getCodeState(): List<S>
////    fun findCodeState(code: Int): S
//
//    suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit = {}): R
//
//    suspend fun post(url: String, block: HttpRequestBuilder.() -> Unit = {}): R
//
//    suspend fun <R> useHttpClient(block: suspend (HttpClient) -> R): R
//
//}

//suspend inline fun <T> KtorClient.get(url: String, block: HttpRequestBuilder.() -> Unit = {}): T {
//    TODO("Not yet implemented")
//}

//interface BaseResult {
//    val code: Int
//    val message: String
//    val data: JsonElement?
//}

//interface BaseResult {
////    val state: CodeState
//    val code: Int
//    val message: String
//    val data: JsonElement?
//}

//sealed interface CodeState {
//    val code: Int
//    val message: String
//
//    fun han(res: BaseResult) {}
//    fun unhan(res: BaseResult)
//}
//interface Sessues: CodeState
//interface Err: CodeState

//sealed class BCodeState(
//    override val code: Int,
//    override val message: String,
//) : CodeState {
//    override fun unhan(res: BaseResult) {
//        throw Exception("未知状态")
//    }
//
//    class un(code: Int, message: String) : BCodeState(code, message)
//
//    object Sess : BCodeState(0, "sss") {
//        override fun han(res: BaseResult) {
//        }
//    }
//
//    object Err : BCodeState(400, "fff") {
//        override fun han(res: BaseResult) {
//            throw Exception(res.message)
//        }
//    }
//}


//@Serializable
//data class BResult(
//    override val code: Int,
//    override val message: String,
//    override val data: JsonElement?
//) : BaseResult

//abstract class BaseClient : KtorClient<BaseResult> {
//    override val clientCount: Int
//        get() = TODO("Not yet implemented")
//
//    override fun initClient(): HttpClient {
//        TODO("Not yet implemented")
//    }
//
//    override val codeState: KClass<out CodeState>
//        get() = BCodeState::class
//
//    override val codeStates: List<BCodeState> by lazy {
//        codeState.sealedSubclasses.map { it.objectInstance as BCodeState }
//    }
//
//    fun findCodeState(code: Int): BCodeState {
//        return codeStates.firstOrNull() { it.code == code } ?: throw Exception("未知状态")
//    }
//
//    override suspend fun get(url: String, block: HttpRequestBuilder.() -> Unit): BaseResult {
//        return useHttpClient<String> {
//            it.get(url) { block() }.body()
//        }.decode()
//    }
//
//    override suspend fun post(url: String, block: HttpRequestBuilder.() -> Unit): BResult {
//        return useHttpClient<String> {
//            it.post(url) { block() }.body()
//        }.decode()
//    }
//
//    override suspend fun <R> useHttpClient(block: suspend (HttpClient) -> R): R {
//        TODO("Not yet implemented")
//    }
//
//    override fun close() {
//        TODO("Not yet implemented")
//    }
//}

//interface KKKtorClient : Closeable {
//
//}
//
//abstract class KKtorClient : KKKtorClient {
//
//    open val clientCount: Int = 3
////    val baseResult: BaseResult
//
//    val clients: MutableList<HttpClient>
//        get() = MutableList(clientCount) { client() }
//
//    abstract fun client(): HttpClient
//
//    suspend inline fun <reified T> get(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
//        return useHttpClient<String> {
//            it.get(url) { block() }.body()
//        }.decode()
//    }
//
//    suspend inline fun <reified T> post(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
//        return useHttpClient<String> {
//            it.post(url) { block() }.body()
//        }.decode()
//    }
//
//    val proxys: List<ProxyConfig> = listOf()
//
//    open val retry: Int = 3
//
//    private var clientIndex = 0
//    private var proxyIndex = 0
//    private var retryIndex = 0
//
//    suspend fun <R> useHttpClient(block: suspend (HttpClient) -> R): R = supervisorScope {
//        while (isActive) {
//            try {
//                val client = clients[clientIndex]
//                if (proxys.isNotEmpty()) {
//                    client.engineConfig.proxy = proxys[proxyIndex]
//                    proxyIndex = (proxyIndex + 1) % proxys.size
//                }
//                return@supervisorScope block(client).apply { retryIndex = 0 }
//            } catch (e: Exception) {
//                if (isActive && retryIndex < retry && (e is IOException)) {
//                    retryIndex ++
//                    clientIndex = (clientIndex + 1) % clients.size
//                } else {
//                    retryIndex = 0
//                    throw e
//                }
//            }
//        }
//        throw CancellationException()
//    }
//
//}
//
//class BiliClient : KKtorClient() {
//    override fun close() = clients.forEach { it.close() }
//
//    override val clientCount: Int
//        get() = 3
////    val clients = MutableList(clientCount) { client() }
//
//    override fun client() = HttpClient(OkHttp) {
//        defaultRequest {
//            header(HttpHeaders.Origin, "https://t.bilibili.com")
//            header(HttpHeaders.Referrer, "https://t.bilibili.com")
//        }
//        install(HttpTimeout) {
//            socketTimeoutMillis = 10_000L
//            connectTimeoutMillis = 10_000L
//            requestTimeoutMillis = 10_000L
//        }
//        expectSuccess = true
//        Json {
//            json
//        }
//        BrowserUserAgent()
//        ContentEncoding()
//    }
//
//
////    suspend inline fun <reified T> get(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
//////        return useHttpClient<String> {
//////            it.get(url) { block() }.body()
//////        }.decode()
////        return "{\"code\":100,\"message\":\"0\",\"ttl\":1,\"data\":{\"name\":\"Colter\"}}".decode()
////    }
//
////    suspend inline fun <reified T> post(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T {
////        return useHttpClient<String> {
////            it.post(url) { block() }.body()
////        }.decode()
////    }
//
////    val proxys: List<ProxyConfig> = listOf()
////
////    val retry = 3
////
////    private var clientIndex = 0
////    private var proxyIndex = 0
////    private var retryIndex = 0
////
////    override suspend fun <T> useHttpClient(block: suspend (HttpClient) -> T): T = supervisorScope {
////        while (isActive) {
////            try {
////                val client = clients[clientIndex]
////                if (proxys.isNotEmpty()) {
////                    client.engineConfig.proxy = proxys[proxyIndex]
////                    proxyIndex = (proxyIndex + 1) % proxys.size
////                }
////                return@supervisorScope block(client).apply { retryIndex = 0 }
////            } catch (e: Exception) {
////                if (isActive && retryIndex < retry && (e is IOException)) {
////                    retryIndex ++
////                    clientIndex = (clientIndex + 1) % clients.size
////                } else {
////                    retryIndex = 0
////                    throw e
////                }
////            }
////        }
////        throw CancellationException()
////    }
//
//}

// reified S: CodeState
//suspend inline fun <reified T: BaseResult, reified R> KKtorClient.getData(url: String, state: KClass<out CodeState>, crossinline block: HttpRequestBuilder.() -> Unit = {}): R {
//    val res =  get<T>(url, block)
//
////    val state = state::class.sealedSubclasses.map { it.objectInstance.cast<S>() }
//    val ss = state
//
//    val state = ss.sealedSubclasses.mapNotNull { it.objectInstance }
//
//    val s = state.find { it.code == res.code }
//    if (s == null) state.first().unhan(res) else s.han(res)
//
//    val r = res.data ?: throw Exception()
//    return r.decode()
//}

// reified S: CodeState
//suspend inline fun <reified T: BaseResult, reified S: CodeState, reified R> KKtorClient.getData(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): R {
//    val res =  get<T>(url, block)
//
////    val state = state::class.sealedSubclasses.map { it.objectInstance.cast<S>() }
//    val ss = S::class
//
//    val state = ss.sealedSubclasses.mapNotNull { it.objectInstance }
//
//    val s = state.find { it.code == res.code }
//    if (s == null) state.first().unhan(res) else s.han(res)
//
//    val r = res.data ?: throw Exception()
//    return r.decode()
//}

//suspend inline fun <reified T> BiliClient.getData(url: String, crossinline block: HttpRequestBuilder.() -> Unit = {}): T{
//    return getData<BResult, BCodeState, T>(url, block)
//}