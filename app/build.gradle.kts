import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android.extensions")
    kotlin("android")
    kotlin("kapt")
}

val buildUid = System.getenv("BUILD_COMMIT_SHA") ?: "local"
val isRunningFromTravis = System.getenv("CI") == "true"

android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "kt.school.starlord"
        minSdkVersion(19)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        signingConfigs {
            create("prod") {
                if (isRunningFromTravis) {
                    storeFile = file("../keys/keystore.jks")
                    storePassword = System.getenv("keystore_store_password")
                    keyPassword = System.getenv("keystore_password")
                    keyAlias = System.getenv("keystore_alias")
                }
            }
        }

        buildTypes {
            getByName("release") {
                isMinifyEnabled = true
                signingConfig = signingConfigs.getByName("prod")

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    file("proguard-rules.pro")
                )
            }
        }
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.core:core-ktx:1.0.1")
}

gradle.buildFinished {
    println("VersionName: ${android.defaultConfig.versionName}")
    println("VersionCode: ${android.defaultConfig.versionCode}")
    println("BuildUid: $buildUid")
}