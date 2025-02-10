package top.colter.bilibili.data.dynamic.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.dynamic.content.DynamicAdditional
import top.colter.bilibili.data.dynamic.content.DynamicDesc
import top.colter.bilibili.data.dynamic.content.DynamicMajor
import top.colter.bilibili.data.dynamic.content.DynamicTopic


/**
 * 动态内容
 * @param additional 附加卡片
 * @param desc 动态文字列表
 * @param major 动态内容卡片
 * @param topic 话题
 */
@Serializable
public data class ModuleDynamic(
    @SerialName("additional")
    val additional: DynamicAdditional? = null,
    @SerialName("desc")
    val desc: DynamicDesc? = null,
    @SerialName("major")
    val major: DynamicMajor? = null,
    @SerialName("topic")
    val topic: DynamicTopic? = null,
)