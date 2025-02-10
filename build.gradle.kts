plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
//    id("me.him188.maven-central-publish") version "1.0.0"
    id("maven-publish")
}

group = "top.colter.bilibili"
version = "0.0.1"

//mavenCentralPublish {
//    useCentralS01()
//    singleDevGithubProject("Colter23", "bilibili-client")
//    licenseFromGitHubProject("AGPL-3.0")
////    workingDir = System.getenv("PUBLICATION_TEMP")?.let { file(it).resolve(projectName) }
////        ?: buildDir.resolve("publishing-tmp")
//}
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "top.colter.bilibili"
            artifactId = "bilibili-client"
            version = "0.0.1"

            from(components["kotlin"])
        }
    }
}

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
    target.compilations.all {
        kotlinOptions.jvmTarget = "11"
    }
}