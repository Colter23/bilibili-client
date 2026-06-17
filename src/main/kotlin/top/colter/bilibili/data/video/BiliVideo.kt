package top.colter.bilibili.data.video

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl
import top.colter.bilibili.data.user.BaseUser

@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class BiliVideo (
    val bvid: String,
    val aid: Long,
    val videos: Int,
    @SerialName("pic")
    @JsonNames("card_url")
    @ImgType(ImageType.COVER)
    val cover: ImageUrl,
    val title: String,
    @SerialName("pubdate")
    val pubTime: Long,
    @SerialName("ctime")
    val uploadTime: Long,
    val desc: String,
    val duration: Int,
    val owner: BaseUser,
    val dynamic: String? = null,
    val cid: Long,
    val stat: BiliVideoStat? = null,
    val pages: List<BiliVideoPage> = emptyList()
)

@Serializable
public data class BiliVideoStat (
    val view: Int,
    val like: Int,
    val danmaku: Int,
    val reply: Int,
    val favorite: Int,
    val coin: Int,
    val share: Int
)

@Serializable
public data class BiliVideoPage (
    val cid: Long,
    val page: Int,
    @SerialName("part")
    val title: String,
    val duration: Int,
    @SerialName("ctime")
    val uploadTime: Long
)
