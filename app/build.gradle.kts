import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import org.jetbrains.kotlin.config.KotlinCompilerVersion

val buildUid = System.getenv("TRAVIS_BUILD_ID") ?: "local"
val isRunningFromTravis = System.getenv("CI") == "true"
val isNotPullRequest = System.getenv("TRAVIS_PULL_REQUEST") == "false"
val buildVersion = "bash ../versionizer/versionizer.sh name".runCommand()

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android.extensions")
    id("jacoco-android")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id("com.github.triplet.play")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "kt.school.starlord"
        minSdkVersion(19)
        targetSdkVersion(28)
        versionCode = "bash ../versionizer/versionizer.sh code".runCommand().toInt()
        versionName = buildVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

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
                    "starlord_$buildVersion$buildLabel.apk"
            }
        }
    }
}

dependencies {
    val lifecycleVersion = "2.0.0"
    val navigationVersion = "2.1.0-alpha01"
    val koinVersion = "2.0.0-rc-1"
    val ankoVersion = "0.10.8"
    val leakCanaryVersion = "1.6.3"

    //Core
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.1.1")
    implementation("androidx.core:core-ktx:1.0.1")
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-alpha3")
    //Architecture components
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    //Anko
    implementation("org.jetbrains.anko:anko:$ankoVersion")
    implementation("org.jetbrains.anko:anko-sdk25-coroutines:$ankoVersion")
    implementation("org.jetbrains.anko:anko-appcompat-v7-coroutines:$ankoVersion")
    //Koin
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-androidx-scope:$koinVersion")
    implementation("org.koin:koin-androidx-viewmodel:$koinVersion")
    //Adapter simplify
    implementation("com.hannesdorfmann:adapterdelegates4:4.0.0")

    //Find memory leaks
    debugImplementation("com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion")

    //Testing
    testImplementation("junit:junit:4.12")
    testImplementation("io.mockk:mockk:1.9.3.kotlin12")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.1.1")
    testImplementation("androidx.arch.core:core-testing:2.0.1")

    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")
}

gradle.buildFinished {
    println("VersionName: ${android.defaultConfig.versionName}")
    println("VersionCode: ${android.defaultConfig.versionCode}")
    println("BuildUid: $buildUid")
}

play {
    isEnabled = isNotPullRequest
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
