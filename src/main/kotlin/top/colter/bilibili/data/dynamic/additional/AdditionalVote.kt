package top.colter.bilibili.data.dynamic.additional

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * 投票
 * https://t.bilibili.com/649724369116856353
 * https://t.bilibili.com/362189798894110797
 * https://t.bilibili.com/352529506908653008
 * @param uid 用户ID
 * @param voteId 投票ID https://api.vc.bilibili.com/vote_svr/v1/vote_svr/vote_info?vote_id=2434097
 * @param desc 描述
 * @param type 类型
 * @param status 状态 正在进行(1) 结束(4)
 * @param joinNum 参加人数
 * @param endTime 结束时间
 * @param choiceCnt 可选数量
 * @param defaultShare 默认分享到动态
 */
@Serializable
public data class AdditionalVote(
    @SerialName("uid")
    val uid: Long,
    @SerialName("vote_id")
    val voteId: Long,
    @SerialName("desc")
    val desc: String,
    @SerialName("type")
    val type: Int?,
    @SerialName("status")
    val status: Int?,
    @SerialName("join_num")
    val joinNum: Int?,
    @SerialName("end_time")
    val endTime: Long,
    @SerialName("choice_cnt")
    val choiceCnt: Int,
    @SerialName("default_share")
    val defaultShare: Int,
)
