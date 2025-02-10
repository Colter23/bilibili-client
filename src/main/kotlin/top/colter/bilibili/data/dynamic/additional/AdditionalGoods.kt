package top.colter.bilibili.data.dynamic.additional

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.colter.bilibili.data.ImageType
import top.colter.bilibili.data.ImgType
import top.colter.bilibili.data.LazyImage


/**
 * 商品
 * https://t.bilibili.com/649213796294852628 单个商品
 * https://t.bilibili.com/648964520676425729 副标题
 * https://t.bilibili.com/649594558007476241 淘宝商品
 * https://t.bilibili.com/648921605368447013 多个商品
 * @param headIcon 头图标
 * @param headText 头文字
 * @param jumpUrl 跳转链接
 * @param items 商品列表
 */
@Serializable
public data class AdditionalGoods(
    @SerialName("head_icon")
    val headIcon: String,
    @SerialName("head_text")
    val headText: String,
    @SerialName("jump_url")
    val jumpUrl: String,
    @SerialName("items")
    val items: List<AdditionalGoodItem>,
)

/**
 * 商品项
 * @param id 商品ID b站商品为Long 外站商品为String
 * @param name 名称
 * @param brief 副标题
 * @param cover 封面
 * @param jumpDesc 跳转按钮描述
 * @param jumpUrl 跳转链接
 */
@Serializable
public data class AdditionalGoodItem(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("brief")
    val brief: String,
    @SerialName("cover")
    @ImgType(ImageType.OTHER)
    val cover: LazyImage,
    @SerialName("price")
    val price: String,
    @SerialName("jump_desc")
    val jumpDesc: String,
    @SerialName("jump_url")
    val jumpUrl: String,
)