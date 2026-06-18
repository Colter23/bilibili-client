import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.colter.bilibili.api.FollowAction
import top.colter.bilibili.api.downloadVideo
import top.colter.bilibili.api.getDynamicDetail
import top.colter.bilibili.api.getLiveStatusBatch
import top.colter.bilibili.api.getNewDynamic
import top.colter.bilibili.api.getUserBatch
import top.colter.bilibili.api.getUserInfoBatch
import top.colter.bilibili.api.getUserInfoCard
import top.colter.bilibili.api.getUserNewDynamic
import top.colter.bilibili.api.modifyFollowBatch
import top.colter.bilibili.api.probeVideoDownloadSize
import top.colter.bilibili.api.relation
import top.colter.bilibili.api.relations
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.data.EditCookie
import top.colter.bilibili.data.toCookie
import top.colter.bilibili.data.dynamic.author
import top.colter.bilibili.data.video.BiliVideoQuality
import top.colter.bilibili.exception.BiliLoginException
import top.colter.bilibili.tools.decode
import java.io.FileNotFoundException


@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ApiTest {

    private val client = BiliClient()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun <T> T.log(block: StringBuilder.(T) -> Unit) {
        logger.info("\n" + buildString { block(this@log) })
    }

    private suspend fun skipIfLoginRequired(block: suspend () -> Unit) {
        try {
            block()
        } catch (e: BiliLoginException) {
            Assumptions.assumeTrue(false, "需要登录 Cookie：${e.message}")
        }
    }

    @BeforeAll
    fun initCookie() {
        // 使用 https://cookie-editor.cgagnier.ca/ 导出cookie到 test/resources 目录
        try {
            val cookies = loadTestText(fileName = "cookie.json").decode<List<EditCookie>>().map { it.toCookie() }
            client.storage.initialize(cookies)
        }catch (e: FileNotFoundException) {
            logger.error("未找到cookie文件，将无法使用部分api")
        }
    }

    @Test
    fun `get all new dynamic`(): Unit = runBlocking {
        skipIfLoginRequired {
            client.getNewDynamic().log { list ->
                appendLine("Dynamic Count: ${list.items.size}")
                appendLine()
                list.items.forEach {
                    appendLine("ID: ${it.id}")
                    appendLine("User: ${it.author.mid}@${it.author.name}")
                    appendLine("Type: ${it.type}")
                    appendLine()
                }
            }
        }
    }

    @Test
    fun `get user space new dynamic`(): Unit = runBlocking {
        skipIfLoginRequired {
            client.getUserNewDynamic(487550002L).log { list ->
                appendLine("Dynamic Count: ${list.items.size}")
                appendLine()
                list.items.forEach {
                    appendLine("ID: ${it.id}")
                    appendLine("Type: ${it.type}")
                    appendLine()
                }
            }
        }
    }

    @Test
    fun `get dynamic detail`(): Unit = runBlocking {
        client.getDynamicDetail(741022677854060553L).log {
            appendLine("Type: ${it.type}")
            appendLine("Time: ${it.formatTime}")
        }
    }

    @Test
    fun `get live status batch`(): Unit = runBlocking {
        client.getLiveStatusBatch(listOf(524484462)).log { map ->
            appendLine("UID: ${map.keys}")
        }
    }

    @Test
    fun `get user card`(): Unit = runBlocking {
        client.getUserInfoCard(524484462).log { card ->
            appendLine("Header: ${card.header.img.url}")
        }
    }

    @Test
    fun `get user batch`(): Unit = runBlocking {
        client.getUserBatch(listOf(524484462,487550002)).log { info ->
            info.forEach {
                appendLine("Name: ${it.name}")
            }
        }
    }

    @Test
    fun `get user info batch`(): Unit = runBlocking {
        client.getUserInfoBatch(listOf(524484462,487550002)).log { info ->
            info.forEach {
                appendLine("Name: ${it.name}")
            }
        }
    }

    @Test
    fun `get user relations`(): Unit = runBlocking {
        client.relations(listOf(70666)).log { info ->
            info.forEach {
                appendLine("UID: ${it.value.mid}   State: ${it.value.attribute}")
            }
        }
    }

    @Test
    fun `get user relation`(): Unit = runBlocking {
        client.relation(70666).log { info ->
            appendLine("UID: ${info.mid}   State: ${info.attribute}")
        }
    }

    @Test
    fun `get follow user batch`(): Unit = runBlocking {
        client.modifyFollowBatch(listOf(1836415750), FollowAction.FOLLOW).log { info ->
            appendLine(info.data)
            appendLine(info.result)
        }
    }


    @Test
    fun probeVideoDownloadSizeManually(): Unit = runBlocking {
        Assumptions.assumeTrue(
            System.getenv("BILI_MANUAL_PROBE") == "true",
            "设置 BILI_MANUAL_PROBE=true 后运行真实视频大小探测测试"
        )

        val size = client.probeVideoDownloadSize(
            bvid = "BV14qVz6tE8L",
            page = 1,
            quality = BiliVideoQuality.AUTO_HIGHEST,
        )

        size.log {
            appendLine("Total Bytes: ${it.totalBytes ?: "unknown"}")
            appendLine("Video Bytes: ${it.video?.sizeBytes ?: "unknown"}")
            appendLine("Audio Bytes: ${it.audio?.sizeBytes ?: "unknown"}")
            appendLine("Durl Bytes: ${it.durl.joinToString { item -> item.sizeBytes?.toString() ?: "unknown" }}")
            appendLine("Video Quality: ${it.videoStream?.id ?: "none"}")
            appendLine("Audio Quality: ${it.audioStream?.id ?: "none"}")
        }
    }

    @Test
    fun downloadVideoManually(): Unit = runBlocking {
        Assumptions.assumeTrue(
            System.getenv("BILI_MANUAL_DOWNLOAD") == "true",
            "设置 BILI_MANUAL_DOWNLOAD=true 后运行真实下载测试"
        )

        val outputDir = testOutput.resolve("video-download")
        val ffmpegPath = System.getenv("BILI_FFMPEG")?.takeIf { it.isNotBlank() }

        val result = client.downloadVideo(
            directory = outputDir,
            bvid = "BV14qVz6tE8L",
            page = 1,
            quality = BiliVideoQuality.AUTO_HIGHEST,
            fileName = "manual-video-download",
            ffmpegPath = ffmpegPath,
            overwrite = true,
        ) { progress ->
            val total = progress.totalBytes?.toString() ?: "unknown"
            logger.info("${progress.type}: ${progress.downloadedBytes}/$total -> ${progress.targetFile.absolutePath}")
        }

        result.log {
            appendLine("Final File: ${it.finalFile?.absolutePath ?: "none"}")
            appendLine("Video File: ${it.videoFile?.absolutePath ?: "none"}")
            appendLine("Audio File: ${it.audioFile?.absolutePath ?: "none"}")
            appendLine("Durl Files: ${it.durlFiles.joinToString { file -> file.absolutePath }}")
        }
    }

}
