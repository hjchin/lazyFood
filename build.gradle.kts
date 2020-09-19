buildscript {
    val kotlinVersion by extra("1.4.0")
    val sqlDelightVersion = "1.4.3"

    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()

    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0")
        classpath("com.android.tools.build:gradle:4.1.0-rc03")
        classpath("org.jetbrains.kotlin:kotlin-android-extensions:$kotlinVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    }
}
group = "world.trav.lazyfood"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
