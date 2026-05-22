package top.colter.bilibili.api

import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.postData
import top.colter.bilibili.data.other.ShortLinkData


/////////////////////////////////////////////
//                   其他                   //
/////////////////////////////////////////////

/**
 * ## 获取短链
 *
 * **Method:** POST
 *
 * **Body:** build=6880300&buvid=abcdefg&platform=android&share_channel=QQ&share_mode=3&oid=ID&share_id=页面ID&share_origin=源
 *
 * **Response:** [BiliCommonResult] / [ShortLinkData]
 *
 * **Example:** https://api.bilibili.com/x/share/click
 */
public const val SHORT_LINK: String = "$BASE_API/x/share/click"

/**
 * ## 获取短链
 *
 * @param oid 分享对象 ID
 * @param shareId 页面 ID
 * @param shareOrigin 分享来源
 * @param buvid 设备 buvid
 * @param build 客户端 build 号
 * @param platform 平台
 * @param shareChannel 分享渠道
 * @param shareMode 分享模式
 *
 * @see SHORT_LINK
 */
public suspend fun BiliClient.getShortLink(
    oid: Long,
    shareId: String,
    shareOrigin: String,
    buvid: String,
    build: Int = 6880300,
    platform: String = "android",
    shareChannel: String = "QQ",
    shareMode: Int = 3,
): ShortLinkData {
    return postData(SHORT_LINK) {
        setBody(formBody {
            append("build", build.toString())
            append("buvid", buvid)
            append("platform", platform)
            append("share_channel", shareChannel)
            append("share_mode", shareMode.toString())
            append("oid", oid.toString())
            append("share_id", shareId)
            append("share_origin", shareOrigin)
        })
    }
}
