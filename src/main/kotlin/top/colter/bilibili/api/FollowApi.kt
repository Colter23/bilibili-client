package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.data.dynamic.BiliDynamicList
import top.colter.bilibili.data.user.BiliRelation


/////////////////////////////////////////////
//                 关注相关                 //
/////////////////////////////////////////////

/**
 * ## 查询是否关注用户
 *
 * **Method:** GET
 *
 * **Authorization:** Cookie(SESSDATA)
 *
 * **Params:** fid=用户ID
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/x/relation?fid=487550002
 */
public const val IS_FOLLOW: String = "$BASE_API/x/relation"

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
 * ## 查看用户是否关注
 *
 * @param mid 用户ID
 *
 * @see IS_FOLLOW
 */
public suspend fun BiliClient.isFollow(mid: Long): BiliRelation {
    return getData(IS_FOLLOW) {
        parameter("fid", mid)
    }
}


/**
 * ## 查看用户是否关注
 *
 * @param mid 用户ID
 *
 * @see IS_FOLLOW
 */
public suspend fun BiliClient.follow(mid: Long): BiliRelation {
    return getData(FOLLOW_MODIFY) {
        parameter("fid", mid)
        parameter("act", 1)
    }
}