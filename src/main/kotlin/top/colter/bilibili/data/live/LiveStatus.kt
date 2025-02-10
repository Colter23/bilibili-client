package top.colter.bilibili.data.live

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(LiveStatusSerializer::class)
public enum class LiveStatus(public val value: Int, public val text: String) {
    CLOSE(0, "未直播"),
    OPEN(1, "直播中"),
    ROUND(2, "轮播中")
}

public object LiveStatusSerializer: KSerializer<LiveStatus> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(LiveStatus::class.qualifiedName!!, PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): LiveStatus {
        val value = decoder.decodeInt()
        return LiveStatus.values().find { it.value == value} ?: throw Exception("直播状态错误: ${decoder.decodeString()}")
    }
    override fun serialize(encoder: Encoder, value: LiveStatus): Unit = encoder.encodeInt(value.value)
}
