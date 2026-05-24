package top.colter.bilibili.exception

/**
 * 接口异常
 */
public open class BiliException(message: String = "BiliBili接口异常"): Exception(message)


/**
 * 接口认证异常
 */
public open class BiliAuthException(message: String = "BiliBili接口认证异常"): BiliException(message)
/**
 * 账号未登录
 */
public open class BiliLoginException(message: String = "BiliBil账号未登录"): BiliAuthException(message)


/**
 * 接口请求异常
 */
public open class BiliRequestException(message: String = "BiliBili接口请求异常"): BiliException(message)
/**
 * 请求被拦截
 */
public open class BiliBanException(message: String = "BiliBili请求被拦截"): BiliRequestException(message)
/**
 * 接口无数据
 */
public open class BiliEmptyException(message: String = "BiliBili接口无数据"): BiliRequestException(message)

