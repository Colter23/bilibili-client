package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage


/**
 * 图片
 * @param id 图片ID
 * @param images 图片列表
 */
@Serializable
public data class MajorDraw(
    @SerialName("id")
    val id: Long,
    @SerialName("items")
    val images: List<MajorDrawItem>,
)

/**
 * 图片项
 * @param width 宽度
 * @param height 高度
 * @param size 文件大小
 * @param src 链接
 * @param tags TAG
 */
@Serializable
public data class MajorDrawItem(
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int,
    @SerialName("size")
    val size: Float,
    @SerialName("src")
    @ImgType(ImageType.IMAGES)
    val src: LazyImage,
    @SerialName("tags")
    val tags: List<MajorDrawTag>? = null,
)

/**
 * TAG
 * https://t.bilibili.com/649677042850201602
 * @param mid 用户ID
 * @param tid TAGID
 * @param itemId
 * @param source
 * @param type
 * @param text 文字
 * @param jumpUrl 跳转链接
 * @param schemaUrl
 * @param poi
 * @param orientation
 * @param x
 * @param y
 */
@Serializable
public data class MajorDrawTag(
    @SerialName("tid")
    val tid: Long,
    @SerialName("mid")
    val mid: Long = 0,
    @SerialName("item_id")
    val itemId: Int = 0,
    @SerialName("source")
    val source: Int = 0,
    @SerialName("type")
    val type: Int,
    @SerialName("text")
    val text: String,
    @SerialName("jump_url")
    val jumpUrl: String = "",
    @SerialName("schema_url")
    val schemaUrl: String = "",
    @SerialName("poi")
    val poi: String = "",
    @SerialName("orientation")
    val orientation: Int,
    @SerialName("x")
    val x: Int,
    @SerialName("y")
    val y: Int,
)