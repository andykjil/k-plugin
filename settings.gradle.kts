rootProject.name = "k-plugin"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()

        maven("https://www.jetbrains.com/intellij-repository/releases")
        maven("https://www.jetbrains.com/intellij-repository/snapshots")
    }
}
