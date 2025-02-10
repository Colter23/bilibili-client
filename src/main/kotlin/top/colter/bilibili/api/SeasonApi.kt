package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData


/////////////////////////////////////////////
//                 番剧相关                 //
/////////////////////////////////////////////

/**
 * ## 通过mdid获取番剧基本信息
 *
 * **Method:** GET
 *
 * **Params:** media_id=mdid(不带md)
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **From:** https://www.bilibili.com/bangumi/media/md1588
 *
 * **Example:** https://api.bilibili.com/pgc/review/user?media_id=1588
 */
public const val SEASON_MEDIA_INFO: String = "$BASE_API/pgc/review/user"

/**
 * ## 通过ssid/epid获取番剧详细信息
 *
 * **Method:** GET
 *
 * **Params:** ep_id=epid(不带ep) | season_id=ssid(不带ss)
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **From:** https://www.bilibili.com/bangumi/play/ss1588
 *
 * **Example:** https://api.bilibili.com/pgc/view/web/season?season_id=1588
 */
public const val SEASON_INFO: String = "$BASE_API/pgc/view/web/season"

/**
 * ## 追番
 *
 * **Method:** POST
 *
 * **Authorization:** Cookie(SESSDATA) 和 CSRF Token
 *
 * **Body:** season_id=ssid(不带ss)&csrf=xxxx
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/pgc/web/follow/add?season_id=1588&csrf=xxxx
 */
public const val FOLLOW_SEASON: String = "$BASE_API/pgc/web/follow/add"

/**
 * ## 取消追番
 *
 * **Method:** POST
 *
 * **Authorization:** Cookie(SESSDATA) 和 CSRF Token
 *
 * **Body:** season_id=ssid(不带ss)&csrf=xxxx
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/pgc/web/follow/del?season_id=1588&csrf=xxxx
 */
public const val UNFOLLOW_SEASON: String = "$BASE_API/pgc/web/follow/del"