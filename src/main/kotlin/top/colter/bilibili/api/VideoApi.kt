package top.colter.bilibili.api

import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData


/////////////////////////////////////////////
//                 视频相关                 //
/////////////////////////////////////////////

/**
 * ## 获取视频详情
 *
 * **Method:** GET
 *
 * **Params:** aid=avid(不带av) | bvid=BVid  (两个都有以bvid为准)
 *
 * **Response:** [BiliCommonResult] / [VideoDetail]
 *
 * **From:** https://www.bilibili.com/video/BV1SV411t7G5
 *
 * **Example:** https://api.bilibili.com/x/web-interface/view?aid=416215637&bvid=BV1SV411t7G5
 */
public const val VIDEO_DETAIL: String = "$BASE_API/x/web-interface/view"

/**
 * ## 获取视频详情 (!未完善!)
 *
 * @param aid avid，不带 av；aid 与 bvid 至少传入一个
 * @param bvid BVid；aid 与 bvid 同时存在时接口以 bvid 为准
 *
 * @see VIDEO_DETAIL
 */
public suspend fun BiliClient.getVideoDetail(aid: Long? = null, bvid: String? = null): JsonElement {
    require(aid != null || !bvid.isNullOrBlank()) { "aid 和 bvid 至少需要传入一个" }
    return getData(VIDEO_DETAIL) {
        aid?.let { parameter("aid", it) }
        bvid?.takeIf { it.isNotBlank() }?.let { parameter("bvid", it) }
    }
}
