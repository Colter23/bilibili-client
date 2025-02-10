package top.colter.bilibili.data.dynamic.additional

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.dynamic.general.Button
import top.colter.bilibili.data.dynamic.general.Desc


/**
 * 充电抽奖
 * https://t.bilibili.com/729548651221745712
 */
@Serializable
public data class AdditionalLottery(
    @SerialName("rid")
    val rid: Long,
    @SerialName("title")
    val title: String,
    @SerialName("up_mid")
    val mid: Long,
    @SerialName("state")
    val state: Int,
    @SerialName("is_upower_active")
    val active: Boolean,
    @SerialName("desc")
    val desc: Desc,
    @SerialName("button")
    val button: Button,
    @SerialName("jump_url")
    val jumpUrl: String,
)