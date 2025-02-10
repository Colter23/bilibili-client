package top.colter.bilibili.data

import io.ktor.http.*
import io.ktor.util.date.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
public data class EditCookie(
    @SerialName("domain")
    val domain: String,
    @SerialName("expirationDate")
    val expirationDate: Double? = null,
    @SerialName("httpOnly")
    val httpOnly: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("path")
    val path: String,
    @SerialName("secure")
    val secure: Boolean,
    @SerialName("value")
    val value: String
)

public fun EditCookie.toCookie(): Cookie = Cookie(
    name = name,
    value = value,
    encoding = CookieEncoding.RAW,
    expires = expirationDate?.run { GMTDate(times(1000).toLong()) },
    domain = domain,
    path = path,
    secure = secure,
    httpOnly = httpOnly
)

public fun Cookie.toEditCookie(): EditCookie = EditCookie(
    name = name,
    value = value,
    expirationDate = expires?.timestamp?.toDouble()?.div(1000),
    domain = domain.orEmpty(),
    path = path.orEmpty(),
    secure = secure,
    httpOnly = httpOnly,
)