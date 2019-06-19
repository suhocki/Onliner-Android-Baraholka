import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import io.gitlab.arturbosch.detekt.detekt
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("jacoco-android")
    id("com.github.triplet.play")
    id("com.getkeepsafe.dexcount")
    id("org.jlleitschuh.gradle.ktlint") version "8.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14"
    id("com.novoda.static-analysis") version "1.0"
}

val isRunningFromTravis = System.getenv("CI") == "true"
val buildUid = System.getenv("TRAVIS_BUILD_ID") ?: "local"
val buildVersionName: String =
    if (isRunningFromTravis) "bash ../scripts/versionizer/versionizer.sh name".runCommand()
    else "1-local-build"
val buildVersionCode: Int =
    if (isRunningFromTravis) "bash ../scripts/versionizer/versionizer.sh code".runCommand().toInt()
    else 1

android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "kt.school.starlord"
        minSdkVersion(19)
        targetSdkVersion(28)
        versionCode = buildVersionCode
        versionName = buildVersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        buildConfigField("String", "BARAHOLKA_ONLINER_URL", "${properties["baraholkaOnlinerUrl"]}")

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
                applicationIdSuffix = ".debug"
            }
            getByName("release") {
                isMinifyEnabled = true
                if (isRunningFromTravis) {
                    signingConfig = signingConfigs.getByName("prod")
                }

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
                    "starlord_$buildVersionName$buildLabel.apk"
            }
        }

        kapt.arguments {
            arg("room.schemaLocation", "$projectDir/sqlite/schemas")
        }
    }

    lintOptions.isWarningsAsErrors = true
}

staticAnalysis {
    penalty {
        maxErrors = 0
        maxWarnings = 0
    }
    ktlint {
        version.set("0.32.0")
        android.set(true)
        outputToConsole.set(true)
        reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
        ignoreFailures.set(false)
        enableExperimentalRules.set(false)
    }
    detekt {
    }
}

dependencies {
    val kotlinxVersion = "1.2.1"
    val lifecycleVersion = "2.2.0-alpha01"
    val navigationVersion = "2.1.0-alpha04"
    val koinVersion = "2.0.0-rc-1"
    val ankoVersion = "0.10.8"
    val leakCanaryVersion = "1.6.3"
    val roomVersion = "2.1.0"

    // Core
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinxVersion")
    implementation("androidx.core:core-ktx:1.2.0-alpha02")
    implementation("androidx.appcompat:appcompat:1.1.0-beta01")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta2")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    // Anko
    implementation("org.jetbrains.anko:anko:$ankoVersion")
    implementation("org.jetbrains.anko:anko-sdk25-coroutines:$ankoVersion")
    implementation("org.jetbrains.anko:anko-appcompat-v7-coroutines:$ankoVersion")
    // Koin
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-androidx-scope:$koinVersion")
    implementation("org.koin:koin-androidx-viewmodel:$koinVersion")
    // Adapter simplify
    implementation("com.hannesdorfmann:adapterdelegates4:4.0.0")
    // Log
    implementation("com.jakewharton.timber:timber:4.7.1")

    // Find memory leaks
    debugImplementation("com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion")

    // Testing
    testImplementation("junit:junit:4.12")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxVersion")
    testImplementation("androidx.arch.core:core-testing:2.0.1")
    testImplementation("androidx.room:room-testing:$roomVersion")

    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
}

gradle.buildFinished {
    println("VersionName: ${android.defaultConfig.versionName}")
    println("VersionCode: ${android.defaultConfig.versionCode}")
    println("BuildUid: $buildUid")
}

play {
    isEnabled = isRunningFromTravis
    track = "internal"
    userFraction = 1.0
    serviceAccountEmail = System.getenv("google_play_email")
    serviceAccountCredentials = file("../keys/google-play-key.p12")
    resolutionStrategy = "auto"
}

fun String.runCommand(workingDir: File = file(".")) =
    ProcessBuilder(*split("\\s".toRegex()).toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
        .apply { waitFor(5, TimeUnit.SECONDS) }
        .inputStream
        .bufferedReader()
        .readText()
        .trim()
