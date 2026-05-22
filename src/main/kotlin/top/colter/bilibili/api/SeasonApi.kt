package top.colter.bilibili.api

import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.client.postResult


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

/**
 * ## 通过 mdid 获取番剧基本信息 (!未完善!)
 *
 * @param mediaId mdid，不带 md
 *
 * @see SEASON_MEDIA_INFO
 */
public suspend fun BiliClient.getSeasonMediaInfo(mediaId: Long): JsonElement {
    return getData(SEASON_MEDIA_INFO) {
        parameter("media_id", mediaId)
    }
}

/**
 * ## 通过 ssid 或 epid 获取番剧详细信息 (!未完善!)
 *
 * @param seasonId ssid，不带 ss；seasonId 与 episodeId 至少传入一个
 * @param episodeId epid，不带 ep；seasonId 与 episodeId 同时存在时接口以 ep_id 为准
 *
 * @see SEASON_INFO
 */
public suspend fun BiliClient.getSeasonInfo(seasonId: Long? = null, episodeId: Long? = null): JsonElement {
    require(seasonId != null || episodeId != null) { "seasonId 和 episodeId 至少需要传入一个" }
    return getData(SEASON_INFO) {
        seasonId?.let { parameter("season_id", it) }
        episodeId?.let { parameter("ep_id", it) }
    }
}

/**
 * ## 追番 (!未完善!)
 *
 * @param seasonId ssid，不带 ss
 * @param csrf CSRF Token；为空时从 Cookie 中读取 bili_jct
 *
 * @see FOLLOW_SEASON
 */
public suspend fun BiliClient.followSeason(seasonId: Long, csrf: String? = null): BiliCommonResult {
    val token = requireCsrf(csrf)
    return postResult(FOLLOW_SEASON) {
        setBody(formBody {
            append("season_id", seasonId.toString())
            append("csrf", token)
        })
    }
}

/**
 * ## 取消追番 (!未完善!)
 *
 * @param seasonId ssid，不带 ss
 * @param csrf CSRF Token；为空时从 Cookie 中读取 bili_jct
 *
 * @see UNFOLLOW_SEASON
 */
public suspend fun BiliClient.unfollowSeason(seasonId: Long, csrf: String? = null): BiliCommonResult {
    val token = requireCsrf(csrf)
    return postResult(UNFOLLOW_SEASON) {
        setBody(formBody {
            append("season_id", seasonId.toString())
            append("csrf", token)
        })
    }
}
