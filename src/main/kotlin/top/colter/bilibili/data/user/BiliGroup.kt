package top.colter.bilibili.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 分组
 * @param tid 分组ID
 * @param name 分组名
 * @param count 分组内数量
 * @param tip 分组描述
 */
@Serializable
public data class BiliGroup(
    @SerialName("tagid")
    val tid: Long,
    val name: String,
    val count: Int,
    val tip: String? = null
)