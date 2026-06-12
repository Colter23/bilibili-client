import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.colter.bilibili.api.searchUser
import top.colter.bilibili.api.searchVideo
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.data.search.UserSearchOrder
import top.colter.bilibili.data.search.UserType
import top.colter.bilibili.data.search.VideoSearchOrder


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class SearchTest {

    private val client = BiliClient()
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun <T> T.log(block: StringBuilder.(T) -> Unit) {
        logger.info("\n" + buildString { block(this@log) })
    }

    @Test
    fun `search video`(): Unit = runBlocking {
        client.searchVideo(
            keyword = "原神",
            page = 1,
            order = VideoSearchOrder.CLICK
        ).log { result ->
            appendLine("搜索ID: ${result.searchId}")
            appendLine("总结果数: ${result.totalResults}")
            appendLine("总页数: ${result.totalPages}")
            appendLine("当前页: ${result.page} (每页${result.pageSize}条)")
            appendLine()
            result.result.take(3).forEach { video ->
                appendLine("视频: ${video.plainTitle}")
                appendLine("  原始标题: ${video.title}")
                appendLine("  BV号: ${video.bvid}")
                appendLine("  UP主: ${video.author} (${video.mid})")
                appendLine("  播放: ${video.play} | 弹幕: ${video.danmaku} | 收藏: ${video.favorites}")
                appendLine("  时长: ${video.duration}")
                appendLine("  分区: ${video.typeName}")
                appendLine("  封面URL: ${video.pic.url}")
                appendLine("  完整封面URL: ${video.pic.fullUrl}")
                appendLine()
            }
        }
    }

    @Test
    fun `search user`(): Unit = runBlocking {
        client.searchUser(
            keyword = "原神",
            page = 1,
            order = UserSearchOrder.FANS,
            userType = UserType.UP
        ).log { result ->
            appendLine("搜索ID: ${result.searchId}")
            appendLine("总结果数: ${result.totalResults}")
            appendLine("总页数: ${result.totalPages}")
            appendLine("当前页: ${result.page} (每页${result.pageSize}条)")
            appendLine()
            result.result.take(3).forEach { user ->
                appendLine("用户: ${user.name}")
                appendLine("  UID: ${user.mid}")
                appendLine("  签名: ${user.sign ?: "无"}")
                appendLine("  粉丝: ${user.fans} | 投稿: ${user.videos}")
                appendLine("  等级: Lv${user.level}")
                appendLine("  认证: ${user.official?.desc ?: "无"}")
                appendLine("  头像URL: ${user.face.url}")
                appendLine("  完整头像URL: ${user.face.fullUrl}")
                appendLine()
            }
        }
    }

    @Test
    fun `search video with filters`(): Unit = runBlocking {
        client.searchVideo(
            keyword = "音乐",
            page = 1,
            order = VideoSearchOrder.PUB_DATE,
            duration = 2,  // 10-30分钟
            tids = 3  // 音乐分区
        ).log { result ->
            appendLine("筛选条件: 音乐分区, 10-30分钟, 按最新排序")
            appendLine("结果数: ${result.totalResults}")
            appendLine()
            result.result.take(2).forEach { video ->
                appendLine("${video.title} - ${video.duration}")
            }
        }
    }
}
