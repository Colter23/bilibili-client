package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl


/**
 * 专栏
 * https://t.bilibili.com/649964827143307286
 * @param id 专栏ID
 * @param title 标题
 * @param label 标签(114阅读)
 * @param description 描述
 * @param jumpUrl 跳转链接
 * @param covers 封面列表
 */
@Serializable
public data class MajorArticle(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("desc")
    val description: String,
    @SerialName("label")
    val label: String,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("covers")
    @ImgType(ImageType.COVER)
    val covers: List<ImageUrl>
)