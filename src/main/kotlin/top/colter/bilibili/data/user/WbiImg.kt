package top.colter.bilibili.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class WbiImg(
    @SerialName("img_url")
    val imgUrl: String,
    @SerialName("sub_url")
    val subUrl: String
)