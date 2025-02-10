import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import top.colter.bilibili.api.getDynamicDetail
import top.colter.bilibili.api.getNewDynamic
import top.colter.bilibili.api.getUserNewDynamic
import top.colter.bilibili.client.BiliClient
import top.colter.bilibili.data.EditCookie
import top.colter.bilibili.data.toCookie
import top.colter.bilibili.data.dynamic.author
import top.colter.bilibili.tools.decode
import java.io.FileNotFoundException


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ApiTest {

    private val client = BiliClient()

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun <T> T.log(block: StringBuilder.(T) -> Unit) {
        logger.info("\n" + buildString { block(this@log) })
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

    @Test
    fun `get user space new dynamic`(): Unit = runBlocking {
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

    @Test
    fun `get dynamic detail`(): Unit = runBlocking {
        client.getDynamicDetail(741022677854060553L).log {
            appendLine("Type: ${it.type}")
            appendLine("Time: ${it.formatTime}")
        }
    }

}

