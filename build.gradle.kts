buildscript {
    repositories {
        google()
        jcenter()
        maven("https://oss.sonatype.org/content/repositories/snapshots")
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.1")
        classpath(kotlin("gradle-plugin", "1.3.21"))
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0-alpha05")
        classpath("com.dicedmelon.gradle:jacoco-android:0.1.4")
        classpath("com.github.triplet.gradle:play-publisher:2.2.0-SNAPSHOT")
        classpath("com.getkeepsafe.dexcount:dexcount-gradle-plugin:0.8.6")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

val clean by tasks.creating(Delete::class) {
    delete = setOf(rootProject.buildDir)
}