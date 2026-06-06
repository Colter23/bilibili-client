package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl
import top.colter.bilibili.data.dynamic.general.Badge
import top.colter.bilibili.data.dynamic.general.Stats


/**
 * 媒体列表
 * https://t.bilibili.com/782595959854989317
 * @param id ID
 * @param title 标题
 * @param subTitle 副标题
 * @param cover 封面
 * @param coverType 封面类型
 * @param jumpUrl 跳转链接
 * @param badge 徽章
 */
@Serializable
public data class MajorMediaList(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String,
    @SerialName("sub_title")
    val subTitle: String,
    @SerialName("cover")
    @ImgType(ImageType.COVER)
    val cover: ImageUrl,
    @SerialName("cover_type")
    val coverType: Int,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("badge")
    val badge: Badge,
)