package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl
import top.colter.bilibili.data.dynamic.general.Badge
import top.colter.bilibili.data.dynamic.general.Stats


/**
 * 视频
 * https://t.bilibili.com/703209180117336130 动态视频
 * https://t.bilibili.com/703118805403959368 合作视频
 * https://t.bilibili.com/688146618021576752 互动视频
 * https://t.bilibili.com/703102853898567699 直播回放
 * @param type 类型
 * @param aid AVID
 * @param bvid BVID
 * @param title 标题
 * @param cover 封面
 * @param description 描述
 * @param duration 视频长度
 * @param jumpUrl 跳转链接
 * @param stats 视频统计
 * @param badge 徽章
 */
@Serializable
public data class MajorVideo(
    @SerialName("type")
    val type: Int,
    @SerialName("aid")
    val aid: Long,
    @SerialName("bvid")
    val bvid: String,
    @SerialName("title")
    val title: String,
    @SerialName("cover")
    @ImgType(ImageType.COVER)
    val cover: ImageUrl,
    @SerialName("desc")
    val description: String,
    @SerialName("duration_text")
    val duration: String,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("stat")
    val stats: Stats,
    @SerialName("badge")
    val badge: Badge,
)