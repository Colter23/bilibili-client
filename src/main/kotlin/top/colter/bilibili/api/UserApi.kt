package top.colter.bilibili.api

import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.data.user.BiliUserInfo


/////////////////////////////////////////////
//                 用户相关                 //
/////////////////////////////////////////////

/**
 * ## 获取指定用户信息
 *
 * **Method:** GET
 *
 * **Params:** mid=用户ID
 *
 * **Response:** [BiliCommonResult] / [BiliUserInfo]
 *
 * **From:** https://space.bilibili.com/487550002/
 *
 * **Example:** https://api.bilibili.com/x/space/wbi/acc/info?mid=487550002
 */
public const val USER_INFO: String = "$BASE_API/x/space/wbi/acc/info"

/**
 * ## 获取当前账号信息
 *
 * **Method:** GET
 *
 * **Authorization:** Cookie(SESSDATA)
 *
 * **Response:** [BiliCommonResult] / [BiliUser]
 *
 * **Example:** https://api.bilibili.com/x/space/myinfo
 */
public const val CURRENT_USER: String = "$BASE_API/x/space/myinfo"

/**
 * ## 获取当前账号信息
 *
 * **Method:** GET
 *
 * **Authorization:** Cookie(SESSDATA)
 *
 * **Response:** [BiliCommonResult] / [BiliUser]
 *
 * **Example:** https://api.bilibili.com/x/web-interface/nav
 */
public const val CURRENT_USER_NAV: String = "$BASE_API/x/web-interface/nav"

/**
 * ## 批量获取用户信息
 *
 * **Method:** GET
 *
 * **Params:** uids=用户ID列表(中间用逗号分隔)
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.vc.bilibili.com/account/v1/user/cards?uids=487550002,487551829
 */
public const val USER_INFO_BATCH: String = "$BASE_API/account/v1/user/cards"

/**
 * ## 查询用户视频投稿信息
 *
 * **Method:** GET
 *
 * **Params:** mid=用户ID&tid=0(分区ID 0为全部)&pn=页数&ps=每页数量&keyword=(关键词搜索)&order=排序(pubdate: 发布时间 | click: 播放量 | stow: 收藏量)
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **From:** https://space.bilibili.com/487550002/video
 *
 * **Example:** https://api.bilibili.com/x/space/arc/search?mid=487550002&ps=30&tid=0&pn=1&keyword=&order=pubdate
 */
public const val SEARCH_USER_VIDEO: String = "$BASE_API/x/space/arc/search"


/**
 * ## 获取指定用户信息
 *
 * @param mid 用户ID
 *
 * @see USER_INFO
 */
public suspend fun BiliClient.getUserInfo(mid: Long): BiliUserInfo {
    return getData(USER_INFO) {
        parameter("mid", mid)
    }
}

/**
 * ## 获取当前账号空间信息 (!未完善!)
 *
 * 需要已登录 Cookie。
 *
 * @see CURRENT_USER
 */
public suspend fun BiliClient.getCurrentUser(): JsonElement {
    return getData(CURRENT_USER)
}

/**
 * ## 获取当前账号导航信息 (!未完善!)
 *
 * 需要已登录 Cookie。
 *
 * @see CURRENT_USER_NAV
 */
public suspend fun BiliClient.getCurrentUserNav(): JsonElement {
    return getData(CURRENT_USER_NAV)
}

/**
 * ## 批量获取用户信息 (!未完善!)
 *
 * @param uids 用户 ID 列表
 *
 * @see USER_INFO_BATCH
 */
public suspend fun BiliClient.getUserInfoBatch(uids: Iterable<Long>): JsonElement {
    val uidList = uids.toList()
    require(uidList.isNotEmpty()) { "uids 不能为空" }
    return getData(USER_INFO_BATCH) {
        parameter("uids", uidList.joinIds())
    }
}

/**
 * ## 查询用户视频投稿信息 (!未完善!)
 *
 * @param mid 用户 ID
 * @param tid 分区 ID，0 为全部
 * @param pn 页数
 * @param ps 每页数量
 * @param keyword 搜索关键词
 * @param order 排序方式：pubdate(发布时间)、click(播放量)、stow(收藏量)
 *
 * @see SEARCH_USER_VIDEO
 */
public suspend fun BiliClient.searchUserVideo(
    mid: Long,
    tid: Int = 0,
    pn: Int = 1,
    ps: Int = 30,
    keyword: String = "",
    order: String = "pubdate",
): JsonElement {
    return getData(SEARCH_USER_VIDEO) {
        parameter("mid", mid)
        parameter("tid", tid)
        parameter("pn", pn)
        parameter("ps", ps)
        parameter("keyword", keyword)
        parameter("order", order)
    }
}
