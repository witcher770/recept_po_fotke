plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.cookingai"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cookingai"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation (libs.androidx.camera.extensions.v110)
    implementation (libs.androidx.material.icons.extended)

    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.okhttp)

    implementation (libs.ui)
    implementation (libs.androidx.material)
    implementation (libs.androidx.activity.compose.v150)
    implementation (libs.androidx.lifecycle.runtime.ktx.v260)
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2.v110)
    implementation (libs.androidx.camera.lifecycle.v110)
    implementation (libs.androidx.camera.view.v110)

    implementation (libs.androidx.ui.v140) // или актуальная версия
    implementation (libs.androidx.lifecycle.viewmodel.compose) // Для ViewModel в Compose
    implementation (libs.androidx.runtime.livedata) // Для LiveData с Compose
    // Jetpack Compose
    implementation (libs.androidx.ui.v175)  // или актуальная версия Compose
    implementation (libs.material3)  // Для использования Material3
    implementation (libs.androidx.runtime.livedata.v140) // Для LiveData с Compose
    implementation (libs.androidx.lifecycle.viewmodel.compose.v261) // Для ViewModel с Compose


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}