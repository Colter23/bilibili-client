package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.client.postResult
import top.colter.bilibili.data.user.BiliRelation


/////////////////////////////////////////////
//                 关注相关                 //
/////////////////////////////////////////////

/**
 * ## 查询用户与自己关系
 *
 * **Method:** GET
 *
 * **Authorization:** Cookie(SESSDATA)
 *
 * **Params:** fid=用户ID
 *
 * **Response:** [BiliCommonResult] / [BiliRelation]
 *
 * **Example:** https://api.bilibili.com/x/relation?fid=487550002
 */
public const val RELATION: String = "$BASE_API/x/relation"


/**
 * ## 批量查询用户与自己关系
 *
 * **Method:** GET
 *
 * **Authorization:** Cookie(SESSDATA)
 *
 * **Params:** fids=用户ID,用户ID
 *
 * **Response:** [BiliCommonResult] / [Map<Long,BiliRelation>]
 *
 * **Example:** https://api.bilibili.com/x/relation/relations?fids=258150656,686127
 */
public const val RELATIONS: String = "$RELATION/relations"

/**
 * ## 操作用户关系
 *
 * **Method:** POST
 *
 * **Authorization:** Cookie(SESSDATA) 和 CSRF Token
 *
 * **Body:** fid=用户ID&act=1(1: 关注 | 2: 取关 | 3: 悄悄关注 | 4: 取消悄悄关注 | 5: 拉黑 | 6: 取消拉黑 | 7: 踢出粉丝)&re_src=11&csrf=xxxx
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/x/relation/modify?fid=487550002&act=1&re_src=11&csrf=xxxx
 */
public const val FOLLOW_MODIFY: String = "$BASE_API/x/relation/modify"


/**
 * ## 批量操作用户关系
 *
 * **Method:** POST
 *
 * **Authorization:** Cookie(SESSDATA) 和 CSRF Token
 *
 * **Body:** fids=用户ID,用户ID&act=1(1: 关注 | 5: 拉黑)&re_src=11&csrf=xxxx
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/x/relation/modify?fid=487550002&act=1&re_src=11&csrf=xxxx
 */
public const val FOLLOW_BATCH_MODIFY: String = "$BASE_API/x/relation/batch/modify"


public enum class FollowAction(public val value: Int) {
    FOLLOW(1),
    UNFOLLOW(2),
    QUIET_FOLLOW(3),
    CANCEL_QUIET_FOLLOW(4),
    BLACKLIST(5),
    CANCEL_BLACKLIST(6),
    REMOVE_FAN(7),
}

/**
 * ## 查询用户与自己关系
 *
 * @param mid 用户ID
 *
 * @see RELATION
 */
public suspend fun BiliClient.relation(mid: Long): BiliRelation {
    return getData(RELATION) {
        parameter("fid", mid)
    }
}

/**
 * ## 批量查询用户与自己关系
 *
 * @param mids 用户ID列表
 *
 * @see RELATIONS
 */
public suspend fun BiliClient.relations(mids: Iterable<Long>): Map<Long, BiliRelation> {
    val midList = mids.toList()
    require(midList.isNotEmpty()) { "mids 不能为空" }
    return getData(RELATIONS) {
        parameter("fids", midList.joinIds())
    }
}


/**
 * ## 操作用户关系
 *
 * @param mid 用户ID
 * @param action 关系操作
 * @param csrf CSRF Token；为空时从 Cookie 中读取 bili_jct
 *
 * @see FOLLOW_MODIFY
 */
public suspend fun BiliClient.modifyFollow(
    mid: Long,
    action: FollowAction,
    csrf: String? = null,
): BiliCommonResult {
    val token = requireCsrf(csrf)
    return postResult(FOLLOW_MODIFY) {
        setBody(formBody {
            append("fid", mid.toString())
            append("act", action.value.toString())
            append("re_src", "11")
            append("csrf", token)
        })
    }
}


/**
 * ## 批量操作用户关系
 *
 * @param mids 用户ID列表
 * @param action 关系操作, 只能关注或拉黑
 * @param csrf CSRF Token；为空时从 Cookie 中读取 bili_jct
 *
 * @see FOLLOW_BATCH_MODIFY
 */
public suspend fun BiliClient.modifyFollowBatch(
    mids: Iterable<Long>,
    action: FollowAction,
    csrf: String? = null,
): BiliCommonResult {
    val midList = mids.toList()
    require(midList.isNotEmpty()) { "mids 不能为空" }
    require(action == FollowAction.FOLLOW || action == FollowAction.BLACKLIST) { "批量操作用户关系仅支持关注或拉黑" }

    val token = requireCsrf(csrf)
    return postResult(FOLLOW_BATCH_MODIFY) {
        setBody(formBody {
            append("fids", midList.joinIds())
            append("act", action.value.toString())
            append("re_src", "11")
            append("csrf", token)
        })
    }
}


/**
 * ## 关注用户
 *
 * @param mid 用户ID
 * @param csrf CSRF Token；为空时从 Cookie 中读取 bili_jct
 *
 * @see FOLLOW_MODIFY
 */
public suspend fun BiliClient.follow(mid: Long, csrf: String? = null): BiliCommonResult {
    return modifyFollow(mid, FollowAction.FOLLOW, csrf)
}

/**
 * ## 取消关注用户
 *
 * @param mid 用户ID
 * @param csrf CSRF Token；为空时从 Cookie 中读取 bili_jct
 *
 * @see FOLLOW_MODIFY
 */
public suspend fun BiliClient.unfollow(mid: Long, csrf: String? = null): BiliCommonResult {
    return modifyFollow(mid, FollowAction.UNFOLLOW, csrf)
}
