package top.colter.bilibili.data.dynamic.general

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 描述
 * @param text 文字
 * @param jumpUrl 跳转链接
 * @param style 样式 灰色#99a2aa(0) 蓝色#00a1d6(1)
 * @param visible 是否可见
 */
@Serializable
public data class Desc(
    @SerialName("text")
    val text: String,
    @SerialName("style")
    val style: Int,
    @SerialName("jump_url")
    val jumpUrl: String? = null,
    @SerialName("visible")
    val visible: Boolean? = null,
)