package top.colter.bilibili.data.dynamic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 动态列表
 *
 * @param hasMore 是否有更多动态
 * @param offset 动态偏移
 * @param updateBaseline
 * @param updateNum 更新数量
 * @param items 动态列表
 */
@Serializable
public data class BiliDynamicList(
    @SerialName("has_more")
    val hasMore: Boolean = false,
    @SerialName("offset")
    val offset: String,
    @SerialName("update_baseline")
    val updateBaseline: String,
    @SerialName("update_num")
    val updateNum: String,

    @SerialName("items")
    val items: List<BiliDynamic>,
)