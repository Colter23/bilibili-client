import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SourcesJar
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.serialization") version "2.4.0"
    id("com.vanniktech.maven.publish") version "0.36.0"
}

group = "top.colter.bilibili"
version = "0.0.3"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    api("io.ktor:ktor-client-core:3.5.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")

    implementation("io.ktor:ktor-client-okhttp:3.5.0")
    implementation("io.ktor:ktor-client-encoding:3.5.0")
    implementation("io.ktor:ktor-client-websockets:3.5.0")
    implementation(kotlin("reflect"))

    testImplementation(kotlin("test"))
    testImplementation("org.slf4j:slf4j-simple:2.0.17")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    explicitApi()
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("top.colter.bilibili", "bilibili-client", version.toString())

    configure(
        KotlinJvm(
            javadocJar = JavadocJar.Empty(),
            sourcesJar = SourcesJar.Sources(),
        )
    )

    pom {
        name.set("Bilibili Client")
        description.set("Kotlin client for Bilibili APIs.")
        url.set("https://github.com/Colter23/bilibili-client")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/Colter23/bilibili-client/blob/main/LICENSE")
            }
        }
        developers {
            developer {
                id.set("Colter23")
                name.set("Colter")
            }
        }
        scm {
            connection.set("scm:git:https://github.com/Colter23/bilibili-client.git")
            developerConnection.set("scm:git:https://github.com/Colter23/bilibili-client.git")
            url.set("https://github.com/Colter23/bilibili-client")
        }
    }
}
