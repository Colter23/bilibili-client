package top.colter.bilibili.client

import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.colter.bilibili.tools.decode
import top.colter.bilibili.tools.json
import top.colter.bilibili.tools.reflectField


/**
 * Cookie容器
 */
@Serializable(CookiesStorageSerializer::class)
public class BiliCookiesStorage(
    private val storage: AcceptAllCookiesStorage = AcceptAllCookiesStorage()
): CookiesStorage by storage {

    private val AcceptAllCookiesStorage.container: MutableList<Cookie> by reflectField()
    public val container: MutableList<Cookie> = storage.container

    public fun initialize(cookies: List<Cookie>) {
        if (container.isNotEmpty()) throw Exception("容器不为空，无法初始化")
        container.addAll(cookies)
    }

}

public object CookiesStorageSerializer: KSerializer<BiliCookiesStorage> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(BiliCookiesStorage::class.qualifiedName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BiliCookiesStorage = BiliCookiesStorage().apply {
        initialize(decoder.decodeString().decode())
    }

    override fun serialize(encoder: Encoder, value: BiliCookiesStorage) {
        encoder.encodeString(json.encodeToString(value.container))
    }
}