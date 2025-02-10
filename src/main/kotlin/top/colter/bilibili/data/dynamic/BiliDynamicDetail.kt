package top.colter.bilibili.data.dynamic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BiliDynamicDetail(
    @SerialName("item")
    val item: BiliDynamic,
    @SerialName("contact")
    val contact: String? = null
)