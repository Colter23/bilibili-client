package top.colter.bilibili.api

import io.ktor.client.request.*
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.client.BiliCommonResult
import top.colter.bilibili.client.getData


/////////////////////////////////////////////
//                 搜索相关                 //
/////////////////////////////////////////////
/**
 * TODO("搜索API")
 */
public const val SEARCH: String = "https://api.bilibili.com/x/web-interface/search/type"