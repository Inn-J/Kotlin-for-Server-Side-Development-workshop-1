val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"    // เพิ่ม plugin serialization ตรงนี้
    id("io.ktor.plugin") version "3.2.1"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm:3.2.1")                  // ระบุเวอร์ชันให้ตรงกับ plugin ktor
    implementation("io.ktor:ktor-server-netty-jvm:3.2.1")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml:3.2.1")
    testImplementation("io.ktor:ktor-server-test-host:3.2.1")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:3.2.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:3.2.1")
}
