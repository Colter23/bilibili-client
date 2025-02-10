package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 空
 * @param tips 提示
 */
@Serializable
public data class MajorNone(
    @SerialName("tips")
    val tips: String? = null,
)