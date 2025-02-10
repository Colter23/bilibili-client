package top.colter.bilibili.data.dynamic.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 动态标签
 * @param text 标签 (置顶)
 */
@Serializable
public data class ModuleTag(
    @SerialName("text")
    val text: String,
)
