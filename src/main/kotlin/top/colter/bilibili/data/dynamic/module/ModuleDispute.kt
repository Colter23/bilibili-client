package top.colter.bilibili.data.dynamic.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 动态警告
 * https://t.bilibili.com/649561422335836211
 * @param title 警告内容
 * @param desc
 * @param jumpUrl
 */
@Serializable
public data class ModuleDispute(
    @SerialName("title")
    val title: String,
    @SerialName("desc")
    val desc: String,
    @SerialName("jump_url")
    val jumpUrl: String,
)