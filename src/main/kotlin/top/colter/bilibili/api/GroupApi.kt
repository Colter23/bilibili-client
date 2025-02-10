package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData


/////////////////////////////////////////////
//                 分组相关                 //
/////////////////////////////////////////////

/**
 * ## 查询关注分组列表
 *
 * **Method:** GET
 *
 * **Authorization:** Cookie(SESSDATA)
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/x/relation/tags
 */
public const val GROUP_LIST: String = "$BASE_API/x/relation/tags"

/**
 * ## 创建分组
 *
 * **Method:** POST
 *
 * **Authorization:** Cookie(SESSDATA) 和 CSRF Token
 *
 * **Body:** tag=分组名(不超过16个字符)&csrf=xxxx
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/x/relation/tag/create?tag=Bot关注&csrf=xxxx
 */
public const val CREATE_GROUP: String = "$BASE_API/x/relation/tag/create"

/**
 * ## 删除分组
 *
 * **Method:** POST
 *
 * **Authorization:** Cookie(SESSDATA) 和 CSRF Token
 *
 * **Body:** tagid=分组ID&csrf=xxxx
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/x/relation/tag/del?tagid=11111&csrf=xxxx
 */
public const val DEL_GROUP: String = "$BASE_API/x/relation/tag/del"

/**
 * ## 修改分组成员
 *
 * **Method:** POST
 *
 * **Authorization:** Cookie(SESSDATA) 和 CSRF Token
 *
 * **Body:** fids=用户ID列表(用逗号分隔)&tagids=分组ID列表(用逗号分隔)&csrf=xxxx
 *
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/x/relation/tags/addUsers?fids=487550002,487551829&tagids=11111,22222&csrf=xxxx
 */
public const val GROUP_USER_MODIFY: String = "$BASE_API/x/relation/tags/addUsers"