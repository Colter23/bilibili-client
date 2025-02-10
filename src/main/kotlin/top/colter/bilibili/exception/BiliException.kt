package top.colter.bilibili.exception


public open class BiliException(message: String = "BiliBili接口异常"): Exception(message)

public open class BiliAuthException(message: String = "BiliBili接口认证异常"): BiliException(message)

public open class BiliRequestException(message: String = "BiliBili接口请求异常"): BiliException(message)

public open class BiliBanException(message: String = "BiliBili接口访问频繁"): BiliRequestException(message)

public open class BiliEmptyException(message: String = "BiliBili接口无数据"): BiliRequestException(message)

//class BiliLoginException: BiliAuthException {
//    public constructor() : super()
//    public constructor(message: String?) : super(message)
//    public constructor(message: String?, cause: Throwable?) : super(message, cause)
//    public constructor(cause: Throwable?) : super(cause)
//}
