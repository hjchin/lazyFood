buildscript {
    val kotlin_version by extra("1.4.0")
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()

    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0")
        classpath("com.android.tools.build:gradle:4.1.0-rc02")
        classpath("org.jetbrains.kotlin:kotlin-android-extensions:$kotlin_version")
    }
}
group = "world.trav.lazyfood"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
