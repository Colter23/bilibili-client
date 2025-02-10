package top.colter.bilibili.data.user

import top.colter.bilibili.data.LazyImage

public interface BiliUser: User{
    override val mid: Long
    override val name: String
    override val face: LazyImage
    public val pendant: Pendant?
    public val official: OfficialVerify?
    public val decorate: Decorate?
}


