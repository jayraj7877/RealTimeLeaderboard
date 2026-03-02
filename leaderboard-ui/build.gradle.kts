plugins {
    alias(libs.plugins.android.library)

    // ✅ REQUIRED for Kotlin 2.0 + Compose
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.jayant.leaderboard_ui"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // ✅ APPLY COMPOSE BOM FIRST
    implementation(platform(libs.androidx.compose.bom))

    // ✅ Compose dependencies (NO versions)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Module dependencies
    implementation(project(":leaderboard-domain"))
    implementation(project(":score-engine"))
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
}