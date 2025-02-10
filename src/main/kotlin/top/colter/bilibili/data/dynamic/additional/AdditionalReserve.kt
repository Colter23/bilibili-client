package top.colter.bilibili.data.dynamic.additional

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.dynamic.general.Button
import top.colter.bilibili.data.dynamic.general.Desc


/**
 * 预约
 * https://t.bilibili.com/648581851972108305 直播预约有奖
 * https://t.bilibili.com/649281734082297859 视频首映直播预告
 * @param rid 预约ID
 * @param upMid UP主ID
 * @param title 标题
 * @param reserveTotal 预约人数
 * @param desc1 第一行描述
 * @param desc2 第二行描述
 * @param desc3 第三行描述
 * @param premiere 预约封面?
 * @param state 状态
 * @param stype 预约类型 视频预约(1) 直播预约(2) 视频首映直播预告(4)
 * @param jumpUrl 跳转链接
 * @param button 按钮
 */
@Serializable
public data class AdditionalReserve(
    @SerialName("rid")
    val rid: Long,
    @SerialName("up_mid")
    val upMid: Long,
    @SerialName("title")
    val title: String,
    @SerialName("reserve_total")
    val reserveTotal: Int,
    @SerialName("desc1")
    val desc1: Desc,
    @SerialName("desc2")
    val desc2: Desc,
    @SerialName("desc3")
    val desc3: Desc? = null,
    @SerialName("state")
    val state: Int,
    @SerialName("stype")
    val stype: Int,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("button")
    val button: Button,
)
