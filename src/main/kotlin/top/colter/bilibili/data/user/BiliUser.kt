package top.colter.bilibili.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.LazyImage

public interface BiliUser: User{
    override val mid: Long
    override val name: String
    override val face: LazyImage
    public val pendant: Pendant?
    public val official: OfficialVerify?
    public val vip: BiliVip?
}


@Serializable
public data class BiliUserInfo(
    override val mid: Long,
    override val name: String,
    override val face: LazyImage,
    override val pendant: Pendant? = null,
    override val official: OfficialVerify? = null,
    override val vip: BiliVip?,

    @SerialName("top_photo_v2")
    val header: BiliUserHeader,
): BiliUser

@Serializable
public data class BiliUserHeader(
    @SerialName("l_img")
    val url: String
)

@Serializable
public data class BiliUserNav(
    override val mid: Long,
    @SerialName("uname")
    override val name: String,
    override val face: LazyImage,
    override val official: OfficialVerify? = null,
    override val pendant: Pendant? = null,
    override val vip: BiliVip? = null
): BiliUser


@Serializable
public data class BiliUserInfoCard(
    val card: BiliUser,
    @SerialName("space")
    val header: BiliUserHeader,
)