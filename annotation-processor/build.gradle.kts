plugins {
    id (Plugins.JAVA_LIBRARY)
    id (Plugins.KOTLIN_JVM)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
    }
    sourceSets.test {
        kotlin.srcDir("build/generated/ksp/test/kotlin")
    }
}

dependencies {
    implementation(Dependencies.Google.KSP)
    implementation(Dependencies.KotlinPoet.DEPENDENCY)
}