package top.colter.bilibili.api

import io.ktor.client.request.*
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