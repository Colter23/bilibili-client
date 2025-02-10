package top.colter.bilibili.data.dynamic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.dynamic.content.DynamicAdditional
import top.colter.bilibili.data.dynamic.content.DynamicDesc
import top.colter.bilibili.data.dynamic.content.DynamicMajor
import top.colter.bilibili.data.dynamic.content.DynamicTopic
import top.colter.bilibili.data.dynamic.module.ModuleAuthor
import top.colter.bilibili.data.dynamic.type.BiliDynamicType
import top.colter.bilibili.data.dynamic.type.OriginDynamicType
import top.colter.bilibili.data.dynamic.type.toBiliDynamicType
import top.colter.bilibili.tools.formatTime

/**
 * 动态项
 *
 * 动态层级 [BiliDynamic] -> [BiliDynamicModules] -> module -> content -> additional/major
 *
 * // @param originType 原动态类型 [OriginDynamicType]
 * @param type 动态类型 [BiliDynamicType]
 * @param idStr ID字符串
 * @param visible 是否显示当前动态(相关动态/合作视频)
 * @param basic 评论相关
 * @param modules 动态模块
 * @param origin 转发的源动态
 */
@Serializable
public data class BiliDynamic(

    @SerialName("type")
    val originType: OriginDynamicType,

    @SerialName("id_str")
    private val idStr: String?,

    @SerialName("visible")
    val visible: Boolean = true,

    @SerialName("modules")
    val modules: BiliDynamicModules,

    @SerialName("basic")
    val basic: DynamicBasic? = null,

    @SerialName("orig")
    val origin: BiliDynamic? = null

) {

    val id: Long
        get() = idStr?.toLong() ?: 0L

    val mid: Long
        get() = author.mid

    val name: String
        get() = author.name

    val type: BiliDynamicType
        get() = originType.toBiliDynamicType()

    val time: Long
        get() = (id shr 32) + 1498838400L

    val formatTime: String
        get() = time.formatTime

}

public val BiliDynamic.author: ModuleAuthor
    get() = modules.author

public val BiliDynamic.content: DynamicDesc?
    get() = modules.dynamic.desc

public val BiliDynamic.major: DynamicMajor?
    get() = modules.dynamic.major

public val BiliDynamic.additional: DynamicAdditional?
    get() = modules.dynamic.additional

public val BiliDynamic.topic: DynamicTopic?
    get() = modules.dynamic.topic


/**
 * 评论相关
 */
@Serializable
public data class DynamicBasic(
    @SerialName("comment_id_str")
    val commentIdStr: String,
    @SerialName("comment_type")
    val commentType: Int,
    @SerialName("rid_str")
    val ridStr: String,
)