package top.colter.bilibili.exception

public open class BiliDownloadException(
    message: String = "BiliBili 视频下载失败"
) : BiliException(message)
