package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.getDataWithWbi
import top.colter.bilibili.data.search.*


/////////////////////////////////////////////
//                 搜索相关                 //
/////////////////////////////////////////////

/**
 * ## 搜索视频
 *
 * **Method:** GET
 *
 * **URL:** https://api.bilibili.com/x/web-interface/wbi/search/type
 *
 * **Response:** [SearchResponse]<List<[VideoSearchResult]>>
 *
 * @param keyword 搜索关键词
 * @param page 页数，默认 1
 * @param order 排序方式，默认综合排序
 * @param duration 视频时长筛选（分钟）：0-全部，1-10分钟以下，2-10-30分钟，3-30-60分钟，4-60分钟以上
 * @param tids 视频分区筛选，默认 0（全部分区）
 */
public suspend fun BiliClient.searchVideo(
    keyword: String,
    page: Int = 1,
    order: VideoSearchOrder = VideoSearchOrder.TOTAL_RANK,
    duration: Int = 0,
    tids: Int = 0
): SearchResponse<List<VideoSearchResult>> {
    return getDataWithWbi("https://api.bilibili.com/x/web-interface/wbi/search/type") {
        parameter("search_type", "video")
        parameter("keyword", keyword)
        parameter("page", page)
        parameter("order", order.value)
        parameter("duration", duration)
        parameter("tids", tids)
    }
}

/**
 * ## 搜索用户
 *
 * **Method:** GET
 *
 * **URL:** https://api.bilibili.com/x/web-interface/wbi/search/type
 *
 * **Response:** [SearchResponse]<List<[UserSearchResult]>>
 *
 * @param keyword 搜索关键词
 * @param page 页数，默认 1
 * @param order 排序方式，默认排序
 * @param orderSort 排序顺序：0-由高到低，1-由低到高
 * @param userType 用户类型筛选，默认全部用户
 */
public suspend fun BiliClient.searchUser(
    keyword: String,
    page: Int = 1,
    order: UserSearchOrder = UserSearchOrder.DEFAULT,
    orderSort: Int = 0,
    userType: UserType = UserType.ALL
): SearchResponse<List<UserSearchResult>> {
    return getDataWithWbi("https://api.bilibili.com/x/web-interface/wbi/search/type") {
        parameter("search_type", "bili_user")
        parameter("keyword", keyword)
        parameter("page", page)
        parameter("order", order.value)
        parameter("order_sort", orderSort)
        parameter("user_type", userType.value)
    }
}
