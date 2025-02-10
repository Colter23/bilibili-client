package top.colter.bilibili.data.dynamic.additional

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage


/**
 * 相关视频
 * https://t.bilibili.com/629668894526444564
 * https://t.bilibili.com/649671579655995396
 * @param idStr ID字符串
 * @param title 标题
 * @param cover 封面
 * @param descSecond 第二行描述
 * @param duration 视频长度
 * @param headText 头文字
 * @param jumpUrl 跳转链接
 * @param multiLine
 */
@Serializable
public data class AdditionalUgc(
    @SerialName("id_str")
    val idStr: String,
    @SerialName("title")
    val title: String,
    @SerialName("cover")
    @ImgType(ImageType.COVER)
    val cover: LazyImage,
    @SerialName("desc_second")
    val descSecond: String,
    @SerialName("duration")
    val duration: String,
    @SerialName("head_text")
    val headText: String,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("multi_line")
    val multiLine: Boolean,
)