package top.colter.bilibili.data.search

/**
 * ## 视频搜索排序方式
 */
public enum class VideoSearchOrder(public val value: String) {
    /** 综合排序（默认） */
    TOTAL_RANK("totalrank"),
    /** 最多播放 */
    CLICK("click"),
    /** 最新发布 */
    PUB_DATE("pubdate"),
    /** 最多弹幕 */
    DANMAKU("dm"),
    /** 最多收藏 */
    STOW("stow")
}

/**
 * ## 用户搜索排序方式
 */
public enum class UserSearchOrder(public val value: String) {
    /** 默认排序 */
    DEFAULT("0"),
    /** 粉丝数 */
    FANS("fans"),
    /** 用户等级 */
    LEVEL("level")
}

/**
 * ## 用户类型筛选
 */
public enum class UserType(public val value: Int) {
    /** 全部用户 */
    ALL(0),
    /** UP主 */
    UP(1),
    /** 普通用户 */
    NORMAL(2),
    /** 认证用户 */
    VERIFIED(3)
}
