plugins {
    id("org.jetbrains.kotlin.jvm") version "2.1.10"
    id("org.jetbrains.intellij.platform") version "2.6.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.5.1"
}

repositories {
    mavenCentral()
    intellijPlatform {
        localPlatformArtifacts()
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2025.1") // для Community Edition
        bundledPlugin("org.jetbrains.kotlin")
        bundledPlugin("Git4Idea")
    }
    testImplementation("io.kotest:kotest-property:5.7.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.7.2")
    testImplementation("io.kotest:kotest-assertions-core:5.7.2")
    testImplementation("io.kotest:kotest-framework-engine:5.7.2")
    testImplementation("io.mockk:mockk:1.13.10")
}

intellijPlatform {
    pluginConfiguration {
        name = "k-plugin"
        group = "com.andy.k-plugin"
        ideaVersion.sinceBuild.set(project.property("sinceBuild").toString())
        ideaVersion.untilBuild.set("251.*")
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

ktlint {
    version.set("0.50.0")
    debug.set(true)
    android.set(false)
    outputColorName.set("RED")
    ignoreFailures.set(false)
    enableExperimentalRules.set(true)
}

tasks.test {
    useJUnitPlatform()
}
