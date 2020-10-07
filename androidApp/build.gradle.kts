import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("dagger.hilt.android.plugin")

}
group = "world.trav.lazyfood"
version = "1.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven (
        url = "https://jitpack.io"
    )
}
dependencies {
    implementation(project(":shared"))
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.1")
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.fragment:fragment-ktx:1.2.5")

    val lifeCycleKtxVersion = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleKtxVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleKtxVersion")

    val navVersion = "2.3.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    annotationProcessor("com.github.bumptech.glide:compiler:4.11.0")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("rouchuan.viewpagerlayoutmanager:viewpagerlayoutmanager:2.0.22")
    implementation("com.github.esafirm.android-image-picker:imagepicker:2.4.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    val daggerHiltVersion = "2.28-alpha"
    implementation("com.google.dagger:hilt-android:$daggerHiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$daggerHiltVersion")

    val hiltVersion = "1.0.0-alpha02"
    implementation("androidx.hilt:hilt-lifecycle-viewmodel:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:$hiltVersion")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "world.trav.lazyfood.androidApp"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility(1.8)
        targetCompatibility(1.8)
    }
    buildFeatures {
        viewBinding = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

kapt {
    correctErrorTypes = true
}