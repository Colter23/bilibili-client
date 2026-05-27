package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.data.dynamic.BiliDynamic
import top.colter.bilibili.data.dynamic.BiliDynamicDetail
import top.colter.bilibili.data.dynamic.BiliDynamicList


/////////////////////////////////////////////
//                 动态相关                 //
/////////////////////////////////////////////

public const val DYNAMIC_BASE: String = "x/polymer/web-dynamic/v1"

/**
 * ## 获取最新动态列表
 *
 * **Method:** GET
 *
 * **Authorization:** Cookie(SESSDATA)
 *
 * **Params:** timezone_offset=-480&page=分页&type=类型(all: 全部 | video: 视频 | pgc: 番剧 | article: 专栏)&host_mid=指定获取用户
 *
 * **Response:** [BiliCommonResult] / [BiliDynamicList]
 *
 * **From:** https://t.bilibili.com
 *
 * **Example:** https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/all?timezone_offset=-480&page=1&type=all
 */
public const val NEW_DYNAMIC: String = "$BASE_API/$DYNAMIC_BASE/feed/all"

/**
 * ## 获取用户空间动态
 *
 * **Method:** GET
 *
 * **Params:** timezone_offset=-480&host_mid=用户ID&offset=动态偏移
 *
 * **Response:** [BiliCommonResult] / [BiliDynamicList]
 *
 * **From:** https://space.bilibili.com/487550002/dynamic
 *
 * **Example:** https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/space?timezone_offset=-480&host_mid=487550002&offset=
 */
public const val SPACE_DYNAMIC: String = "$BASE_API/$DYNAMIC_BASE/feed/space"

/**
 * ## 获取单个动态详情
 *
 * **Method:** GET
 *
 * **Params:** timezone_offset=-480&id=动态ID
 *
 * **Response:** [BiliCommonResult] / [BiliDynamic]
 *
 * **From:** https://t.bilibili.com/683357961644408871
 *
 * **Example:** https://api.bilibili.com/x/polymer/web-dynamic/v1/detail?timezone_offset=-480&id=683357961644408871
 */
public const val DYNAMIC_DETAIL: String = "$BASE_API/$DYNAMIC_BASE/detail"



/**
 * ## 获取账号全部最新动态
 *
 * @param page 分页 (每页20左右)
 * @param type 动态类型 all(全部) video(视频)  pgc(番剧)  article(专栏)
 *
 * @see NEW_DYNAMIC
 */
public suspend fun BiliClient.getNewDynamic(page: Int = 1, type: String = "all"): BiliDynamicList {
    return getData(NEW_DYNAMIC) {
        parameter("timezone_offset", "-480")
        parameter("type", type)
        parameter("page", page)
        parameter("features", "itemOpusStyle")
    }
}

/**
 * ## 获取用户最新动态
 *
 * @param uid 用户ID
 * @param hasTop 是否包含置顶动态
 * @param offset 动态偏移
 *
 * @see SPACE_DYNAMIC
 * @see NEW_DYNAMIC
 */
public suspend fun BiliClient.getUserNewDynamic(uid: Long, hasTop: Boolean = false, offset: String = ""): BiliDynamicList {
    return getData(if (hasTop) SPACE_DYNAMIC else NEW_DYNAMIC) {
        parameter("timezone_offset", "-480")
        parameter("host_mid", uid)
        parameter("offset", offset)
        parameter("features", "itemOpusStyle")
    }
}

/**
 * ## 获取指定动态详情
 *
 * @param did 动态ID
 *
 * @see DYNAMIC_DETAIL
 */
public suspend fun BiliClient.getDynamicDetail(did: Long): BiliDynamic {
    return getData<BiliDynamicDetail>(DYNAMIC_DETAIL) {
        parameter("timezone_offset", "-480")
        parameter("id", did)
        parameter("features", "itemOpusStyle")
    }.item
}
