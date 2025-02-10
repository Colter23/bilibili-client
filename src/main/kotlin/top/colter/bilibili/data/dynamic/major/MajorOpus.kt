package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage
import top.colter.bilibili.data.dynamic.content.DynamicDesc


/**
 * 新样式 目前观测到只有专栏
 *
 * @param title 标题
 * @param jumpUrl 跳转链接
 * @param summary 总结
 * @param pics 封面
 *
 */
@Serializable
public data class MajorOpus(
    @SerialName("title")
    val title: String,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("summary")
    val summary: DynamicDesc,
    @SerialName("pics")
    val pics: List<MajorOpusPic>
)

/**
 * 图片项
 * @param width 宽度
 * @param height 高度
 * @param size 文件大小
 * @param url 链接
 */
@Serializable
public data class MajorOpusPic(
    @SerialName("width")
    val width: Int,
    @SerialName("height")
    val height: Int,
    @SerialName("size")
    val size: Double,
    @SerialName("url")
    @ImgType(ImageType.COVER)
    val url: LazyImage,
)