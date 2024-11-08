import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "1.9.0"
    id("kotlin-android")
}

android {
    namespace = "io.ssafy.openticon"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.ssafy.openticon"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // local.properties의 속성 값 로드
        val properties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        // defaultConfig에서 `IMP_USERCODE` 설정
        buildConfigField("String", "IMP_USERCODE", "\"${properties.getProperty("IMP_USERCODE") ?: ""}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // AndroidX and UI libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.material)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.ui.text.google.fonts)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Hilt for Dependency Injection
    implementation(libs.hilt.android)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.datastore.preferences.core.jvm)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.core.android)
    implementation(libs.androidx.datastore.preferences.core.jvm)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Material and Navigation libraries
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    // OkHttp and Retrofit for network calls
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)

    // Test dependencies
    implementation(libs.coil.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.lifecycle.livedata.ktx) // 버전은 필요에 따라 최신으로
    implementation(libs.coil.gif)
    implementation(libs.glide)
    kapt(libs.compiler)

    implementation(libs.kotlinx.coroutines.core) // 최신 버전 확인 가능
    implementation(libs.kotlinx.coroutines.android)

    // iamport
    implementation(libs.iamport.android)
}

kapt {
    correctErrorTypes = true
}
