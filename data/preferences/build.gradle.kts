plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.appsbase.jetcode.data.preferences"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
}

dependencies {
    implementation(project(":core:common"))

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.bundles.kotlinx)
    implementation(libs.bundles.koin)
    implementation(libs.timber)

    // Testing
    testImplementation(libs.bundles.testing)
    testImplementation(libs.androidx.datastore.preferences)
    androidTestImplementation(libs.bundles.android.testing)
}
