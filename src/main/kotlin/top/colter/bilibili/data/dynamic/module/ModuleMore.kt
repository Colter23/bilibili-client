package top.colter.bilibili.data.dynamic.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 更多选项
 * @param threePointItems
 */
@Serializable
public data class ModuleMore(
    @SerialName("three_point_items")
    val threePointItems: List<ThreePointItem>,
) {
    /**
     * @param type 类型
     * @param label 标签
     * @param modal
     */
    @Serializable
    public data class ThreePointItem(
        /**
         * THREE_POINT_FOLLOWING 取消关注
         * THREE_POINT_REPORT 举报
         * THREE_POINT_DELETE 删除
         */
        @SerialName("type")
        val type: String,
        @SerialName("label")
        val label: String,
        @SerialName("modal")
        val modal: Modal? = null,
    ) {
        /**
         * @param title 标题
         * @param content 内容
         * @param cancel 取消文字
         * @param confirm 确认文字
         */
        @Serializable
        public data class Modal(
            @SerialName("title")
            val title: String,
            @SerialName("content")
            val content: String,
            @SerialName("cancel")
            val cancel: String,
            @SerialName("confirm")
            val confirm: String,
        )
    }
}