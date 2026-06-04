package top.colter.bilibili.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * VIP
 * @param type vip类型 大会员(1) 年度大会员/十年大会员(2)
 * @param avatarSubscript 角标
 * @param avatarSubscriptUrl 角标链接
 * @param dueDate
 * @param label 标签
 * @param nicknameColor 昵称颜色
 * @param status 状态
 * @param themeType 主题类型
 */
@Serializable
public data class BiliVip(
    @SerialName("type")
    val type: Int? = null,

    @SerialName("avatar_subscript")
    val avatarSubscript: Int? = null,
    @SerialName("avatar_subscript_url")
    val avatarSubscriptUrl: String? = null,
    @SerialName("due_date")
    val dueDate: Long? = null,
    @SerialName("label")
    val label: Label? = null,
    @SerialName("nickname_color")
    val nicknameColor: String? = null,
    @SerialName("status")
    val status: Int? = null,
    @SerialName("theme_type")
    val themeType: Int? = null,
) {
    /**
     * 标签
     * @param bgColor 背景颜色
     * @param bgStyle 背景样式
     * @param borderColor 边框颜色
     * @param labelTheme 标签主题 (annual_vip)
     * @param path
     * @param text 文字 (大会员)
     * @param textColor 文字颜色
     */
    @Serializable
    public data class Label(
        @SerialName("bg_color")
        val bgColor: String? = null,
        @SerialName("bg_style")
        val bgStyle: Int? = null,
        @SerialName("border_color")
        val borderColor: String? = null,
        @SerialName("label_theme")
        val labelTheme: String? = null,
        @SerialName("path")
        val path: String? = null,
        @SerialName("text")
        val text: String? = null,
        @SerialName("text_color")
        val textColor: String? = null,
    )
}
