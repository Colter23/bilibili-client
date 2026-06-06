package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl
import top.colter.bilibili.data.dynamic.general.Badge


/**
 * 活动
 * https://t.bilibili.com/451600413724453059 评分
 * https://t.bilibili.com/649769062547587096 装扮
 * https://t.bilibili.com/649677803073044502 活动
 * https://t.bilibili.com/255349133036764369 整部动漫评分
 * @param id
 * @param sketchId
 * @param bizType 类型 评分(0) 活动(1) 无徽章(3) 动漫评分(101) 装扮(231)
 * @param title 标题
 * @param cover 封面
 * @param desc 描述
 * @param label 标签
 * @param jumpUrl 跳转链接
 * @param style 样式
 * @param badge 徽章
 */
@Serializable
public data class MajorCommon(
    @SerialName("id")
    val id: String,
    @SerialName("sketch_id")
    val sketchId: String,
    @SerialName("biz_type")
    val bizType: Int,
    @SerialName("title")
    val title: String,
    @SerialName("cover")
    @ImgType(ImageType.OTHER)
    val cover: ImageUrl,
    @SerialName("desc")
    val desc: String,
    @SerialName("label")
    val label: String = "",
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("style")
    val style: Int,
    @SerialName("badge")
    val badge: Badge,
)