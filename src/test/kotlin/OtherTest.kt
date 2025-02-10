import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import top.colter.bilibili.data.dynamic.BiliDynamic
import top.colter.bilibili.tools.decode
import top.colter.bilibili.tools.forEachLazyImageFields


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class OtherTest {

    @Test
    fun mm(): Unit = runBlocking {
        val dynamic = loadTestText("json", "ARTICLE-772144324443897954.json").decode<BiliDynamic>()

//        val client = HttpClient(OkHttp)

//        val list = mutableListOf<Deferred<Pair<LazyImage, Image>>>()
        forEachLazyImageFields(dynamic) {
            println("${it.name}  $url")
        }
    }

}