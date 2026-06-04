package top.colter.bilibili.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage

/**
 * 粉丝套装卡片
 * @param id 套装ID
 * @param type 套装类型 图标(1, 2) 专属卡片编号(3)
 * @param name 套装名称
 * @param image 图片
 * @param fan 粉丝卡片
 */
@Serializable
public data class Decorate(
    val id: Long,
    val type: Int,
    val name: String,
    @SerialName("card_url")
    @ImgType(ImageType.USER)
    val image: LazyImage,
    val fan: Fan? = null,
){
    /**
     * 粉丝卡片
     * @param color 颜色
     * @param isFan 是否为粉丝
     * @param numStr 粉丝专属卡片数字字符串
     * @param number 粉丝专属卡片数字
     */
    @Serializable
    public data class Fan(
        @SerialName("color")
        val color: String,
//        @SerialName("is_fan")
//        val isFan: Boolean,
        @SerialName("num_str")
        val numStr: String,
        @SerialName("number")
        val number: Int,
    )
}