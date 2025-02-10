package top.colter.bilibili.data.dynamic.general

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 跳转按钮样式
 * @param text 文字
 * @param iconUrl 图标链接
 */
@Serializable
public data class JumpStyle(
    @SerialName("text")
    val text: String,
    @SerialName("icon_url")
    val iconUrl: String,
)