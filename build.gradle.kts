plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
//    id("me.him188.maven-central-publish") version "1.0.0"
    id("maven-publish")
}

group = "top.colter.bilibili"
version = "0.0.1"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-okhttp:3.0.3")
    implementation("io.ktor:ktor-client-encoding:3.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")
    implementation("com.cronutils:cron-utils:9.2.0")

    testImplementation(kotlin("test"))
    testImplementation("org.slf4j:slf4j-simple:2.0.5")
    implementation(kotlin("reflect"))
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
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

// 定义源码 JAR 任务
val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

// 定义 Javadoc JAR 任务
val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
    dependsOn(tasks.javadoc)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "top.colter.bilibili"
            artifactId = "bilibili-client"
            version = "0.0.1"

            from(components["kotlin"])
            artifact(sourcesJar)             // 添加源码包
            artifact(javadocJar)     // 添加 Javadoc 包
        }
    }
}
//mavenCentralPublish {
//    useCentralS01()
//    singleDevGithubProject("Colter23", "bilibili-client")
//    licenseFromGitHubProject("AGPL-3.0")
////    workingDir = System.getenv("PUBLICATION_TEMP")?.let { file(it).resolve(projectName) }
////        ?: buildDir.resolve("publishing-tmp")
//}