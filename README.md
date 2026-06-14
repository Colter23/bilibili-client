# bilibili-client

一个用 Kotlin 编写的 Bilibili 接口客户端库，封装了常用的用户、动态、直播、搜索、视频播放地址与下载相关接口。

> 这是非官方项目，接口行为可能随 Bilibili 页面和接口变化而变化，请按实际业务做好异常处理。

## 安装

Gradle Kotlin DSL:

```kotlin
dependencies {
    implementation("top.colter.bilibili:bilibili-client:0.0.1")
}
```

## 基本使用

```kotlin
import kotlinx.coroutines.runBlocking
import top.colter.bilibili.api.getDynamicDetail
import top.colter.bilibili.client.BiliClient

fun main() = runBlocking {
    val client = BiliClient()
    try {
        val dynamic = client.getDynamicDetail(741022677854060553L)
        println(dynamic.type)
        println(dynamic.formatTime)
    } finally {
        client.close()
    }
}
```

## Cookie

部分接口需要登录态，可以导入浏览器 Cookie：

```kotlin
client.importEditCookiesJson(cookiesJson)
```

`cookiesJson` 可以使用 Cookie Editor 一类浏览器扩展导出。库内也提供了 `exportEditCookiesJson()`，方便保存和恢复登录态。

## 常用能力

- 用户信息、关注分组与关系接口
- 动态列表、动态详情与图文内容解析
- 直播信息、直播状态与直播弹幕流
- 搜索视频、搜索用户
- 视频播放地址解析、清晰度选择、下载与可选 ffmpeg 合并
- Cookie 导入导出与通用 JSON 容错解析

## License

MIT
