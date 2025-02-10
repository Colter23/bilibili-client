package top.colter.bilibili.data.dynamic.general

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 统计
 * @param danmaku 弹幕数
 * @param play 播放数
 */
@Serializable
public data class Stats(
    @SerialName("danmaku")
    val danmaku: String,
    @SerialName("play")
    val play: String,
)