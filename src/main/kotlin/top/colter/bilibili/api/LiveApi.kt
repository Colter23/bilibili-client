package top.colter.bilibili.api

import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData
import top.colter.bilibili.client.getDataWithWbi
import top.colter.bilibili.data.live.BiliLiveInfo
import top.colter.bilibili.data.live.BiliLiveInfoDetail
import top.colter.bilibili.data.live.danmaku.LiveDanmakuInfo
import top.colter.bilibili.data.live.danmaku.LiveDanmakuMessage
import top.colter.bilibili.data.live.danmaku.LiveDanmakuOptions
import top.colter.bilibili.exception.BiliEmptyException
import top.colter.bilibili.live.danmaku.createLiveDanmakuFlow
import top.colter.bilibili.tools.decode


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
 * **Response:** [BiliCommonResult] / [List<BiliLiveInfo>]
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
 * **Authorization:** room_id=直播间 ID
 *
 * **Response:** [BiliCommonResult] / [BiliLiveInfoDetail]
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
 * ## 获取直播弹幕 WebSocket 连接信息
 *
 * **Method:** GET
 *
 * **Authorization:** id=直播间 ID&type=0
 *
 * **Response:** [BiliCommonResult] / [LiveDanmakuInfo]
 */
public const val LIVE_DANMAKU_INFO: String = "$BASE_LIVE_API/xlive/web-room/v1/index/getDanmuInfo"

/**
 * ## 获取账号直播列表
 *
 * 需要已登录 Cookie。
 *
 * @see LIVE_LIST
 */
public suspend fun BiliClient.getLiveList(): List<BiliLiveInfo> {
    val data = getData<JsonObject>(LIVE_LIST)
    return (data["rooms"] ?: throw BiliEmptyException("直播列表为空")).decode()
}

/**
 * ## 获取直播间信息
 * @param roomId 房间ID 支持短号
 *
 * @see LIVE_INFO
 */
public suspend fun BiliClient.getLiveInfo(roomId: Long): BiliLiveInfoDetail {
    return getData(LIVE_INFO) {
        parameter("room_id", roomId)
    }
}

/**
 * ## 通过 uid 批量获取直播间状态
 *
 * @param uids 用户 ID 列表。
 *
 * @see LIVE_STATUS_BATCH
 */
public suspend fun BiliClient.getLiveStatusBatch(uids: Iterable<Long>): Map<Long, BiliLiveInfo> {
    val uidList = uids.toList()
    require(uidList.isNotEmpty()) { "uids 不能为空" }
    return getData(LIVE_STATUS_BATCH) {
        uidList.forEach { parameter("uids[]", it) }
    }
}

/**
 * ## 获取直播弹幕 WebSocket 连接信息
 *
 * @param roomId 真实直播间 ID。
 *
 * @see LIVE_DANMAKU_INFO
 */
public suspend fun BiliClient.getLiveDanmakuInfo(roomId: Long): LiveDanmakuInfo {
    return getDataWithWbi(LIVE_DANMAKU_INFO) {
        parameter("id", roomId)
        parameter("type", 0)
        parameter("web_location", "444.8")
    }
}

/**
 * ## 连接并解析直播弹幕
 *
 * 返回冷 [Flow]：开始 collect 时建立 WebSocket，取消 collect 时关闭连接。
 *
 * @param roomId 直播间 ID，支持短号，连接前会先解析为真实房间号。
 * @param options 鉴权、心跳和重连选项。
 */
public fun BiliClient.liveDanmakuMessages(
    roomId: Long,
    options: LiveDanmakuOptions = LiveDanmakuOptions()
): Flow<LiveDanmakuMessage> {
    return createLiveDanmakuFlow(roomId, options)
}
