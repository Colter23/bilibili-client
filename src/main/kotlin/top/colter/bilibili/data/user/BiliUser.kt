package top.colter.bilibili.data.user

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl

public interface BiliUser: User {
    override val mid: Long
    override val name: String
    override val face: ImageUrl
    public val pendant: Pendant?
    public val official: OfficialVerify?
    public val vip: BiliVip?
}


@OptIn(ExperimentalSerializationApi::class)
@Serializable
public data class BiliUserInfo(
    override val mid: Long,
    @JsonNames("uname")
    override val name: String,
    @ImgType(ImageType.USER)
    override val face: ImageUrl,
    override val pendant: Pendant? = null,
    override val official: OfficialVerify? = null,
    override val vip: BiliVip? = null,
    @SerialName("top_photo_v2")
    val header: BiliUserHeader? = null,
): BiliUser


@Serializable
public data class BiliUserInfoCard(
    val card: BiliUserInfo,
    @SerialName("space")
    val header: BiliUserHeader,
)


@Serializable
public data class BiliUserHeader(
    @SerialName("l_img")
    @ImgType(ImageType.USER)
    val image: ImageUrl
) {
    public val img: ImageUrl
        get() = image
}
