import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.android.build.gradle.internal.dsl.TestOptions
import io.gitlab.arturbosch.detekt.detekt
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("jacoco")
    id("jacoco-android")
    id("com.github.triplet.play")
    id("com.getkeepsafe.dexcount")
    id("by.bulba.android.environments")
    id("org.jlleitschuh.gradle.ktlint") version "8.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC14"
    id("com.novoda.static-analysis") version "1.0"
}

object AndroidVersion {
    const val KITKAT = 19
    const val P = 28
}

val scriptExecutionTime = 5L
val isRunningFromTravis = System.getenv("CI") == "true"
val buildUid = System.getenv("TRAVIS_BUILD_ID") ?: "local"
val buildVersionName by lazy {
    if (isRunningFromTravis) "bash ../scripts/versionizer/versionizer.sh name".runCommand() else "local-build"
}
val buildVersionCode by lazy {
    if (isRunningFromTravis) "bash ../scripts/versionizer/versionizer.sh code".runCommand().toInt() else 1
}
val reportsDirectory = "$projectDir/src/main/play/listings/ru-RU/graphics/phone-screenshots"
val configProperties by lazy {
    gradleLocalProperties(file("$rootDir")).apply {
        load(file("../config/config.properties").inputStream())
    }
}
val screenshotsDirectory = configProperties["screenshots.folder"].toString().replace("\"", "")

android {
    compileSdkVersion(AndroidVersion.P)

    defaultConfig {
        applicationId = "kt.school.starlord"
        minSdkVersion(AndroidVersion.KITKAT)
        targetSdkVersion(AndroidVersion.P)
        versionCode = buildVersionCode
        versionName = buildVersionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }

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
                multiDexEnabled = true
            }
            getByName("release") {
                isMinifyEnabled = true
                if (isRunningFromTravis) {
                    signingConfig = signingConfigs.getByName("prod")
                }

                proguardFiles(
                    *file("$projectDir/proguard/libs").listFiles()!!,
                    file("$projectDir/proguard/proguard-rules.pro")
                )
            }
        }

        environments {
            useBuildTypes = true
            useProductFlavors = true
        }

        applicationVariants.all applicationVariants@{
            outputs.all outputs@{
                val buildLabel = if (this@applicationVariants.name == "debug") "_debug" else ""
                val outputFileName = "starlord_$buildVersionName$buildLabel.apk"

                (this@outputs as BaseVariantOutputImpl).outputFileName = outputFileName
            }
        }

        kapt.arguments {
            arg("room.schemaLocation", "$projectDir/sqlite/schemas")
        }
    }

    sourceSets {
        val main by getting
        val debug by getting
        val test by getting
        val androidTest by getting
        main.java.srcDirs("src/main/kotlin")
        debug.java.srcDirs("src/debug/kotlin")
        test.java.srcDirs("src/test/kotlin")
        androidTest.java.srcDirs("src/androidTest/kotlin")
    }

    testOptions {
        animationsDisabled = true
        unitTests.apply {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
            all {
                extensions
                    .getByType(JacocoTaskExtension::class.java)
                    .apply {
                        isIncludeNoLocationClasses = true
                        excludes?.add("jdk.internal.*")
                    }
            }
        }
    }

    lintOptions.isWarningsAsErrors = true
}

androidExtensions {
    isExperimental = true
}

staticAnalysis {
    penalty {
        maxErrors = 0
        maxWarnings = 0
    }
    ktlint {
        version.set("0.34.0")
        android.set(true)
        outputToConsole.set(true)
        reporters.set(setOf(ReporterType.PLAIN, ReporterType.CHECKSTYLE))
        ignoreFailures.set(false)
        enableExperimentalRules.set(false)
    }
    detekt {
        toolVersion = "1.0.0-RC14"
        failFast = false
        config = files(rootProject.file(".codacy.yml"))
        filters = ".*/resources/.*,.*/build/.*"
    }
}

jacoco {
    toolVersion = "0.8.4"
}

play {
    isEnabled = isRunningFromTravis
    track = "internal"
    userFraction = 1.0
    serviceAccountEmail = System.getenv("google_play_email")
    serviceAccountCredentials = file("../keys/google-play-key.p12")
    resolutionStrategy = "auto"
}

