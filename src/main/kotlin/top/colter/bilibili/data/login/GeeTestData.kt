package top.colter.bilibili.data.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GeeTestData(
    @SerialName("gt")
    val gt: String,
    @SerialName("challenge")
    val challenge: String
)
