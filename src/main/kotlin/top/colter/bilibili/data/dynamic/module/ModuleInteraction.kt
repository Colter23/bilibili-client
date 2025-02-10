package top.colter.bilibili.data.dynamic.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.dynamic.module.ModuleDynamic

/**
 * 动态评论
 * @param items 评论
 */
@Serializable
public data class ModuleInteraction(
    @SerialName("items")
    val items: List<ModuleDynamic>,
)