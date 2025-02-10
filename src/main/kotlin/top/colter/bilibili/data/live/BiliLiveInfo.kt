package top.colter.bilibili.data.live

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.LazyImage

/**
 *
 */
@Serializable
public data class BiliLiveInfo(
    @SerialName("uid")
    override val uid: Long,
    @SerialName("room_id")
    override val roomId: Long,
    @SerialName("short_id")
    val shortId: Long,
    @SerialName("attention")
    val attention: Int,
    @SerialName("online")
    val online: Int,
    @SerialName("live_status")
    override val status: LiveStatus,
    @SerialName("parent_area_id")
    val parentAreaId: Int,
    @SerialName("parent_area_name")
    val parentAreaName: String,
    @SerialName("area_id")
    val areaId: Int,
    @SerialName("area_name")
    val areaName: String,
    @SerialName("background")
    val background: String,
    @SerialName("title")
    override val title: String,
    @SerialName("user_cover")
    override val cover: LazyImage,
    @SerialName("keyframe")
    val keyframe: String,
    @SerialName("live_time")
    val liveTime: String,
    @SerialName("up_session")
    val upSession: String,
) : BiliLive