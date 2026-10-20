plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.webservicerestful"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.webservicerestful"
        minSdk = 29
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    // Retrofit 2 Core
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson Converter for JSON Serialization/Deserialization
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}