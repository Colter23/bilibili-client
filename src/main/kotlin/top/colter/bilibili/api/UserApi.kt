package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.data.live.BiliLiveInfo
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
