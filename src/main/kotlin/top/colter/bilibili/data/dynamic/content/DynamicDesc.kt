package top.colter.bilibili.data.dynamic.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage
import top.colter.bilibili.data.dynamic.type.RichTextType


/**
 * 动态文字列表
 * @param richTextNodes 文字节点列表
 * @param text 全部文字
 */
@Serializable
public data class DynamicDesc(
    @SerialName("rich_text_nodes")
    val richTextNodes: List<RichTextNode>,
    @SerialName("text")
    val text: String,
)

/**
 * 文字节点
 * @param type 节点类型
 * @param origText 源文字
 * @param text 显示文字
 * @param rid @用户ID/商品ID...
 * @param jumpUrl 网页链接/主题 跳转链接
 * @param emoji Emoji
 */
@Serializable
public data class RichTextNode(
    @SerialName("type")
    val type: RichTextType,

    @SerialName("orig_text")
    val origText: String,
    @SerialName("text")
    val text: String,

    @SerialName("rid")
    val rid: String? = null,
    @SerialName("jump_url")
    val jumpUrl: String? = null,
    @SerialName("emoji")
    val emoji: Emoji? = null,
) {
    /**
     * Emoji
     * @param type 类型
     * @param iconUrl emoji链接
     * @param size 大小?
     * @param text emoji文字
     */
    @Serializable
    public data class Emoji(
        @SerialName("type")
        val type: Int,

        @SerialName("icon_url")
        @ImgType(ImageType.EMOJI)
        val iconUrl: LazyImage,
        @SerialName("size")
        val size: Int,
        @SerialName("text")
        val text: String,
    )
}

