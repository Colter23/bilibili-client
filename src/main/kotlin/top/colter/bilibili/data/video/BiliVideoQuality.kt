package top.colter.bilibili.data.video

/**
 * 视频清晰度选择。
 *
 * 可以直接指定具体清晰度，也可以使用自动策略：
 *
 * - [AUTO_HIGHEST]：自动选择返回结果中的最高可用清晰度
 * - [AUTO_LOWEST]：自动选择返回结果中的最低可用清晰度
 * - [atMost]：自动选择不超过指定档位的最高可用清晰度
 */
public sealed class BiliVideoQuality {

    public abstract val code: Int?
    public abstract val description: String

    public data object AutoHighest : BiliVideoQuality() {
        override val code: Int? = null
        override val description: String = "自动最高画质"
    }

    public data object AutoLowest : BiliVideoQuality() {
        override val code: Int? = null
        override val description: String = "自动最低画质"
    }

    public data class Exact(
        override val code: Int,
        override val description: String
    ) : BiliVideoQuality()

    public data class AtMost(
        public val max: Exact
    ) : BiliVideoQuality() {
        override val code: Int = max.code
        override val description: String = "最高不超过 ${max.description}"
    }

    override fun toString(): String = code?.let { "$description($it)" } ?: description

    public companion object {
        public val AUTO_HIGHEST: BiliVideoQuality = AutoHighest
        public val AUTO_LOWEST: BiliVideoQuality = AutoLowest

        public val P240: Exact = Exact(6, "240P 极速")
        public val P360: Exact = Exact(16, "360P 流畅")
        public val P480: Exact = Exact(32, "480P 清晰")
        public val P720: Exact = Exact(64, "720P 高清")
        public val P720_60: Exact = Exact(74, "720P60 高帧率")
        public val P1080: Exact = Exact(80, "1080P 高清")
        public val P1080_PLUS: Exact = Exact(112, "1080P+ 高码率")
        public val P1080_60: Exact = Exact(116, "1080P60 高帧率")
        public val P4K: Exact = Exact(120, "4K 超清")
        public val HDR: Exact = Exact(125, "HDR 真彩")
        public val DOLBY: Exact = Exact(126, "杜比视界")
        public val P8K: Exact = Exact(127, "8K 超高清")

        public val known: List<Exact> = listOf(
            P240,
            P360,
            P480,
            P720,
            P720_60,
            P1080,
            P1080_PLUS,
            P1080_60,
            P4K,
            HDR,
            DOLBY,
            P8K
        )

        public fun fromCode(code: Int): Exact? = known.firstOrNull { it.code == code }

        public fun custom(code: Int, description: String = "自定义画质"): Exact = fromCode(code) ?: Exact(code, description)

        public fun atMost(quality: Exact): BiliVideoQuality = AtMost(quality)
    }
}

public val BiliVideoSupportFormat.videoQuality: BiliVideoQuality.Exact?
    get() = BiliVideoQuality.fromCode(quality)

public val BiliVideoPlayUrl.availableQualities: List<BiliVideoQuality.Exact>
    get() {
        val qualities = acceptQuality.ifEmpty { supportFormats.map { it.quality } }
        return qualities.mapNotNull { BiliVideoQuality.fromCode(it) }.distinct()
    }

internal val BiliVideoQuality.requestCode: Int?
    get() = when (this) {
        BiliVideoQuality.AutoHighest -> BiliVideoQuality.P8K.code
        BiliVideoQuality.AutoLowest -> BiliVideoQuality.P240.code
        is BiliVideoQuality.Exact -> code
        is BiliVideoQuality.AtMost -> max.code
    }
