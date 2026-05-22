package top.colter.bilibili.client

import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.colter.bilibili.data.EditCookie
import top.colter.bilibili.data.toCookie
import top.colter.bilibili.data.toEditCookie
import top.colter.bilibili.tools.decode
import top.colter.bilibili.tools.json

/**
 * Cookie container.
 */
@Serializable(CookiesStorageSerializer::class)
public class BiliCookiesStorage(
    storage: AcceptAllCookiesStorage = AcceptAllCookiesStorage()
) : CookiesStorage {

    public companion object {
        public val DEFAULT_URL: Url = Url("https://www.bilibili.com/")
    }

    private var storageRef: AcceptAllCookiesStorage = storage
    private val snapshot: MutableList<Cookie> = mutableListOf()

    override suspend fun get(requestUrl: Url): List<Cookie> = storageRef.get(requestUrl)

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        storageRef.addCookie(requestUrl, cookie)
        upsertSnapshot(cookie)
    }

    override fun close() {
        storageRef.close()
    }

    public fun initialize(cookies: List<Cookie>) {
        runBlocking { importCookies(cookies = cookies, replace = true) }
    }

    public suspend fun importCookies(
        cookies: List<Cookie>,
        replace: Boolean = true,
        requestUrl: Url = DEFAULT_URL
    ) {
        if (replace) {
            storageRef.close()
            storageRef = AcceptAllCookiesStorage()
            snapshot.clear()
        }
        cookies.forEach { addCookie(resolveRequestUrl(requestUrl, it), it) }
    }

    public suspend fun importEditCookies(
        cookies: List<EditCookie>,
        replace: Boolean = true,
        requestUrl: Url = DEFAULT_URL
    ) {
        importCookies(cookies.map { it.toCookie() }, replace, requestUrl)
    }

    public suspend fun importEditCookiesJson(
        cookiesJson: String,
        replace: Boolean = true,
        requestUrl: Url = DEFAULT_URL
    ) {
        importEditCookies(cookiesJson.decode(), replace, requestUrl)
    }

    public fun exportCookies(): List<Cookie> = snapshot.toList()

    public fun exportEditCookies(): List<EditCookie> = exportCookies().map { it.toEditCookie() }

    public fun exportEditCookiesJson(): String = json.encodeToString(exportEditCookies())

    private fun upsertSnapshot(cookie: Cookie) {
        val index = snapshot.indexOfFirst {
            it.name == cookie.name &&
                it.domain.orEmpty() == cookie.domain.orEmpty() &&
                it.path.orEmpty() == cookie.path.orEmpty()
        }
        if (index >= 0) {
            snapshot[index] = cookie
        } else {
            snapshot.add(cookie)
        }
    }

    private fun resolveRequestUrl(defaultUrl: Url, cookie: Cookie): Url {
        val domain = cookie.domain?.removePrefix(".")
        val path = cookie.path?.takeIf { it.isNotBlank() } ?: "/"
        return if (domain.isNullOrBlank()) defaultUrl else Url("https://$domain$path")
    }
}

public object CookiesStorageSerializer : KSerializer<BiliCookiesStorage> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(BiliCookiesStorage::class.qualifiedName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BiliCookiesStorage = BiliCookiesStorage().apply {
        initialize(decoder.decodeString().decode())
    }

    override fun serialize(encoder: Encoder, value: BiliCookiesStorage) {
        encoder.encodeString(json.encodeToString(value.exportCookies()))
    }
}
