package top.colter.bilibili.data.dynamic.additional

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage
import top.colter.bilibili.data.dynamic.general.Button


/**
 * 活动
 * https://t.bilibili.com/666029864355102737
 * https://t.bilibili.com/665881610678173713
 * @param idStr ID字符串
 * @param title 标题
 * @param cover 封面
 * @param subType 类型 游戏(game) 番剧(ogv) 装扮(decoration) 官方活动(official_activity)
 * @param desc1 第一行描述
 * @param desc2 第二行描述
 * @param headText 头文字描述(相关游戏)
 * @param jumpUrl 跳转链接
 * @param style 样式
 * @param button 按钮
 */
@Serializable
public data class AdditionalCommon(
    @SerialName("id_str")
    val idStr: String,
    @SerialName("title")
    val title: String,
    @SerialName("cover")
    @ImgType(ImageType.OTHER)
    val cover: LazyImage,
    @SerialName("sub_type")
    val subType: String,
    @SerialName("desc1")
    val desc1: String,
    @SerialName("desc2")
    val desc2: String,
    @SerialName("head_text")
    val headText: String,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("style")
    val style: Int,
    @SerialName("button")
    val button: Button,
)