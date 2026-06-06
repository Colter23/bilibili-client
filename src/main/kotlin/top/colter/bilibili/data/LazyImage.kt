package top.colter.bilibili.data

@Deprecated(
    message = "Use ImageUrl. This type only represents an image URL and does not lazy-load or cache bytes.",
    replaceWith = ReplaceWith("ImageUrl")
)
public typealias LazyImage = ImageUrl

@Deprecated(
    message = "Use ImageUrlSerializer.",
    replaceWith = ReplaceWith("ImageUrlSerializer")
)
public typealias LazyImageSerializer = ImageUrlSerializer
