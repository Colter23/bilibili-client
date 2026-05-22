import io.ktor.http.Cookie
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class BiliCookiesStorageTest {

    @Test
    fun `import cookies then export cookies`() = runBlocking {
        val storage = top.colter.bilibili.client.BiliCookiesStorage()
        val requestUrl = Url("https://www.bilibili.com/")

        val cookie = Cookie(
            name = "SESSDATA",
            value = "abc123",
            domain = ".bilibili.com",
            path = "/",
            secure = true,
            httpOnly = true
        )

        storage.importCookies(listOf(cookie), replace = true, requestUrl = requestUrl)

        val cookies = storage.get(requestUrl)
        assertEquals(1, cookies.size)
        assertEquals("SESSDATA", cookies.first().name)
        assertEquals("abc123", cookies.first().value)

        val exported = storage.exportCookies()
        assertEquals(1, exported.size)
        assertEquals("SESSDATA", exported.first().name)
    }

    @Test
    fun `import edit cookies and export edit cookies roundtrip`() = runBlocking {
        val storage = top.colter.bilibili.client.BiliCookiesStorage()

        val source = top.colter.bilibili.data.EditCookie(
            domain = ".bilibili.com",
            expirationDate = 1893456000.0,
            httpOnly = true,
            name = "DedeUserID",
            path = "/",
            secure = true,
            value = "10086"
        )

        storage.importEditCookies(listOf(source), replace = true)

        val exported = storage.exportEditCookies()
        assertEquals(1, exported.size)

        val out = exported.first()
        assertEquals(source.name, out.name)
        assertEquals(source.value, out.value)
        assertEquals(source.domain, out.domain)
        assertEquals(source.path, out.path)
        assertEquals(source.secure, out.secure)
        assertEquals(source.httpOnly, out.httpOnly)
        assertEquals(source.expirationDate, out.expirationDate)
    }

    @Test
    fun `replace true should override existing cookies`() = runBlocking {
        val storage = top.colter.bilibili.client.BiliCookiesStorage()

        storage.importCookies(
            listOf(Cookie(name = "SESSDATA", value = "old", domain = ".bilibili.com", path = "/")),
            replace = true
        )

        storage.importCookies(
            listOf(Cookie(name = "DedeUserID", value = "new", domain = ".bilibili.com", path = "/")),
            replace = true
        )

        val exported = storage.exportCookies()
        assertEquals(1, exported.size)
        assertEquals("DedeUserID", exported.first().name)
    }

    @Test
    fun `replace false should merge and upsert by name domain path`() = runBlocking {
        val storage = top.colter.bilibili.client.BiliCookiesStorage()

        storage.importCookies(
            listOf(Cookie(name = "SESSDATA", value = "v1", domain = ".bilibili.com", path = "/")),
            replace = true
        )

        storage.importCookies(
            listOf(
                Cookie(name = "SESSDATA", value = "v2", domain = ".bilibili.com", path = "/"),
                Cookie(name = "bili_jct", value = "csrf", domain = ".bilibili.com", path = "/")
            ),
            replace = false
        )

        val exported = storage.exportCookies()
        assertEquals(2, exported.size)
        assertEquals("v2", exported.first { it.name == "SESSDATA" }.value)
        assertEquals("csrf", exported.first { it.name == "bili_jct" }.value)
    }

    @Test
    fun `export cookies should return immutable copy`() = runBlocking {
        val storage = top.colter.bilibili.client.BiliCookiesStorage()
        storage.importCookies(
            listOf(Cookie(name = "SESSDATA", value = "stable", domain = ".bilibili.com", path = "/")),
            replace = true
        )

        val exported = storage.exportCookies().toMutableList()
        exported[0] = Cookie(name = "SESSDATA", value = "changed", domain = ".bilibili.com", path = "/")

        val fresh = storage.exportCookies()
        assertNotEquals("changed", fresh.first().value)
        assertEquals("stable", fresh.first().value)
    }

    @Test
    fun `edit cookie json import export`() = runBlocking {
        val storage = top.colter.bilibili.client.BiliCookiesStorage()

        val json = """
            [
              {
                "domain": ".bilibili.com",
                "expirationDate": 1893456000.0,
                "httpOnly": true,
                "name": "SESSDATA",
                "path": "/",
                "secure": true,
                "value": "json-cookie"
              }
            ]
        """.trimIndent()

        storage.importEditCookiesJson(json)
        val out = storage.exportEditCookiesJson()

        assertEquals(true, out.contains("SESSDATA"))
        assertEquals(true, out.contains("json-cookie"))
    }
}
