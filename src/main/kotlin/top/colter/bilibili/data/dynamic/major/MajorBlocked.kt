package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl


/**
 * 锁定内容
 *
 * https://t.bilibili.com/767238492501573669 包月充电解锁
 *
 * @param blockedType   隐藏类型
 * @param bgImg       背景图片
 * @param hintMessage 提示信息
 */
@Serializable
public data class MajorBlocked(
    @SerialName("blocked_type")
    val blockedType: Int,
    @SerialName("bg_img")
    val bgImg: Img,
    @SerialName("hint_message")
    val hintMessage: String,
    @SerialName("icon")
    val icon: Img
) {
    /**
     * 图片组合
     * 包含夜间、日间图片
     * @param imgDark  夜间图片
     * @param imgDay   日间图片
     */
    @Serializable
    public data class Img(
        @SerialName("img_dark")
        @ImgType(ImageType.OTHER)
        val imgDark: ImageUrl,
        @SerialName("img_day")
        @ImgType(ImageType.OTHER)
        val imgDay: ImageUrl,
    )
}
