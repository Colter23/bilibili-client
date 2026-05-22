package top.colter.bilibili.data.other

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ShortLinkData(
    @SerialName("title")
    val title: String? = null,
    @SerialName("content")
    val content: String? = null,
    @SerialName("link")
    val link: String,
    @SerialName("count")
    val count: Int? = null
)