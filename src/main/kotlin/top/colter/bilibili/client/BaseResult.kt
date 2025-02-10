package top.colter.bilibili.client

import kotlinx.serialization.json.JsonElement

public interface BaseResult: StatusCode {
    override val code: Int
    override val message: String
    public val data: JsonElement?
}