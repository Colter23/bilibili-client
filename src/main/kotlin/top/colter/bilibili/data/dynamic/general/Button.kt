package top.colter.bilibili.data.dynamic.general

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 按钮
 * @param type 类型 使用jumpStyle内容(1)
 * @param status 状态 正常(1) 已预约/撤销(2)
 * @param jumpUrl 跳转链接
 * @param jumpStyle 样式
 * @param check 按钮点击
 * @param uncheck 按钮未点击
 */
@Serializable
public data class Button(
    @SerialName("type")
    val type: Int,
    @SerialName("status")
    val status: Int? = null,
    @SerialName("jump_url")
    val jumpUrl: String? = null,
    @SerialName("jump_style")
    val jumpStyle: JumpStyle? = null,
    @SerialName("check")
    val check: Check? = null,
    @SerialName("uncheck")
    val uncheck: Check? = null,
) {
    /**
     * 点击
     * @param text 文字
     * @param iconUrl 图标链接
     * @param disable 是否禁用
     * @param toast 按钮提示
     */
    @Serializable
    public data class Check(
        @SerialName("text")
        val text: String,
        @SerialName("icon_url")
        val iconUrl: String?,
        @SerialName("disable")
        val disable: Int? = null,
        @SerialName("toast")
        val toast: String? = null,
    )
}