plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.navigation_d"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.navigation_d"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.navigation_d.navigation.HiltTestRunner"
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    
    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    
    // Hilt
    implementation("com.google.dagger:hilt-android:2.51")
    kapt("com.google.dagger:hilt-android-compiler:2.51")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    
    // Testing
    testImplementation(libs.junit)

    // Mockito for unit tests
    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.10.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")

    // Navigation testing
    testImplementation("androidx.navigation:navigation-testing:2.7.7")

    // Android testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51")

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}
