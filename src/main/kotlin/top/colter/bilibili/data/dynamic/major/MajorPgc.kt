package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage
import top.colter.bilibili.data.dynamic.general.Badge
import top.colter.bilibili.data.dynamic.general.Stats


/**
 * 番剧/电影
 * https://t.bilibili.com/649951005934354482 电影
 * https://t.bilibili.com/649955687456047124 番剧
 * https://t.bilibili.com/649948476201762833 国创
 * https://t.bilibili.com/611864781908739389 纪录片
 * @param type 类型 目前观测到只有 2
 * @param subType type为2时: 番剧(1) 电影(2) 纪录片(3) 国创(4)
 * @param epid 番剧ID
 * @param seasonId
 * @param title 标题
 * @param cover 封面
 * @param jumpUrl 跳转链接
 * @param stats 统计
 * @param badge 徽章
 */
@Serializable
public data class MajorPgc(
    @SerialName("type")
    val type: Int,
    @SerialName("sub_type")
    val subType: Int,
    @SerialName("epid")
    val epid: Int,
    @SerialName("season_id")
    val seasonId: Int,
    @SerialName("title")
    val title: String,
    @SerialName("cover")
    @ImgType(ImageType.COVER)
    val cover: LazyImage,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("stat")
    val stats: Stats,
    @SerialName("badge")
    val badge: Badge,
)