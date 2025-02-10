package top.colter.bilibili.data.dynamic.general

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 徽标图案
 *
 */
@Serializable
public data class IconBadge(
    @SerialName("icon")
    val icon: String = "",
    @SerialName("render_img")
    val renderImg: String = "",
    @SerialName("text")
    val text: String = ""
)