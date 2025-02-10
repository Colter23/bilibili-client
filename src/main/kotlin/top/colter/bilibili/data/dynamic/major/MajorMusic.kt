package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage


/**
 * 音乐
 * https://t.bilibili.com/649393566418731042
 * @param id 音频ID
 * @param title 标题
 * @param cover 封面
 * @param label 标签
 * @param jumpUrl 跳转链接
 */
@Serializable
public data class MajorMusic(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("cover")
    @ImgType(ImageType.COVER)
    val cover: LazyImage,
    @SerialName("label")
    val label: String,
    @SerialName("jump_url")
    val jumpUrl: String
)