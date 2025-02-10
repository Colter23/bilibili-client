package top.colter.bilibili.data.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class GeeTestResult(
    @SerialName("validate")
    val validate: String,
    @SerialName("seccode")
    val seccode: String
)
