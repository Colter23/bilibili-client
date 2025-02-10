package top.colter.bilibili.data.dynamic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.dynamic.module.*


/**
 * 动态模块
 * @param author 动态作者
 * @param dynamic 动态内容
 * @param interaction 动态评论
 * @param dispute 动态警告
 * @param fold 折叠动态
 * @param more 更多选项
 * @param stat 动态统计
 * @param tag 动态标签 (置顶)
 */
@Serializable
public data class BiliDynamicModules(
    @SerialName("module_author")
    val author: ModuleAuthor,
    @SerialName("module_dynamic")
    val dynamic: ModuleDynamic,
    @SerialName("module_interaction")
    val interaction: ModuleInteraction? = null,
    @SerialName("module_dispute")
    val dispute: ModuleDispute? = null,
    @SerialName("module_fold")
    val fold: ModuleFold? = null,
    @SerialName("module_more")
    val more: ModuleMore? = null,
    @SerialName("module_stat")
    val stat: ModuleStats? = null,
    @SerialName("module_tag")
    val tag: ModuleTag? = null,
)