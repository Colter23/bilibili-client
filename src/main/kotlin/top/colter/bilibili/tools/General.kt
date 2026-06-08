package top.colter.bilibili.tools

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementDescriptors
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.serializer
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImageUrl
import top.colter.bilibili.data.ImgType
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

@OptIn(ExperimentalSerializationApi::class)
public val json: Json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
    allowStructuredMapKeys = true
    coerceInputValues = true
    explicitNulls = false
}

public inline fun <reified T> String.decode(): T = json.parseToJsonElement(this).decode()

public fun <T> String.decode(deserializer: DeserializationStrategy<T>): T =
    json.parseToJsonElement(this).decode(deserializer)

@OptIn(ExperimentalSerializationApi::class)
public inline fun <reified T> JsonElement.decode(): T {
    return decode(serializer<T>())
}

@OptIn(ExperimentalSerializationApi::class)
public fun <T> JsonElement.decode(deserializer: DeserializationStrategy<T>): T {
    return try {
        json.decodeFromJsonElement(deserializer, this)
    } catch (e: SerializationException) {
        val tolerantElement = this.coerceFor(deserializer.descriptor)
        try {
            json.decodeFromJsonElement(deserializer, tolerantElement)
        } catch (fallbackException: SerializationException) {
            val file = writeDecodeErrorFile(fallbackException, this, tolerantElement)
            throw SerializationException(
                "Json解析失败，请把 ${file.absolutePath} 文件反馈给开发者\n${fallbackException.message}",
                fallbackException
            )
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@PublishedApi
internal fun JsonElement.coerceFor(descriptor: SerialDescriptor): JsonElement {
    if (this is JsonNull) {
        return if (descriptor.isNullable) this else descriptor.defaultJsonElement()
    }

    return when (descriptor.kind) {
        PrimitiveKind.BOOLEAN -> JsonPrimitive(jsonPrimitiveOrNull?.booleanOrNull ?: false)
        PrimitiveKind.BYTE -> JsonPrimitive(jsonPrimitiveOrNull?.intOrNull ?: 0)
        PrimitiveKind.CHAR -> JsonPrimitive(jsonPrimitiveOrNull?.contentOrNull?.firstOrNull()?.toString() ?: "")
        PrimitiveKind.DOUBLE -> JsonPrimitive(jsonPrimitiveOrNull?.doubleOrNull ?: 0.0)
        PrimitiveKind.FLOAT -> JsonPrimitive(jsonPrimitiveOrNull?.floatOrNull ?: 0f)
        PrimitiveKind.INT -> JsonPrimitive(jsonPrimitiveOrNull?.intOrNull ?: 0)
        PrimitiveKind.LONG -> JsonPrimitive(jsonPrimitiveOrNull?.longOrNull ?: 0L)
        PrimitiveKind.SHORT -> JsonPrimitive(jsonPrimitiveOrNull?.intOrNull ?: 0)
        PrimitiveKind.STRING -> JsonPrimitive(jsonPrimitiveOrNull?.contentOrNull ?: "")
        StructureKind.CLASS, StructureKind.OBJECT -> coerceObjectFor(descriptor)
        StructureKind.LIST -> coerceListFor(descriptor)
        StructureKind.MAP -> coerceMapFor(descriptor)
        SerialKind.ENUM -> coerceEnumFor(descriptor)
        else -> this
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun JsonElement.coerceEnumFor(descriptor: SerialDescriptor): JsonElement {
    val value = jsonPrimitiveOrNull?.contentOrNull
    return if (value != null && value in descriptor.elementNames) {
        JsonPrimitive(value)
    } else {
        descriptor.defaultJsonElement()
    }
}

@OptIn(ExperimentalSerializationApi::class)
private fun JsonElement.coerceObjectFor(descriptor: SerialDescriptor): JsonElement {
    val source = this as? JsonObject ?: return descriptor.defaultJsonElement()
    val result = source.toMutableMap()

    descriptor.elementNames.forEachIndexed { index, name ->
        val childDescriptor = descriptor.getElementDescriptor(index)
        val child = source[name]
        if (child != null) {
            result[name] = child.coerceFor(childDescriptor)
        } else if (!descriptor.isElementOptional(index) && !childDescriptor.isNullable) {
            result[name] = childDescriptor.defaultJsonElement()
        }
    }

    return JsonObject(result)
}

@OptIn(ExperimentalSerializationApi::class)
private fun JsonElement.coerceListFor(descriptor: SerialDescriptor): JsonElement {
    val source = this as? JsonArray ?: return JsonArray(emptyList())
    val itemDescriptor = descriptor.elementDescriptors.firstOrNull() ?: return source
    return JsonArray(source.map { it.coerceFor(itemDescriptor) })
}

@OptIn(ExperimentalSerializationApi::class)
private fun JsonElement.coerceMapFor(descriptor: SerialDescriptor): JsonElement {
    val source = this as? JsonObject ?: return JsonObject(emptyMap())
    val valueDescriptor = descriptor.elementDescriptors.drop(1).firstOrNull() ?: return source
    return JsonObject(source.mapValues { (_, value) -> value.coerceFor(valueDescriptor) })
}

@OptIn(ExperimentalSerializationApi::class)
private fun SerialDescriptor.defaultJsonElement(): JsonElement {
    if (isNullable) return JsonNull

    return when (kind) {
        PrimitiveKind.BOOLEAN -> JsonPrimitive(false)
        PrimitiveKind.BYTE -> JsonPrimitive(0)
        PrimitiveKind.CHAR -> JsonPrimitive("")
        PrimitiveKind.DOUBLE -> JsonPrimitive(0.0)
        PrimitiveKind.FLOAT -> JsonPrimitive(0f)
        PrimitiveKind.INT -> JsonPrimitive(0)
        PrimitiveKind.LONG -> JsonPrimitive(0L)
        PrimitiveKind.SHORT -> JsonPrimitive(0)
        PrimitiveKind.STRING -> JsonPrimitive("")
        StructureKind.LIST -> JsonArray(emptyList())
        StructureKind.MAP -> JsonObject(emptyMap())
        StructureKind.CLASS, StructureKind.OBJECT -> JsonObject(
            elementNames.mapIndexedNotNull { index, name ->
                val childDescriptor = getElementDescriptor(index)
                if (isElementOptional(index) || childDescriptor.isNullable) null
                else name to childDescriptor.defaultJsonElement()
            }.toMap()
        )
        SerialKind.ENUM -> JsonPrimitive(elementNames.firstOrNull() ?: "")
        else -> JsonPrimitive("")
    }
}

private val JsonElement.jsonPrimitiveOrNull: JsonPrimitive?
    get() = this as? JsonPrimitive

@PublishedApi
internal fun writeDecodeErrorFile(
    exception: SerializationException,
    originalElement: JsonElement,
    tolerantElement: JsonElement
): File {
    val time = currentTimeSecond.formatTime("yyyy-MM-dd")
    val md5 = exception.message?.md5()
    val fileName = "$time-$md5.json"
    val file = File(System.getProperty("user.dir"), fileName)
    if (!file.exists()) {
        file.writeText(exception.stackTraceToString())
        file.appendText("\n\n\n原始 JSON:\n")
        file.appendText(json.encodeToString(JsonElement.serializer(), originalElement))
        file.appendText("\n\n\n解析后 JSON:\n")
        file.appendText(json.encodeToString(JsonElement.serializer(), tolerantElement))
    }
    return file
}

internal inline fun <reified T : Any, reified R> reflectField(): ReadOnlyProperty<T, R> =
    ReadOnlyProperty { thisRef, property ->
        thisRef::class.java.getDeclaredField(property.name).apply { isAccessible = true }.get(thisRef) as R
    }

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
 * 查找对象中所有 ImageUrl 类型的属性
 */
public fun forEachImageUrlFields(obj: Any, block: ImageUrl.(ImageType) -> Unit) {
    obj::class.declaredMemberProperties.forEach { property ->
        val type = property.returnType.jvmErasure
        val imageType = property.findAnnotation<ImgType>()?.type ?: ImageType.UNKNOWN
        if (type == ImageUrl::class) {
            (property.apply { isAccessible = true }.getter.call(obj) as? ImageUrl)
                ?.block(imageType)
        } else if (type == List::class) {
            val list = property.apply { isAccessible = true }.getter.call(obj) as List<*>
            list.forEach { item ->
                when (item) {
                    is ImageUrl -> item.block(imageType)
                    null -> {}
                    else -> if (item::class.isData) {
                        forEachImageUrlFields(item, block)
                    }
                }
            }
        } else if (type.isData) {
            property.apply { isAccessible = true }.getter.call(obj)?.let {
                forEachImageUrlFields(it, block)
            }
        }
    }
}

@Deprecated(
    message = "Use forEachImageUrlFields.",
    replaceWith = ReplaceWith("forEachImageUrlFields(obj, block)")
)
public fun forEachLazyImageFields(obj: Any, block: ImageUrl.(ImageType) -> Unit) {
    forEachImageUrlFields(obj, block)
}
