package top.colter.bilibili.data.dynamic.major

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage
import top.colter.bilibili.data.live.BiliLive
import top.colter.bilibili.data.live.LiveStatus
import top.colter.bilibili.data.live.WatchedShow
import top.colter.bilibili.tools.decode


/**
 * 直播
 * @param content 内容
 * @param reserveType
 */
@Serializable
public data class MajorLiveRcmd(
    @SerialName("content")
    val content: String,
    @SerialName("reserve_type")
    val reserveType: Int,
) {
    val liveInfo: LiveRcmdContent get() = content.decode()
}

@Serializable
public data class LiveRcmdContent(
    @SerialName("type")
    val type: Int,
    @SerialName("live_play_info")
    val livePlayInfo: LivePlayInfo,
)

@Serializable
public data class LivePlayInfo(
    @SerialName("uid")
    override val uid: Long,
    @SerialName("room_id")
    override val roomId: Long,
    @SerialName("live_id")
    val liveId: String,
    @SerialName("live_status")
    override val status: LiveStatus,
    @SerialName("title")
    override val title: String,
    @SerialName("cover")
    @ImgType(ImageType.COVER)
    override val cover: LazyImage,
    @SerialName("parent_area_name")
    val parentAreaName: String,
    @SerialName("parent_area_id")
    val parentAreaId: Int,
    @SerialName("area_name")
    val areaName: String,
    @SerialName("area_id")
    val areaId: Int,
    @SerialName("link")
    val link: String,
    @SerialName("room_type")
    val roomType: Int,
    @SerialName("live_screen_type")
    val liveScreenType: Int,
    @SerialName("live_start_time")
    val liveStartTime: Long,
    @SerialName("play_type")
    val playType: Int,
    @SerialName("online")
    val online: Int,
    @SerialName("room_paid_type")
    val roomPaidType: Int,
    @SerialName("watched_show")
    val watchedShow: WatchedShow,
    //@SerialName("pendants")
    //val pendants: Pendants,
) : BiliLive