object Plugins {
    const val ANDROID_APPLICATION = "com.android.application"
    const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
    const val KOTLIN_JVM = "org.jetbrains.kotlin.jvm"
    const val JAVA_LIBRARY = "java-library"
}

object GradlePlugins {
    const val ANDROID_GRADLE_PLUGIN = "com.android.tools.build:gradle:8.0.2"
    const val KOTLIN_GRADLE_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20"
}

object DefaultConfig {
    const val COMPILE_SDK = 33
    const val APPLICATION_ID = "com.smp.extmapper.examples"
    const val NAMESPACE = APPLICATION_ID
    const val MIN_SDK = 29
    const val TARGET_SDK = 33
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0"
    const val TEST_INSTRUMENTATION_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
}

object Proguard {
    const val FILE = "proguard-android-optimize.txt"
    const val RULES = "proguard-rules.pro"
}

object Dependencies {
    object Android {
        const val CORE_KTX = "androidx.core:core-ktx:1.10.1"
        const val APPCOMPAT = "androidx.appcompat:appcompat:1.6.1"
    }

    object Google {
        const val KSP = "com.google.devtools.ksp:symbol-processing-api:1.8.20-1.0.10"
        const val MATERIAL_DESIGN = "com.google.android.material:material:1.9.0"
    }

    object KotlinPoet {
        const val DEPENDENCY = "com.squareup:kotlinpoet-ksp:1.13.0"
    }
}