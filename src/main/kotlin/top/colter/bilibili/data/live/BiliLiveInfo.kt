package top.colter.bilibili.data.live

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class BiliLiveInfo(
    @SerialName("title")
    val title: String,
    @SerialName("room_id")
    val roomId: Long,
    @SerialName("uid")
    val uid: Long,
    @SerialName("uname")
    val uname: String,
    @SerialName("face")
    val face: String,
    @SerialName("cover_from_user")
    val cover: String,
    @SerialName("liveTime")
    val liveTimeStart: Long? = null,
    @SerialName("live_time")
    val liveTimeDuration: Long,
    @SerialName("live_status")
    val liveStatus: Int,
    @SerialName("area_v2_name")
    val area: String,
){
    val liveTime: Long get() = liveTimeStart ?: liveTimeDuration
}