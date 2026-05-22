package top.colter.bilibili.data.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
public data class LoginCaptcha(
    @SerialName("type")
    val type: String,
    @SerialName("token")
    val token: String,
    @SerialName("geetest")
    val geetest: GeeTestData,
    @SerialName("tencent")
    val tencent: JsonElement? = null,
)

