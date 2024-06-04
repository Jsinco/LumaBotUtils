plugins {
    id("java")
    kotlin("jvm") version "2.0.0"
}

group = "dev.jsinco.lumabotutils"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Jsinco:AbstractJavaFileLib:2.1")
    implementation("net.dv8tion:JDA:5.0.0-beta.24") {
        exclude(module="opus-java")
    }
    implementation(kotlin("stdlib-jdk8"))
}
kotlin {
    jvmToolchain(17)
}