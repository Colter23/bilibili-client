package top.colter.bilibili.data.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl
import top.colter.bilibili.data.user.OfficialVerify
import top.colter.bilibili.data.user.User

/**
 * ## 搜索结果响应
 */
@Serializable
public data class SearchResponse<T>(
    @SerialName("seid")
    val searchId: String,
    val page: Int,
    @SerialName("pagesize")
    val pageSize: Int,
    @SerialName("numResults")
    val totalResults: Int,
    @SerialName("numPages")
    val totalPages: Int,
    val result: T
)

/**
 * ## 视频搜索结果
 */
@Serializable
public data class VideoSearchResult(
    val type: String,
    val aid: Long,
    val bvid: String,
    val title: String,
    val description: String,
    @ImgType(ImageType.COVER)
    val pic: ImageUrl,
    val author: String,
    val mid: Long,
    @SerialName("typeid")
    val typeId: String,
    @SerialName("typename")
    val typeName: String,
    val play: Long,
    @SerialName("video_review")
    val danmaku: Long,
    val favorites: Long,
    val review: Long,
    @SerialName("pubdate")
    val pubDate: Long,
    val duration: String,
    val tag: String? = null
) {
    /** 纯文本标题（移除高亮标签） */
    public val plainTitle: String
        get() = title.replace(Regex("<em[^>]*>|</em>"), "")
}

/**
 * ## 用户搜索结果
 */
@Serializable
public data class UserSearchResult(
    val type: String,
    override val mid: Long,
    @SerialName("uname")
    override val name: String,
    @SerialName("upic")
    @ImgType(ImageType.USER)
    override val face: ImageUrl,
    @SerialName("usign")
    val sign: String? = null,
    val fans: Long,
    val videos: Int,
    val level: Int,
    val gender: Int,
    @SerialName("is_upuser")
    val isUpUser: Int,
    @SerialName("is_live")
    val isLive: Int,
    @SerialName("room_id")
    val roomId: Long? = null,
    @SerialName("official_verify")
    val official: OfficialVerify? = null
) : User
