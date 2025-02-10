package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage
import top.colter.bilibili.data.live.LiveStatus
import top.colter.bilibili.data.dynamic.general.Badge
import top.colter.bilibili.data.live.BiliLive


/**
 * 直播
 * https://t.bilibili.com/387009689050325972
 * @param roomId LiveID
 * @param title 标题
 * @param cover 封面
 * @param descFirst 第一个描述
 * @param descSecond 第二个描述
 * @param jumpUrl 跳转链接
 * @param status 直播状态 [LiveStatus]
 * @param reserveType
 * @param badge 徽章
 */
@Serializable
public data class MajorLive(
    @SerialName("id")
    override val roomId: Long,
    @SerialName("title")
    override val title: String,
    @SerialName("cover")
    @ImgType(ImageType.COVER)
    override val cover: LazyImage,
    @SerialName("desc_first")
    val descFirst: String,
    @SerialName("desc_second")
    val descSecond: String,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("live_state")
    override val status: LiveStatus,
    @SerialName("reserve_type")
    val reserveType: Int,
    @SerialName("badge")
    val badge: Badge
) : BiliLive {
    override val uid: Long get() = 0
}

