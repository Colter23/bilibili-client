package top.colter.bilibili.data.dynamic.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 主题
 * @param id 主题ID
 * @param name 主题名
 * @param jumpUrl 跳转链接
 */
@Serializable
public data class DynamicTopic(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("jump_url")
    val jumpUrl: String,
)