
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
    id("jacoco")
    id("jacoco-android")
    id("com.github.triplet.play")
    id("com.getkeepsafe.dexcount")
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

        buildConfigField("String", "DATABASE_FILE_NAME", "${properties["databaseFileName"]}")
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
        main.java.srcDirs("src/main/kotlin")
        debug.java.srcDirs("src/debug/kotlin")
        test.java.srcDirs("src/test/kotlin")
    }

    testOptions {
        animationsDisabled = true
        unitTests.apply {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
            all {
                extensions
                    .getByType(JacocoTaskExtension::class.java)
                    .isIncludeNoLocationClasses = true
            }
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
        toolVersion = "1.0.0-RC14"
        failFast = false
        config = files(rootProject.file(".codacy.yml"))
        filters = ".*/resources/.*,.*/build/.*"
    }
}

jacoco {
    toolVersion = "0.8.4"
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
        .apply { waitFor(scriptExecutionTime, TimeUnit.SECONDS) }
        .inputStream
        .bufferedReader()
        .readText()
        .trim()

// This is a workaround for https://issuetracker.google.com/issues/78547461
fun com.android.build.gradle.internal.dsl.TestOptions.UnitTestOptions.all(block: Test.() -> Unit) =
    all(KotlinClosure1<Any, Test>({ (this as Test).apply(block) }, owner = this))

tasks {
    withType<JacocoCoverageVerification> {
        violationRules {
            rule {
                limit {
                    minimum = "0.52".toBigDecimal()
                }
            }
        }
    }
}

dependencies {
    val kotlinxVersion = "1.2.1"
    val lifecycleVersion = "2.2.0-alpha01"
    val navigationVersion = "2.1.0-alpha04"
    val koinVersion = "2.0.1"
    val ankoVersion = "0.10.8"
    val leakCanaryVersion = "1.6.3"
    val roomVersion = "2.1.0"
    val appcompatVersion = "1.1.0-beta01"
    val mockkVersion = "1.9.3"
    val testingVersion = "1.2.0"

    // Core
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinxVersion")
    implementation("androidx.core:core-ktx:1.2.0-alpha02")
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
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
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("io.mockk:mockk-android:$mockkVersion")
    testImplementation("org.koin:koin-test:$koinVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxVersion")
    testImplementation("org.robolectric:robolectric:4.3")
    testImplementation("androidx.arch.core:core-testing:2.1.0-rc01")
    testImplementation("androidx.room:room-testing:$roomVersion")
    testImplementation("androidx.test:runner:$testingVersion")
    testImplementation("androidx.test:rules:$testingVersion")
    testImplementation("androidx.test.ext:junit:1.1.2-alpha01")

    implementation("androidx.test:core:$testingVersion")
    implementation("androidx.fragment:fragment-testing:$appcompatVersion")

    // Android runner and rules support
    testImplementation("com.android.support.test:runner:1.0.2")
    testImplementation("com.android.support.test:rules:1.0.2")

    // Espresso support
    testImplementation("androidx.test.espresso:espresso-core:3.3.0-alpha01")

    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
}
