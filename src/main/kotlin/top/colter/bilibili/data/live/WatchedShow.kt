package top.colter.bilibili.data.live

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class WatchedShow(
    @SerialName("num")
    val num: Int,
    @SerialName("text_small")
    val textSmall: String,
    @SerialName("text_large")
    val textLarge: String,
    @SerialName("icon")
    val icon: String,
    @SerialName("icon_location")
    val iconLocation: String,
    @SerialName("icon_web")
    val iconWeb: String,
    @SerialName("switch")
    val switch: Boolean,
)