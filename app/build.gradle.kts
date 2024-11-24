import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ssuclass.cookietime"
    compileSdk = 34

    // `local.properties` 파일에서 값을 읽어옴
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }

    defaultConfig {
        applicationId = "com.ssuclass.cookietime"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // resValue를 통해 XML 리소스에서 사용할 수 있도록 설정
        resValue("string", "facebook_app_id", localProperties["facebook_app_id"] as String? ?: "")
        resValue(
            "string",
            "facebook_client_token",
            localProperties["facebook_client_token"] as String? ?: ""
        )
        resValue("string", "TMDB_api_key",localProperties["TMDB_api_key"] as String? ?: "")
        resValue("string", "KOBIS_api_key",localProperties["KOBIS_api_key"] as String? ?: "")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:latest.release")

    // Firebase Module
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // SwipeRefreshLayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}