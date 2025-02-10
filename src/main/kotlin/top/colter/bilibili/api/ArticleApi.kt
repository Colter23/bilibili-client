package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData


/////////////////////////////////////////////
//                 专栏相关                 //
/////////////////////////////////////////////

/**
 * ## 获取专栏详情
 *
 * **Method:** GET
 *
 * **Params:** id=cvid(不带cv)
 *
 * **Response:** [BiliCommonResult] / [ArticleDetail]
 *
 * **From:** https://www.bilibili.com/read/cv18257173
 *
 * **Example:** https://api.bilibili.com/x/article/viewinfo?id=18257173
 */
public const val ARTICLE_DETAIL: String = "$BASE_API/x/article/viewinfo"

/**
 * ## 获取专栏详情
 *
 * **Method:** GET
 *
 * **Params:** ids=cvid列表(带cv, 中间用逗号分隔)
 *
 * **Response:** [BiliCommonResult] / [Map(cvid, ArticleDetail)]
 *
 * **From:** https://www.bilibili.com/read/cv18257173
 *
 * **Example:** https://api.bilibili.com/x/article/cards?ids=cv18257173,cv18238121
 */
public const val ARTICLE_LIST: String = "$BASE_API/x/article/cards"