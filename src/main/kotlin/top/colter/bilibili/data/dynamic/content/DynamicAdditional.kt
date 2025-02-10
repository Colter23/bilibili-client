package top.colter.bilibili.data.dynamic.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.dynamic.additional.*
import top.colter.bilibili.data.dynamic.type.AdditionalType

/**
 * 附加卡片
 * @param type 卡片类型 [AdditionalType]
 * @param common  活动
 * @param reserve 预约
 * @param vote    投票
 * @param ugc     相关视频
 * @param goods   商品
 * @param lottery 充电抽奖
 */
@Serializable
public data class DynamicAdditional(

    @SerialName("type")
    val type: AdditionalType,

    @SerialName("common")
    val common: AdditionalCommon? = null,
    @SerialName("reserve")
    val reserve: AdditionalReserve? = null,
    @SerialName("vote")
    val vote: AdditionalVote? = null,
    @SerialName("ugc")
    val ugc: AdditionalUgc? = null,
    @SerialName("goods")
    val goods: AdditionalGoods? = null,
    @SerialName("upower_lottery")
    val lottery: AdditionalLottery? = null,
)



