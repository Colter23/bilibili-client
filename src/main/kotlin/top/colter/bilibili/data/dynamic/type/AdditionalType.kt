package top.colter.bilibili.data.dynamic.type

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.awt.SystemColor.info

/**
 * 附加类型
 *
 * [COMMON] ADDITIONAL_TYPE_COMMON   活动
 *
 * [RESERVE] ADDITIONAL_TYPE_RESERVE  预约
 *
 * [VOTE] ADDITIONAL_TYPE_VOTE     投票
 *
 * [UGC] ADDITIONAL_TYPE_UGC      相关视频
 *
 * [GOODS] ADDITIONAL_TYPE_GOODS    商品
 *
 * [LOTTERY] ADDITIONAL_TYPE_UPOWER_LOTTERY  充电抽奖
 *
 * [UNKNOWN] 未知类型
 */
@Serializable(AdditionalTypeSerializer::class)
public enum class AdditionalType(public var info: String = "") {
    COMMON,
    RESERVE,
    VOTE,
    UGC,
    GOODS,
    LOTTERY,
    UNKNOWN
}

public object AdditionalTypeSerializer: KSerializer<AdditionalType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(AdditionalType::class.qualifiedName!!, PrimitiveKind.STRING)
    private const val prefix = "ADDITIONAL_TYPE_"
    override fun deserialize(decoder: Decoder): AdditionalType {
        val typeStr = decoder.decodeString().removePrefix(prefix)
        return try {
            AdditionalType.valueOf(typeStr)
        }catch (e: IllegalArgumentException) {
            AdditionalType.UNKNOWN.apply { info = prefix + typeStr }
        }
    }
    override fun serialize(encoder: Encoder, value: AdditionalType) {
        encoder.encodeString(prefix + value.name)
    }
}