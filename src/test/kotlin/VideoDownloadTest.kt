import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import top.colter.bilibili.api.downloadVideo
import top.colter.bilibili.api.selectBestAudioStream
import top.colter.bilibili.api.selectBestVideoStream
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.data.video.BiliDownloadProgress
import top.colter.bilibili.data.video.BiliVideoDash
import top.colter.bilibili.data.video.BiliVideoDashStream
import top.colter.bilibili.data.video.BiliVideoDownloadStreamType
import top.colter.bilibili.data.video.BiliVideoPlayUrl
import top.colter.bilibili.data.video.BiliVideoQuality
import top.colter.bilibili.exception.BiliDownloadException
import top.colter.bilibili.tools.decode
import java.io.File
import java.net.InetSocketAddress
import java.nio.file.Files
import java.util.concurrent.Executors
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class VideoDownloadTest {

    @Test
    fun `decode dash playurl fixture and select streams`() {
        val playUrl = loadTestText("json", "VIDEO-PLAYURL-DASH.json").decode<BiliVideoPlayUrl>()

        val video = playUrl.selectBestVideoStream()
        val audio = playUrl.selectBestAudioStream()

        assertEquals(80, video.id)
        assertEquals(7, video.codecid)
        assertEquals("https://example.test/video-avc.m4s", video.baseUrl)
        assertEquals(30280, audio?.id)
        assertEquals(80, playUrl.supportFormats.single().quality)
    }

    @Test
    fun `decode durl playurl fixture`() {
        val playUrl = loadTestText("json", "VIDEO-PLAYURL-DURL.json").decode<BiliVideoPlayUrl>()

        assertNull(playUrl.dash)
        assertEquals("https://example.test/video.mp4", playUrl.durl.single().url)
        assertEquals("https://example.test/video-backup.mp4", playUrl.durl.single().backupUrl?.single())
    }

    @Test
    fun `decode nullable backup urls`() {
        val playUrl = """
            {
              "dash": {
                "video": [
                  {
                    "id": 80,
                    "baseUrl": "https://example.test/video.m4s",
                    "backupUrl": null
                  }
                ],
                "audio": null
              },
              "durl": [
                {
                  "order": 1,
                  "url": "https://example.test/video.mp4",
                  "backup_url": null
                }
              ]
            }
        """.trimIndent().decode<BiliVideoPlayUrl>()

        assertEquals(listOf("https://example.test/video.m4s"), playUrl.dash!!.video.single().urls)
        assertEquals(listOf("https://example.test/video.mp4"), playUrl.durl.single().urls)
    }

    @Test
    fun `select exact quality and fail unavailable quality`() {
        val playUrl = loadTestText("json", "VIDEO-PLAYURL-DASH.json").decode<BiliVideoPlayUrl>()

        assertEquals(64, playUrl.selectBestVideoStream(BiliVideoQuality.P720).id)
        assertFailsWith<BiliDownloadException> {
            playUrl.selectBestVideoStream(BiliVideoQuality.custom(999))
        }
    }

    @Test
    fun `select video stream by automatic quality options`() {
        val playUrl = loadTestText("json", "VIDEO-PLAYURL-DASH.json").decode<BiliVideoPlayUrl>()

        assertEquals(80, playUrl.selectBestVideoStream(BiliVideoQuality.AUTO_HIGHEST).id)
        assertEquals(64, playUrl.selectBestVideoStream(BiliVideoQuality.AUTO_LOWEST).id)
        assertEquals(64, playUrl.selectBestVideoStream(BiliVideoQuality.atMost(BiliVideoQuality.P720)).id)
    }

    @Test
    fun `download dash streams with referer header and progress`() = runBlocking {
        val server = LocalVideoServer(
            videoBytes = "video-bytes".toByteArray(),
            audioBytes = "audio-bytes".toByteArray()
        )
        val directory = tempDirectory()
        val client = BiliClient()
        val progress = mutableListOf<BiliDownloadProgress>()
        try {
            server.start()
            val result = client.downloadVideo(
                playUrl = localDashPlayUrl(server.videoUrl, server.audioUrl),
                directory = directory,
                fileName = "sample",
                bvid = "BVdownloadtest",
                onProgress = { progress += it }
            )

            assertEquals("video-bytes", result.videoFile!!.readText())
            assertEquals("audio-bytes", result.audioFile!!.readText())
            assertNull(result.finalFile)
            assertEquals("https://www.bilibili.com/video/BVdownloadtest", server.referers["/video.m4s"])
            assertEquals("https://www.bilibili.com/video/BVdownloadtest", server.referers["/audio.m4s"])
            assertTrue(progress.any { it.type == BiliVideoDownloadStreamType.VIDEO && it.totalBytes == 11L })
            assertTrue(progress.any { it.type == BiliVideoDownloadStreamType.AUDIO && it.totalBytes == 11L })
        } finally {
            client.close()
            server.close()
            directory.deleteRecursively()
        }
    }

    @Test
    fun `download protects existing files by default`() = runBlocking {
        val directory = tempDirectory()
        val client = BiliClient()
        try {
            directory.resolve("sample.video.m4s").writeText("old")

            assertFailsWith<BiliDownloadException> {
                client.downloadVideo(
                    playUrl = localDashPlayUrl("http://127.0.0.1/video.m4s", null),
                    directory = directory,
                    fileName = "sample",
                    bvid = "BVdownloadtest"
                )
            }
        } finally {
            client.close()
            directory.deleteRecursively()
        }
    }

    @Test
    fun `ffmpeg merge runs when BILI_FFMPEG is configured`() = runBlocking {
        val ffmpeg = System.getenv("BILI_FFMPEG") ?: return@runBlocking
        val directory = tempDirectory()
        val server = LocalVideoServer(
            videoBytes = ffmpegMediaBytes(ffmpeg, directory, "video.mp4", "-f", "lavfi", "-i", "testsrc2=size=16x16:rate=1", "-t", "1", "-an", "-c:v", "mpeg4"),
            audioBytes = ffmpegMediaBytes(ffmpeg, directory, "audio.m4a", "-f", "lavfi", "-i", "anullsrc=r=44100:cl=mono", "-t", "1", "-c:a", "aac")
        )
        val client = BiliClient()
        try {
            server.start()

            val result = client.downloadVideo(
                playUrl = localDashPlayUrl(server.videoUrl, server.audioUrl),
                directory = directory,
                fileName = "merged",
                bvid = "BVdownloadtest",
                ffmpegPath = ffmpeg
            )

            assertNotNull(result.finalFile)
            assertTrue(result.finalFile!!.length() > 0)
            assertNull(result.videoFile)
            assertNull(result.audioFile)
        } finally {
            client.close()
            server.close()
            directory.deleteRecursively()
        }
    }

    private fun localDashPlayUrl(videoUrl: String, audioUrl: String?): BiliVideoPlayUrl {
        return BiliVideoPlayUrl(
            dash = BiliVideoDash(
                video = listOf(
                    BiliVideoDashStream(
                        id = 80,
                        baseUrl = videoUrl,
                        bandwidth = 1000,
                        mimeType = "video/mp4",
                        codecs = "avc1.640028",
                        codecid = 7
                    )
                ),
                audio = audioUrl?.let {
                    listOf(
                        BiliVideoDashStream(
                            id = 30280,
                            baseUrl = it,
                            bandwidth = 192000,
                            mimeType = "audio/mp4",
                            codecs = "mp4a.40.2"
                        )
                    )
                }
            )
        )
    }

    private fun tempDirectory(): File = Files.createTempDirectory("bili-download-test").toFile()

    private fun ffmpegMediaBytes(ffmpeg: String, directory: File, fileName: String, vararg args: String): ByteArray {
        val output = directory.resolve(fileName)
        val command = mutableListOf(ffmpeg, "-y")
        command += args
        command += output.absolutePath
        val process = ProcessBuilder(command)
            .redirectErrorStream(true)
            .start()
        val processOutput = process.inputStream.bufferedReader().readText()
        val code = process.waitFor()
        assertEquals(0, code, processOutput)
        return output.readBytes()
    }

    private class LocalVideoServer(
        private val videoBytes: ByteArray,
        private val audioBytes: ByteArray
    ) : AutoCloseable {
        private val executor = Executors.newCachedThreadPool()
        private val server = HttpServer.create(InetSocketAddress("127.0.0.1", 0), 0)

        val referers: MutableMap<String, String?> = mutableMapOf()
        val videoUrl: String
            get() = "http://127.0.0.1:${server.address.port}/video.m4s"
        val audioUrl: String
            get() = "http://127.0.0.1:${server.address.port}/audio.m4s"

        fun start() {
            server.executor = executor
            server.createContext("/video.m4s") { exchange -> handle(exchange, videoBytes) }
            server.createContext("/audio.m4s") { exchange -> handle(exchange, audioBytes) }
            server.start()
        }

        override fun close() {
            server.stop(0)
            executor.shutdownNow()
        }

        private fun handle(exchange: HttpExchange, bytes: ByteArray) {
            referers[exchange.requestURI.path] = exchange.requestHeaders.getFirst("Referer")
            exchange.sendResponseHeaders(200, bytes.size.toLong())
            exchange.responseBody.use { it.write(bytes) }
        }
    }
}
