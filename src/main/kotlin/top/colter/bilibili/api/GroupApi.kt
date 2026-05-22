package top.colter.bilibili.api

import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.client.postResult
import top.colter.bilibili.data.user.BiliGroup


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
 * **Response:** [BiliCommonResult] / [List<BiliGroup>]
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

/**
 * ## 查询关注分组列表
 *
 * 需要已登录 Cookie。
 *
 * @see GROUP_LIST
 */
public suspend fun BiliClient.getGroupList(): List<BiliGroup> {
    return getData(GROUP_LIST)
}

/**
 * ## 创建关注分组
 *
 * @param tag 分组名，不超过 16 个字符
 * @param csrf CSRF Token；为空时从 Cookie 中读取 bili_jct
 *
 * @see CREATE_GROUP
 */
public suspend fun BiliClient.createGroup(tag: String, csrf: String? = null): BiliCommonResult {
    val token = requireCsrf(csrf)
    return postResult(CREATE_GROUP) {
        setBody(formBody {
            append("tag", tag)
            append("csrf", token)
        })
    }
}

/**
 * ## 删除关注分组
 *
 * @param tagId 分组 ID
 * @param csrf CSRF Token；为空时从 Cookie 中读取 bili_jct
 *
 * @see DEL_GROUP
 */
public suspend fun BiliClient.deleteGroup(tagId: Long, csrf: String? = null): BiliCommonResult {
    val token = requireCsrf(csrf)
    return postResult(DEL_GROUP) {
        setBody(formBody {
            append("tagid", tagId.toString())
            append("csrf", token)
        })
    }
}

/**
 * ## 修改分组成员
 *
 * @param fids 用户 ID 列表
 * @param tagIds 分组 ID 列表
 * @param csrf CSRF Token；为空时从 Cookie 中读取 bili_jct
 *
 * @see GROUP_USER_MODIFY
 */
public suspend fun BiliClient.modifyGroupUsers(
    fids: Iterable<Long>,
    tagIds: Iterable<Long>,
    csrf: String? = null,
): BiliCommonResult {
    val fidList = fids.toList()
    val tagIdList = tagIds.toList()
    require(fidList.isNotEmpty()) { "fids 不能为空" }
    require(tagIdList.isNotEmpty()) { "tagIds 不能为空" }

    val token = requireCsrf(csrf)
    return postResult(GROUP_USER_MODIFY) {
        setBody(formBody {
            append("fids", fidList.joinIds())
            append("tagids", tagIdList.joinIds())
            append("csrf", token)
        })
    }
}
