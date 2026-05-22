package top.colter.bilibili.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param mid 用户ID
 * @param attribute 用户关系 0(未关注) 2(已关注) 6(已互粉) 128(已拉黑)
 * @param mtime 关注时间
 * @param tag 分组id
 * @param special 特别关注标志 0(否) 1(是)
 */
@Serializable
public data class BiliRelation(
    @SerialName("mid")
    val mid: Long,
    @SerialName("attribute")
    val attribute: Int,
    @SerialName("mtime")
    val mtime: Long,
    @SerialName("tag")
    val tag: List<Int>?,
    @SerialName("special")
    val special: Int
)
