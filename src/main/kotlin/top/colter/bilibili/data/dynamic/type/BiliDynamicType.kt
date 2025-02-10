package top.colter.bilibili.data.dynamic.type

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.awt.SystemColor.info
import java.awt.SystemColor.text

public enum class BiliDynamicType(public val text: String){
//    ALL_DYNAMIC("全部动态"),

    DYNAMIC("动态"),
    FORWARD("转发"),
    VIDEO("视频"),
    ARTICLE("专栏"),
    MUSIC("音乐"),
    PGC("番剧"),

    LIVE("直播"),
//    LIVE_CLOSE("直播结束"),

    NONE("动态被删除"),
    UNKNOWN("未知动态");

    override fun toString(): String {
        return text
    }
}

public fun OriginDynamicType.toBiliDynamicType(): BiliDynamicType {
    return when (this) {
        OriginDynamicType.WORD,
        OriginDynamicType.DRAW,
        OriginDynamicType.COMMON_SQUARE,
        OriginDynamicType.COMMON_VERTICAL -> BiliDynamicType.DYNAMIC
        OriginDynamicType.ARTICLE -> BiliDynamicType.ARTICLE
        OriginDynamicType.FORWARD -> BiliDynamicType.FORWARD
        OriginDynamicType.UGC_SEASON,
        OriginDynamicType.MEDIALIST,
        OriginDynamicType.AV -> BiliDynamicType.VIDEO
        OriginDynamicType.MUSIC -> BiliDynamicType.MUSIC
        OriginDynamicType.LIVE,
        OriginDynamicType.LIVE_RCMD -> BiliDynamicType.LIVE
        OriginDynamicType.PGC -> BiliDynamicType.PGC
        OriginDynamicType.NONE -> BiliDynamicType.NONE
        OriginDynamicType.UNKNOWN -> BiliDynamicType.UNKNOWN
    }
}


/**
 * ## 原动态类型
 *
 * [WORD] DYNAMIC_TYPE_WORD   文字动态
 *
 * [DRAW] DYNAMIC_TYPE_DRAW    图片动态
 *
 * [ARTICLE] DYNAMIC_TYPE_ARTICLE   文章
 *
 * [FORWARD] DYNAMIC_TYPE_FORWARD   转发动态
 *
 * [AV] DYNAMIC_TYPE_AV   视频
 *
 * [MUSIC] DYNAMIC_TYPE_MUSIC    音乐
 *
 * [LIVE] DYNAMIC_TYPE_LIVE    直播
 *
 * [LIVE_RCMD] DYNAMIC_TYPE_LIVE_RCMD   直播
 *
 * [PGC] DYNAMIC_TYPE_PGC   番剧
 *
 * [COMMON_SQUARE] DYNAMIC_TYPE_COMMON_SQUARE  活动
 *
 * [COMMON_VERTICAL] DYNAMIC_TYPE_COMMON_VERTICAL 活动
 *
 * [UGC_SEASON] DYNAMIC_TYPE_UGC_SEASON   合集
 *
 * [MEDIALIST] DYNAMIC_TYPE_MEDIALIST  媒体列表
 *
 * [NONE] DYNAMIC_TYPE_NONE   动态被删除
 *
 * [UNKNOWN] DYNAMIC_TYPE_UNKNOWN  未知
 */
@Serializable(OriginDynamicTypeSerializer::class)
public enum class OriginDynamicType(public val text: String, public var info: String = "") {
    WORD("动态"),
    DRAW("动态"),
    ARTICLE("专栏"),
    FORWARD("转发动态"),
    AV("视频"),
    MUSIC("音乐"),
    LIVE("直播"),
    LIVE_RCMD("直播"),
    PGC("番剧"),
    COMMON_SQUARE("活动"),
    COMMON_VERTICAL("活动"),
    UGC_SEASON("合集"),
    MEDIALIST("媒体列表"),
    NONE("动态被删除"),
    UNKNOWN("未知类型"),
}

/**
 * TODO("通用自定义枚举序列化器")
 */
public object OriginDynamicTypeSerializer: KSerializer<OriginDynamicType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(OriginDynamicType::class.qualifiedName!!, PrimitiveKind.STRING)
    private const val prefix = "DYNAMIC_TYPE_"
    override fun deserialize(decoder: Decoder): OriginDynamicType {
        val typeStr = decoder.decodeString().removePrefix(prefix)
        return try {
            OriginDynamicType.valueOf(typeStr)
        }catch (e: IllegalArgumentException) {
            OriginDynamicType.UNKNOWN.apply { info = prefix + typeStr }
        }
    }
    override fun serialize(encoder: Encoder, value: OriginDynamicType) {
        encoder.encodeString(prefix + value.name)
    }
}
