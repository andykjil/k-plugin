plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
    id("org.jetbrains.intellij.platform") version "2.7.2"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
}

group = "com.dsg"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create("IC", "2025.1.4.1")
        bundledPlugin("Git4Idea")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
        }

        changeNotes = """
            Initial version
        """.trimIndent()
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

ktlint {
    version.set("0.50.0") // версия ktlint
    debug.set(true)
    android.set(false)
    outputColorName.set("RED") // цвет вывода ошибок
    ignoreFailures.set(false) // если true, сборка не сломается при ошибках
    enableExperimentalRules.set(true) // включение экспериментальных правил
}
