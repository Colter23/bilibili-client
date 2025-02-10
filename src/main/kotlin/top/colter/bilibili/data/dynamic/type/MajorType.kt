package top.colter.bilibili.data.dynamic.type

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 *
 * [ARCHIVE] MAJOR_TYPE_ARCHIVE    视频
 *
 * [DRAW] MAJOR_TYPE_DRAW       图片
 *
 * [ARTICLE] MAJOR_TYPE_ARTICLE    专栏
 *
 * [OPUS] MAJOR_TYPE_OPUS      专栏的新样式
 *
 * [MUSIC] MAJOR_TYPE_MUSIC      音乐
 *
 * [LIVE] MAJOR_TYPE_LIVE       直播
 *
 * [LIVE_RCMD] MAJOR_TYPE_LIVE_RCMD  直播
 *
 * [PGC] MAJOR_TYPE_PGC        番剧
 *
 * [COMMON] MAJOR_TYPE_COMMON     活动
 *
 * [UGC_SEASON] MAJOR_TYPE_UGC_SEASON  广播剧
 *
 * [BLOCKED] MAJOR_TYPE_BLOCKED    锁定 (包月充电)
 *
 * [MEDIALIST] MAJOR_TYPE_MEDIALIST    媒体列表
 *
 * [NONE] MAJOR_TYPE_NONE       空
 *
 * [UNKNOWN] 未知
 */
@Serializable(MajorTypeSerializer::class)
public enum class MajorType(public var info: String = "") {
    ARCHIVE,
    DRAW,
    ARTICLE,
    OPUS,
    MUSIC,
    LIVE,
    LIVE_RCMD,
    PGC,
    COMMON,
    UGC_SEASON,
    BLOCKED,
    MEDIALIST,
    NONE,
    UNKNOWN
}


public object MajorTypeSerializer: KSerializer<MajorType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(MajorType::class.qualifiedName!!, PrimitiveKind.STRING)
    private const val prefix = "MAJOR_TYPE_"
    override fun deserialize(decoder: Decoder): MajorType {
        val typeStr = decoder.decodeString().removePrefix(prefix)
        return try {
            MajorType.valueOf(typeStr)
        }catch (e: IllegalArgumentException) {
            MajorType.UNKNOWN.apply { info = prefix + typeStr }
        }
    }
    override fun serialize(encoder: Encoder, value: MajorType) {
        encoder.encodeString(prefix + value.name)
    }
}
