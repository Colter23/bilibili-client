package top.colter.bilibili.data.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.ImageUrl

/**
 * 头像挂件
 * @param pid 挂件ID
 * @param name 挂件名
 * @param image 挂件图片
 */
@Serializable
public data class Pendant(
    val pid: Long,
    val name: String,
    @SerialName("image")
    @ImgType(ImageType.USER)
    val image: ImageUrl
)