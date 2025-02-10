package top.colter.bilibili.data.dynamic.type

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * [TEXT] RICH_TEXT_NODE_TYPE_TEXT     文字
 *
 * [EMOJI] RICH_TEXT_NODE_TYPE_EMOJI    Emoji
 *
 * [AT] RICH_TEXT_NODE_TYPE_AT       @用户
 *
 * [TOPIC] RICH_TEXT_NODE_TYPE_TOPIC    #主题#
 *
 * [WEB] RICH_TEXT_NODE_TYPE_WEB      网页链接
 *
 * [VOTE] RICH_TEXT_NODE_TYPE_VOTE     投票
 *
 * [BV] RICH_TEXT_NODE_TYPE_LOTTERY  互动抽奖
 *
 * [BV] RICH_TEXT_NODE_TYPE_BV          BV号
 *
 * [GOODS] RICH_TEXT_NODE_TYPE_GOODS      商品
 *
 * [UNKNOWN]   未知
 */
@Serializable(RichTextTypeSerializer::class)
public enum class RichTextType(public var info: String = "") {
    TEXT,
    EMOJI,
    AT,
    TOPIC,
    WEB,
    VOTE,
    BV,
    GOODS,
    UNKNOWN
}

public object RichTextTypeSerializer: KSerializer<RichTextType> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(RichTextType::class.qualifiedName!!, PrimitiveKind.STRING)
    private const val prefix = "RICH_TEXT_NODE_TYPE_"
    override fun deserialize(decoder: Decoder): RichTextType {
        val typeStr = decoder.decodeString().removePrefix(prefix)
        return try {
            RichTextType.valueOf(typeStr)
        }catch (e: IllegalArgumentException) {
            RichTextType.UNKNOWN.apply { info = prefix + typeStr }
        }
    }
    override fun serialize(encoder: Encoder, value: RichTextType) {
        encoder.encodeString(prefix + value.name)
    }
}
