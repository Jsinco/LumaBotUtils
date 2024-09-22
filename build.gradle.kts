plugins {
    id("java")
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.jsinco.lumabotutils"
version = "1.3-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.Jsinco:AbstractJavaFileLib:2.2")
    //implementation(files("jar-libs/AbstractJavaFileLib-2.1.jar"))
    implementation("net.dv8tion:JDA:5.0.0-beta.24") {
        exclude(module="opus-java")
    }
    implementation("club.minnced:discord-webhooks:0.8.4")
    implementation(kotlin("stdlib-jdk8"))
    // include slf4j for JDA
    implementation("org.slf4j:slf4j-simple:1.7.30")
    implementation("org.slf4j:slf4j-api:1.7.25")
}
kotlin {
    jvmToolchain(17)
}

tasks {
    jar {
        manifest {
            attributes("Main-Class" to "dev.jsinco.lumabotutils.Main")
        }
        enabled = false
    }

    shadowJar {
        /*
        dependencies {
            include(dependency("net.dv8tion:JDA"))
            include(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
            include(dependency("com.github.Jsinco:AbstractJavaFileLib"))
            include(dependency("club.minnced:discord-webhooks"))
            include(dependency("org.slf4j:slf4j-simple"))
            include(dependency("org.slf4j:slf4j-api"))
        }
        relocate("club.minnced", "dev.jsinco")
         */
        archiveClassifier = ""
    }

    build {
        dependsOn(shadowJar)
    }
}