package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData


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
 * **Response:** [BiliCommonResult] / [...]
 *
 * **Example:** https://api.bilibili.com/x/share/click
 */
public const val SHORT_LINK: String = "$BASE_API/x/share/click"