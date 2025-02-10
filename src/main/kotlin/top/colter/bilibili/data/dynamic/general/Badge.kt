package top.colter.bilibili.data.dynamic.general

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 徽章
 * @param bgColor 背景颜色
 * @param color 颜色
 * @param text 徽章文字
 */
@Serializable
public data class Badge(
    @SerialName("bg_color")
    val bgColor: String,
    @SerialName("color")
    val color: String,
    @SerialName("text")
    val text: String,
)