package top.colter.bilibili.api

import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData


/////////////////////////////////////////////
//                 搜索相关                 //
/////////////////////////////////////////////
/**
 * ## 搜索
 *
 * **Method:** GET
 *
 * **Params:** search_type=搜索类型&keyword=关键词&page=页数&order=排序
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/x/web-interface/search/type?search_type=video&keyword=哔哩哔哩&page=1
 */
public const val SEARCH: String = "https://api.bilibili.com/x/web-interface/search/type"

/**
 * ## 搜索 (!未完善!)
 *
 * @param keyword 搜索关键词
 * @param searchType 搜索类型，常见值：video、media_bangumi、media_ft、bili_user、article
 * @param page 页数
 * @param order 排序方式，为空时使用接口默认排序
 *
 * @see SEARCH
 */
public suspend fun BiliClient.search(
    keyword: String,
    searchType: String = "video",
    page: Int = 1,
    order: String? = null,
): JsonElement {
    return getData(SEARCH) {
        parameter("search_type", searchType)
        parameter("keyword", keyword)
        parameter("page", page)
        order?.takeIf { it.isNotBlank() }?.let { parameter("order", it) }
    }
}
