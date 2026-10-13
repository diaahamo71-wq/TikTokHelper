plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.tiktokhelper"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tiktokhelper"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
