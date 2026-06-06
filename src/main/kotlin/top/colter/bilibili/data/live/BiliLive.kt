package top.colter.bilibili.data.live

import top.colter.bilibili.data.ImageUrl

public interface BiliLive {
    public val uid: Long
    public val roomId: Long
    public val status: LiveStatus
    public val title: String
    public val cover: ImageUrl
}