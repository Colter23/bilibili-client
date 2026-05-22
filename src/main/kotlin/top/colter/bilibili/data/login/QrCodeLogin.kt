package top.colter.bilibili.data.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class QrCodeLoginData(
    @SerialName("url")
    val url: String,
    @SerialName("qrcode_key")
    val qrcodeKey: String,
)

@Serializable
public data class QrCodeLoginResult(
    @SerialName("url")
    val url: String = "",
    @SerialName("refresh_token")
    val refreshToken: String = "",
    @SerialName("timestamp")
    val timestamp: Long = 0L,
    @SerialName("code")
    val code: Int,
    @SerialName("message")
    val message: String,
) {
    public val status: QrCodeLoginStatus
        get() = QrCodeLoginStatus.fromCode(code)
}

public enum class QrCodeLoginStatus(public val code: Int, public val text: String) {
    SUCCESS(0, "扫码登录成功"),
    EXPIRED(86038, "二维码已失效"),
    SCANNED(86090, "二维码已扫码未确认"),
    WAITING(86101, "未扫码"),
    UNKNOWN(Int.MIN_VALUE, "未知状态");

    public companion object {
        public fun fromCode(code: Int): QrCodeLoginStatus = values().find { it.code == code } ?: UNKNOWN
    }
}

