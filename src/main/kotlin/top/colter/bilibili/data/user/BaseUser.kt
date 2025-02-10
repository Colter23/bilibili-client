package top.colter.bilibili.data.user

import kotlinx.serialization.Serializable
import top.colter.bilibili.data.LazyImage

public interface User {
    public val mid: Long
    public val name: String
    public val face: LazyImage
}

@Serializable
public data class BaseUser(
    override val mid: Long,
    override val name: String,
    override val face: LazyImage,
): User