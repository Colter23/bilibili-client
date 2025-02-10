package top.colter.bilibili.data.dynamic.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.user.BaseUser


/**
 * 折叠动态
 * @param type
 * @param ids 动态IDS
 * @param statement 折叠描述(展开3条相关动态)
 * @param users
 */
@Serializable
public data class ModuleFold(
    @SerialName("type")
    val type: Int,
    @SerialName("ids")
    val ids: List<String>,
    @SerialName("statement")
    val statement: String,
    @SerialName("users")
    val users: List<BaseUser>,
)