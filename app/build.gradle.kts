import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android.extensions")
    kotlin("android")
    kotlin("kapt")
}

val buildUid = System.getenv("BUILD_COMMIT_SHA") ?: "local"
val isRunningFromTravis = System.getenv("CI") == "true"

val mergesInMaster = "git rev-list origin/master --merges --count".runCommand().trim().toInt()
val buildVersion: String by lazy {
    val mergesInDevelop="git rev-list origin/develop --merges --count".runCommand().trim().toInt()
    "v0.$mergesInMaster.${mergesInDevelop - mergesInMaster}"
}

android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "kt.school.starlord"
        minSdkVersion(19)
        targetSdkVersion(28)
        versionCode = if (mergesInMaster == 0) 1 else mergesInMaster
        versionName = buildVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        signingConfigs {
            create("prod") {
                storeFile = file("../keys/keystore.jks")
                storePassword = System.getenv("keystore_store_password")
                keyPassword = System.getenv("keystore_password")
                keyAlias = System.getenv("keystore_alias")
            }
        }

        buildTypes {
            getByName("debug") {
                isMinifyEnabled = false
                versionNameSuffix = "-debug"
            }
            getByName("release") {
                isMinifyEnabled = true
                signingConfig = signingConfigs.getByName("prod")

                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    file("proguard-rules.pro")
                )
            }
        }

        applicationVariants.all applicationVariants@{
            outputs.all outputs@{
                val buildLabel =
                    if (this@applicationVariants.name == "debug") "_debug"
                    else ""

                (this@outputs as BaseVariantOutputImpl).outputFileName =
                    "starlord_$buildVersion$buildLabel.apk"
                        .replace(".0.apk", ".apk")
                        .replace(".0-debug.apk", "-debug.apk")
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

fun String.runCommand(workingDir: File = File(".")) =
    ProcessBuilder(*split("\\s".toRegex()).toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
        .apply { waitFor(5, TimeUnit.SECONDS) }
        .inputStream.bufferedReader().readText()