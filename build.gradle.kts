buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.0-alpha07")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.21")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0-alpha01")
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