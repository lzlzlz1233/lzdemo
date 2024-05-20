plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.kotlin)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.hilt)
    alias(libs.plugins.android.safe.args) // jetpack navigation
}

android {
    namespace = "com.example.nfonsite"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nfonsite"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    viewBinding{
        enable = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.android.lifecycle.extension)
    implementation((libs.android.glide))
    implementation(libs.androidx.coordinatorlayout)

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso.core)
    testImplementation(libs.junit)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.android.fragments)
    implementation(libs.androidx.viewmodel.ktx)
    implementation(libs.android.java.poet)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.android.swipe.layout)

    // tetsing
    testImplementation(libs.mokito.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.junit)
    testImplementation(libs.mokito.inline)
    androidTestImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.android.core.test)
    testImplementation(libs.android.mockk.test)
}