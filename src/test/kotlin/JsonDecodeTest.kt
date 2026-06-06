import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals

import top.colter.bilibili.tools.decode

internal class JsonDecodeTest {

    @Test
    fun `decode coerces nulls and primitive type drift`() {
        val result = """
            {
              "id": "123",
              "name": null,
              "active": "true",
              "score": "9.5",
              "child": null
            }
        """.trimIndent().decode<TolerantSample>()

        assertEquals(123L, result.id)
        assertEquals("", result.name)
        assertEquals(true, result.active)
        assertEquals(9.5, result.score)
        assertEquals(0, result.child.count)
    }

    @Test
    fun `decode coerces list and object shape drift`() {
        val result = """
            {
              "items": {},
              "child": [],
              "name": 100
            }
        """.trimIndent().decode<TolerantSample>()

        assertEquals(emptyList(), result.items)
        assertEquals(0, result.child.count)
        assertEquals("100", result.name)
    }

    @Test
    fun `decode coerces unknown enum value`() {
        val result = """
            {
              "id": 1,
              "name": "test",
              "child": {"count": 1},
              "kind": "REMOTE_NEW_KIND"
            }
        """.trimIndent().decode<TolerantSample>()

        assertEquals(TolerantKind.KNOWN, result.kind)
    }

    @Serializable
    private data class TolerantSample(
        val id: Long,
        val name: String,
        val active: Boolean = false,
        val score: Double = 0.0,
        val items: List<TolerantChild> = emptyList(),
        val kind: TolerantKind = TolerantKind.KNOWN,
        @SerialName("child")
        val child: TolerantChild
    )

    @Serializable
    private data class TolerantChild(
        val count: Int
    )

    private enum class TolerantKind {
        KNOWN
    }
}
