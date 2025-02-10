package top.colter.bilibili.data.dynamic.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.dynamic.major.*
import top.colter.bilibili.data.dynamic.type.MajorType


/**
 * 动态内容卡片
 * @param type 卡片类型 [MajorType]
 * @param video 视频
 * @param draw 图片
 * @param article 专栏
 * @param opus 新专栏
 * @param music 音乐
 * @param live 直播
 * @param liveRcmd 直播
 * @param pgc 番剧
 * @param common 活动
 * @param ugcSeason 广播剧
 * @param blocked 锁定
 * @param mediaList 媒体列表
 * @param none 空
 */
@Serializable
public data class DynamicMajor(
    @SerialName("type")
    val type: MajorType,

    @SerialName("archive")
    val video: MajorVideo? = null,
    @SerialName("draw")
    val draw: MajorDraw? = null,
    @SerialName("article")
    val article: MajorArticle? = null,
    @SerialName("opus")
    val opus: MajorOpus? = null,
    @SerialName("music")
    val music: MajorMusic? = null,
    @SerialName("live")
    val live: MajorLive? = null,
    @SerialName("live_rcmd")
    val liveRcmd: MajorLiveRcmd? = null,
    @SerialName("pgc")
    val pgc: MajorPgc? = null,
    @SerialName("common")
    val common: MajorCommon? = null,
    @SerialName("ugc_season")
    val ugcSeason: MajorVideo? = null,
    @SerialName("blocked")
    val blocked: MajorBlocked? = null,
    @SerialName("medialist")
    val mediaList: MajorMediaList? = null,
    @SerialName("none")
    val none: MajorNone? = null,
)


