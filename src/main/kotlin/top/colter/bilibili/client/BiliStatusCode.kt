package top.colter.bilibili.client


//class BiliCodeState(
//    override val code: Int,
//    override val message: String
//): StatusCode {
//    override fun handleStatus() {
//        when (code) {
//            0 -> {}
//            in -115..-1 -> throw BiliAuthException("CODE: $code; MESSAGE: $message")
//            in -701 .. -304 ->  throw BiliRequestException("CODE: $code; MESSAGE: $message")
//            else -> throw BiliException("CODE: $code; MESSAGE: $message")
//        }
//    }
//}

//sealed class BiliCodeState(
//    override val code: Int,
//    override val message: String,
//) : StatusCode {
//    override fun unknownCode(res: BaseResult) {
//        throw BiliException("CODE: ${res.code}; MSG: ${res.message}")
//    }
//
//    object Succeed : BiliCodeState(0, "成功") {
//
//    }
//
//    object NoLogin : BiliCodeState(-101, "账号未登录") {
//        override fun handle() {
//            throw BiliAuthException(message)
//        }
//    }
//
//    object AccountProbation : BiliCodeState(-102, "账号非正式会员或在适应期") {
//        override fun handle() {
//            throw BiliAuthException(message)
//        }
//    }
//
//    object AccountBanned : BiliCodeState(-106, "账号被封停") {
//        override fun handle() {
//            throw BiliAuthException(message)
//        }
//    }
//
//    object CsrfVerifyFail : BiliCodeState(-111, "csrf 校验失败") {
//        override fun handle() {
//            throw BiliAuthException(message)
//        }
//    }
//
//
//    object Fail : BiliCodeState(400, "失败") {
//        override fun handle() {
//            throw Exception()
//        }
//    }
//}
//
//enum class BBiliCodeState(
//    override val code: Int,
//    override val message: String
//): StatusCode {
//    SUCCEED(0, "成功"),
//    NO_LOGIN(-101, "账号未登录"){
//        override fun handle() {
//            throw BiliAuthException(message)
//        }
//    },
//
//
//    ;override fun unknownCode(res: BaseResult) {
//        throw BiliException("CODE: ${res.code}; MSG: ${res.message}")
//    }
//}

