package top.colter.bilibili.data.season

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl
import top.colter.bilibili.data.dynamic.general.Stats

@Serializable
public data class Season (
    val ssid: Int,
    val mdid: Int,
    val title: String,
    @ImgType(ImageType.COVER)
    val cover: ImageUrl,
    val type: SeasonType,
    val stats: Stats
)

@Serializable(SeasonTypeSerializer::class)
public enum class SeasonType(public val value: Int, public val text: String) {
    BANGUMI(1, "番剧"), //ANIME
    MOVIE(2, "电影"),
    DOCUMENTARY(3, "纪录片"),
    CHINA(4, "国创"),
    TELEPLAY(5, "电视剧"),
    VARIETY(7, "综艺"),
}

public object SeasonTypeSerializer: KSerializer<SeasonType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(SeasonType::class.qualifiedName!!, PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): SeasonType =
        SeasonType.values().find { it.value == decoder.decodeInt() } ?: throw Exception("番剧类型错误: ${decoder.decodeString()}")
    override fun serialize(encoder: Encoder, value: SeasonType): Unit = encoder.encodeInt(value.value)
}

