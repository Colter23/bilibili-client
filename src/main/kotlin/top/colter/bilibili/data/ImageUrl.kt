package top.colter.bilibili.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(ImageUrlSerializer::class)
public data class ImageUrl(public val url: String) {
    /** 完整 URL（补全协议） */
    public val fullUrl: String
        get() = if (url.startsWith("//")) "https:$url" else url
}

public object ImageUrlSerializer : KSerializer<ImageUrl> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(ImageUrl::class.qualifiedName!!, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): ImageUrl {
        return ImageUrl(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: ImageUrl) {
        encoder.encodeString(value.url)
    }
}
