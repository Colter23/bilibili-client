package top.colter.bilibili.data.dynamic.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 动态统计
 * @param comment 评论
 * @param forward 转发
 * @param like 点赞
 */
@Serializable
public data class ModuleStats(
    @SerialName("comment")
    val comment: Stats,
    @SerialName("forward")
    val forward: Stats,
    @SerialName("like")
    val like: Stats,
) {
    /**
     * 统计
     * @param text 数量文字
     * @param count 数量
     * @param forbidden
     * @param status
     */
    @Serializable
    public data class Stats(
        @SerialName("text")
        val text: String? = null,
        @SerialName("count")
        val count: Int,
        @SerialName("forbidden")
        val forbidden: Boolean,
        @SerialName("status")
        val status: Boolean? = null,
    )
}