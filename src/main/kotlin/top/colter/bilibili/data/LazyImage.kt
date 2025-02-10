package top.colter.bilibili.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(LazyImageSerializer::class)
public data class LazyImage(val url: String) {
    var image: ByteArray? = null
}

public object LazyImageSerializer: KSerializer<LazyImage> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(LazyImage::class.qualifiedName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LazyImage {
        return LazyImage(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: LazyImage) {
        encoder.encodeString(value.url)
    }
}