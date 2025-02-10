package top.colter.bilibili.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import top.colter.bilibili.exception.BiliAuthException
import top.colter.bilibili.exception.BiliBanException
import top.colter.bilibili.exception.BiliException
import top.colter.bilibili.exception.BiliRequestException

public interface BiliBaseResult: BaseResult {
    override fun handleStatus() {
        when (code) {
            0 -> {}
            -412 -> throw BiliBanException("CODE: $code; MESSAGE: $message")
            in -115..-1 -> throw BiliAuthException("CODE: $code; MESSAGE: $message")
            in -701 .. -304 ->  throw BiliRequestException("CODE: $code; MESSAGE: $message")
            else -> throw BiliException("CODE: $code; MESSAGE: $message")
        }
    }
}

@Serializable
public data class BiliCommonResult(
    override val code: Int,
    override val message: String,
    @SerialName("data")
    val dataResult: JsonElement? = null,
    val result: JsonElement? = null
): BiliBaseResult {
    override val data: JsonElement? get() = dataResult ?: result
}
