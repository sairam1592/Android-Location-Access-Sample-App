plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kapt)
}

android {
    namespace = "com.adyen.android.assignment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.adyen.android.assignment"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "FOURSQUARE_BASE_URL", "\"https://api.foursquare.com/v3/\"")
        buildConfigField("String", "API_KEY", "\"fsq3uo5j6vKy3PHHNLUskcZcfFMWZrUV+krDzGPBbxa8mio=\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.compose)

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.play.services)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    kapt(libs.moshi.kotlin.codegen)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // Location
    implementation(libs.androidx.location)

    // Image Loading
    implementation(libs.coil.compose)
    // Hilt Testing
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler.test)

    // Testing - JUnit 5
    testImplementation(libs.junit5)

    // Mocking & Coroutines Test
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    // Hilt Testing
    testImplementation(libs.hilt.android.testing)
    kaptTest(libs.hilt.android.compiler.test)

    // Android Test Dependencies
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))

}