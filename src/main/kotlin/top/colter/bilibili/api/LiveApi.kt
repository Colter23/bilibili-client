package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.data.dynamic.BiliDynamicList
import top.colter.bilibili.data.live.BiliLiveInfo


/////////////////////////////////////////////
//                 直播相关                 //
/////////////////////////////////////////////

/**
 * ## 获取账号直播列表(按开播顺序)
 *
 * **Method:** GET
 *
 * **Authorization:** Cookie(SESSDATA)
 *
 * **Response:** [BiliCommonResult] / [LiveList]
 *
 * **From:** https://www.bilibili.com 动态/直播动态
 *
 * **Example:** https://api.live.bilibili.com/xlive/web-ucenter/v1/xfetter/GetWebList
 */
public const val LIVE_LIST: String = "$BASE_LIVE_API/xlive/web-ucenter/v1/xfetter/GetWebList"

/**
 * ## 通过roomid获取直播间信息
 *
 * **Method:** GET
 *
 * **Params:** room_id=直播间ID
 *
 * **Response:** [BiliCommonResult] / [BiliLiveInfo]
 *
 * **From:** https://live.bilibili.com/21811136
 *
 * **Example:** https://api.live.bilibili.com/room/v1/Room/get_info?room_id=21811136
 */
public const val LIVE_INFO: String = "$BASE_LIVE_API/room/v1/Room/get_info"

/**
 * ## 通过uid批量获取直播间状态
 *
 * **Method:** GET
 *
 * **Params:** uids[]=用户ID&uids[]=用户ID
 *
 * **Response:** [BiliCommonResult] / [Map(uid, LiveInfo)]
 *
 * **Example:** https://api.live.bilibili.com/room/v1/Room/get_status_info_by_uids?uids[]=487550002&uids[]=487551829
 */
public const val LIVE_STATUS_BATCH: String = "$BASE_LIVE_API/room/v1/Room/get_status_info_by_uids"


/**
 * ## 获取直播间信息
 * @param roomId 房间ID 支持短号
 *
 * @see LIVE_INFO
 */
public suspend fun BiliClient.getLiveInfo(roomId: Long): BiliLiveInfo {
    return getData(LIVE_INFO) {
        parameter("room_id", roomId)
    }
}
