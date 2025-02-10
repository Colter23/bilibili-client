package top.colter.bilibili.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 勋章
 * @param nid 勋章id
 * @param name 勋章名称
 * @param image 勋章图标
 * @param imageSmall 勋章图标（小）
 * @param level 勋章等级
 * @param condition 获取条件
 */
@Serializable
public data class Nameplate(
    @SerialName("nid")
    val nid: Long,
    @SerialName("name")
    val name: String,
    @SerialName("image")
    val image: String,
    @SerialName("image_small")
    val imageSmall: String,
    @SerialName("level")
    val level: String,
    @SerialName("condition")
    val condition: String
)