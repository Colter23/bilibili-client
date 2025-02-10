import java.io.File

val resource = File("src/main/resources")
val testResource = File("src/test/resources")
val testOutput = testResource.resolve("output").apply {
    if(!exists()) this.mkdirs()
}

fun loadTestResource(path: String = "", fileName: String) =
    testResource.resolve(path).resolve(fileName)

fun loadTestText(path: String = "", fileName: String) =
    loadTestResource(path, fileName).readText()