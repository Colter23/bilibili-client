package top.colter.bilibili.data.dynamic.module

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage
import top.colter.bilibili.data.dynamic.general.IconBadge
import top.colter.bilibili.data.user.*


/**
 * 动态作者
 * @param type 作者类型
 * @param mid 用户ID
 * @param name 用户名
 * @param face 头像
 * @param faceNFT 是否为NFT头像
 * @param pendant 头像挂件
 * @param following 是否关注(未关注为null)
 * @param label 标签
 * @param jumpUrl 跳转URL
 * @param official 官方认证
 * @param pubAction 名称下方的副标题 (投稿了视频/与他人联合创作)
 * @param pubTime 名称下方的时间 (10分钟前)
 * @param pubTs 上传时间戳
 * @param vip VIP (大会员)
 * @param decorate 粉丝套装卡片
 */
@Serializable
public data class ModuleAuthor(
    /**
     * AUTHOR_TYPE_NORMAL
     * AUTHOR_TYPE_PGC    番剧
     */
    @SerialName("type")
    val type: String = "AUTHOR_TYPE_NORMAL",

    @SerialName("mid")
    override val mid: Long,
    @SerialName("name")
    override val name: String,
    @SerialName("face")
    @ImgType(ImageType.USER)
    override val face: LazyImage,
    @SerialName("pub_ts")
    val pubTs: Long = 0,
    @SerialName("pub_time")
    val pubTime: String = "",
    @SerialName("pub_action")
    val pubAction: String = "",
//    @SerialName("face_nft")
//    val faceNFT: Boolean? = null,
//    @SerialName("following")
//    val following: Boolean? = null,
    @SerialName("icon_badge")
    val iconBadge: IconBadge? = null,
    @SerialName("label")
    val label: String = "",
    @SerialName("jump_url")
    val jumpUrl: String? = null,
    @SerialName("vip")
    override val vip: BiliVip? = null,
    @SerialName("official_verify")
    override val official: OfficialVerify? = null,
    @SerialName("pendant")
    override val pendant: Pendant? = null,
    @SerialName("decorate")
    val decorate: Decorate? = null,
): BiliUser