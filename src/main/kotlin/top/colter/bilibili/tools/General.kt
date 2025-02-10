package top.colter.bilibili.tools

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage
import java.io.File
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure


public val biliClient: BiliClient = BiliClient()

public val json: Json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
    allowStructuredMapKeys = true
}

public inline fun <reified T> String.decode(): T = json.parseToJsonElement(this).decode()

public inline fun <reified T> JsonElement.decode(): T {
    return try {
        json.decodeFromJsonElement(this)
    }catch (e: SerializationException) {
        val time = currentTimeSecond.formatTime("yyyy-MM-dd")

        val md5 = e.message?.md5()
        val fileName = "$time-$md5.json"
        val file = File(System.getProperty("user.dir"), fileName)
        if (!file.exists()) {
            file.writeText(e.stackTraceToString())
            file.appendText("\n\n\n")
            file.appendText(json.encodeToString(JsonElement.serializer(), this@decode))
        }

        throw SerializationException("Json解析失败，请把 ${file.absolutePath} 文件反馈给开发者\n${e.message}")
    }
}

internal inline fun <reified T : Any, reified R> reflectField() = ReadOnlyProperty<T, R> { thisRef, property ->
    thisRef::class.java.getDeclaredField(property.name).apply { isAccessible = true }.get(thisRef) as R
}

internal inline fun <reified R> Any.reflectMethod(method: String, vararg args: Any?): R =
    this::class.java.getDeclaredMethod(method).invoke(this, args) as R

public val currentTimeMillis: Long
    get() = System.currentTimeMillis()

public val currentTimeSecond: Long
    get() = System.currentTimeMillis() / 1000


public val Long.formatTime: String
    get() = formatTime()

public fun Long.formatTime(template: String = "yyyy年MM月dd日 HH:mm:ss"): String = DateTimeFormatter.ofPattern(template)
    .format(LocalDateTime.ofEpochSecond(this, 0, OffsetDateTime.now().offset))

public fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return md.digest(toByteArray()).joinToString("") { "%02x".format(it) }
}

/**
 * 查找对象中所有 LazyImage 类型的属性
 */
public fun forEachLazyImageFields(obj: Any, block: LazyImage.(ImageType) -> Unit) {
    obj::class.declaredMemberProperties.forEach { property ->
        val type = property.returnType.jvmErasure
        if (type == LazyImage::class) {
            (property.apply { isAccessible = true }.getter.call(obj) as LazyImage)
                .block(property.findAnnotation<ImgType>()?.type?:ImageType.UNKNOWN)
        } else if (type == List::class) {
            val list = property.apply { isAccessible = true }.getter.call(obj) as List<*>
            if (list.isNotEmpty()) {
                if (list.first()!!::class == LazyImage::class) {
                    list.forEach {
                        (it as LazyImage).block(property.findAnnotation<ImgType>()?.type?:ImageType.UNKNOWN)
                    }
                }else if (list.first()!!::class.isData) {
                    list.forEach { forEachLazyImageFields(it!!, block) }
                }
            }
        } else if (type.isData) {
            property.apply { isAccessible = true }.getter.call(obj)?.let {
                forEachLazyImageFields(it, block)
            }
        }
    }
}