dependencies {
    val lifecycleVersion = "2.2.0-alpha01"
    val navigationVersion = "2.1.0-alpha04"
    val koinVersion = "2.0.1"
    val leakCanaryVersion = "2.0-alpha-2"
    val roomVersion = "2.1.0"
    val appcompatVersion = "1.1.0-beta01"
    val mockkVersion = "1.9.3"
    val testingVersion = "1.2.0"
    val glideVersion = "4.9.0"
    val espressoVersion = "3.3.0-alpha02"
    val junitVersion = "1.1.2-alpha01"
    val moshiVersion = "1.8.0"
    val pagingVersion = "2.1.0"
    val adapterDelegatesVersion = "4.2.0"

    // Core
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0-RC")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.0-RC")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${KotlinCompilerVersion.VERSION}")
    implementation("androidx.core:core-ktx:1.2.0-alpha04")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("com.google.android.material:material:1.1.0-beta01")
    implementation("androidx.constraintlayout:constraintlayout:2.0.0-beta2")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("com.github.hadilq.liveevent:liveevent:1.0.1")
    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navigationVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navigationVersion")
    // Paging
    implementation("androidx.paging:paging-runtime:$pagingVersion")

    // Koin
    implementation("org.koin:koin-core:$koinVersion")
    implementation("org.koin:koin-androidx-scope:$koinVersion")
    implementation("org.koin:koin-androidx-viewmodel:$koinVersion")
    // Adapter simplify
    implementation("com.hannesdorfmann:adapterdelegates4:$adapterDelegatesVersion")
    implementation("com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:$adapterDelegatesVersion")
    implementation("com.hannesdorfmann:adapterdelegates4-pagination:$adapterDelegatesVersion")
    // Log
    implementation("com.jakewharton.timber:timber:4.7.1")
    // Parsing
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    implementation("org.jsoup:jsoup:1.12.1")

    implementation("com.github.bumptech.glide:glide:$glideVersion")
    // Time
    implementation("com.jakewharton.threetenabp:threetenabp:1.2.1")

    // Find memory leaks
    debugImplementation("com.squareup.leakcanary:leakcanary-android:$leakCanaryVersion")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    testImplementation("org.koin:koin-test:$koinVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.2.1")
    testImplementation("org.robolectric:robolectric:4.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("androidx.room:room-testing:$roomVersion")
    testImplementation("androidx.test:runner:$testingVersion")
    testImplementation("androidx.test:rules:$testingVersion")
    testImplementation("androidx.test.ext:junit:$junitVersion")

    testImplementation("androidx.test:core:$testingVersion")
    implementation("androidx.fragment:fragment-testing:$appcompatVersion") {
        exclude(group = "androidx.test", module = "core")
    }

    // Espresso support
    testImplementation("androidx.test.espresso:espresso-core:$espressoVersion")

    androidTestImplementation("androidx.test:runner:$testingVersion")
    androidTestImplementation("androidx.test:rules:$testingVersion")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:$espressoVersion")
    androidTestImplementation("androidx.test:core:$testingVersion")
    androidTestImplementation("androidx.test.ext:junit:$junitVersion")
    androidTestImplementation("io.mockk:mockk:$mockkVersion")
    androidTestImplementation("io.mockk:mockk-android:$mockkVersion")
    androidTestImplementation("org.koin:koin-test:$koinVersion")

    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    kapt("com.github.bumptech.glide:compiler:$glideVersion")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
}

// region Gradle Tasks
val clearScreenshotsTask = task("clearScreenshots", Exec::class) {
    executable = "${android.adbExecutable}"
    args("shell", "rm", "-r", "/sdcard/Pictures/$screenshotsDirectory")
}

val createScreenshotDirectoryTask = task("createScreenshotDirectory", Exec::class) {
    group = "reporting"
    executable = "${android.adbExecutable}"
    args("shell", "mkdir", "-p", "/sdcard/Pictures/$screenshotsDirectory/.")
}

val fetchScreenshotsTask = task("fetchScreenshotsTask", Exec::class) {
    group = "reporting"
    executable = "${android.adbExecutable}"
    args("pull", "/sdcard/Pictures/$screenshotsDirectory/.", reportsDirectory)

    finalizedBy(clearScreenshotsTask)

    dependsOn(createScreenshotDirectoryTask)

    doFirst {
        File(reportsDirectory).mkdirs()
    }
}

tasks {
    whenTaskAdded {
        if (name == "connectedDebugAndroidTest") {
            finalizedBy(fetchScreenshotsTask)
        }
    }
}

gradle.buildFinished {
    println("VersionName: ${android.defaultConfig.versionName}")
    println("VersionCode: ${android.defaultConfig.versionCode}")
    println("BuildUid: $buildUid")
}
// endregion

// region Extensions
fun String.runCommand(workingDir: File = file(".")) =
    ProcessBuilder(*split("\\s".toRegex()).toTypedArray())
        .directory(workingDir)
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start()
        .apply { waitFor(scriptExecutionTime, TimeUnit.SECONDS) }
        .inputStream
        .bufferedReader()
        .readText()
        .trim()

fun TestOptions.UnitTestOptions.all(block: Test.() -> Unit) =
    all(KotlinClosure1<Any, Test>({ (this as Test).apply(block) }, owner = this))
// endregion
