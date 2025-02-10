package top.colter.bilibili.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.LazyImage
import top.colter.bilibili.data.live.BiliLive
import top.colter.bilibili.data.live.LiveStatus
import top.colter.bilibili.data.live.WatchedShow


@Serializable
public data class BiliUserInfo(
    @SerialName("mid")
    override val mid: Long,
    @SerialName("name")
    override val name: String,
    @SerialName("face")
    override val face: LazyImage,
    @SerialName("official")
    override val official: OfficialVerify? = null,
    @SerialName("pendant")
    override val pendant: Pendant? = null,
    @SerialName("decorate")
    override val decorate: Decorate? = null,

    @SerialName("vip")
    val vip: BiliVip? = null,
    @SerialName("sex")
    val sex: String,
    @SerialName("face_nft")
    val faceNft: Int,
    @SerialName("face_nft_type")
    val faceNftType: Int,
    @SerialName("rank")
    val rank: Int,
    @SerialName("level")
    val level: Int,
    @SerialName("moral")
    val moral: Int,
    @SerialName("silence")
    val silence: Int,
    @SerialName("coins")
    val coins: Int,
    @SerialName("is_followed")
    val isFollowed: Boolean,
    @SerialName("top_photo")
    val topPhoto: String,
    @SerialName("birthday")
    val birthday: String,
    @SerialName("is_senior_member")
    val isSeniorMember: Int,
    @SerialName("fans_badge")
    val fansBadge: Boolean,
    @SerialName("fans_medal")
    val fansMedal: FansMedal,
    @SerialName("nameplate")
    val nameplate: Nameplate,
    @SerialName("live_room")
    val liveRoom: LiveRoom,
    @SerialName("school")
    val school: School? = null,
    @SerialName("profession")
    val profession: Profession,
    @SerialName("tags")
    val tags: List<String>? = null,

): BiliUser {

    @Serializable
    public data class School(
        @SerialName("name")
        val name: String,
    )

    @Serializable
    public data class Profession(
        @SerialName("name")
        val name: String,
        @SerialName("department")
        val department: String,
        @SerialName("title")
        val title: String,
        @SerialName("is_show")
        val isShow: Int,
    )

    @Serializable
    public data class LiveRoom(
        @SerialName("roomStatus")
        val roomStatus: Int,
        @SerialName("liveStatus")
        override val status: LiveStatus,
        @SerialName("url")
        val url: String,
        @SerialName("title")
        override val title: String,
        @SerialName("cover")
        override val cover: LazyImage,
        @SerialName("roomid")
        override val roomId: Long,
        @SerialName("roundStatus")
        val roundStatus: Int,
        @SerialName("broadcast_type")
        val broadcastType: Int,
        @SerialName("watched_show")
        val watchedShow: WatchedShow,
    ) : BiliLive {
        override val uid: Long get() = 0
    }

}

@Serializable
public data class FansMedal(
    @SerialName("show")
    val show: Boolean,
    @SerialName("wear")
    val wear: Boolean,
    @SerialName("medal")
    val medal: Medal?,
) {
    @Serializable
    public data class Medal(
        @SerialName("uid")
        val uid: Long,
        @SerialName("target_id")
        val targetId: Long,
        @SerialName("medal_id")
        val medalId: Long,
        @SerialName("level")
        val level: Int,
        @SerialName("medal_name")
        val medalName: String,
        @SerialName("intimacy")
        val intimacy: Int,
        @SerialName("next_intimacy")
        val nextIntimacy: Int,
        @SerialName("day_limit")
        val dayLimit: Int,
        @SerialName("medal_color")
        val medalColor: Int,
        @SerialName("medal_color_start")
        val medalColorStart: Int,
        @SerialName("medal_color_end")
        val medalColorEnd: Int,
        @SerialName("medal_color_border")
        val medalColorBorder: Int,
        @SerialName("is_lighted")
        val isLighted: Int,
        @SerialName("light_status")
        val lightStatus: Int,
        @SerialName("wearing_status")
        val wearingStatus: Int,
        @SerialName("score")
        val score: Int,
    )

}


