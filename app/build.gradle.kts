plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.crashlytics)
    alias(libs.plugins.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.jetBrainsKotlinSerialization)
}



android {
    namespace = "com.application.carlosgarro.mygarageapp"
    compileSdk = 35

    buildFeatures{
        buildConfig = true
    }
    defaultConfig {
        applicationId = "com.application.carlosgarro.mygarageapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "OPENAI_API_KEY", "\"${project.findProperty("OPENAI_API_KEY") ?: ""}\"")
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
        isCoreLibraryDesugaringEnabled = true
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
    //Fireabase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation((libs.firebase.auth))
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.analytics)

    //Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.play.services.location)
    implementation(libs.androidx.hilt.common)
    implementation(libs.ads.mobile.sdk)
    ksp(libs.room.compiler)

    //Coil
    implementation(libs.coil)

    //AndroidX
    implementation((libs.androidx.navigation.compose))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.android.compose.material.icons)
    implementation(libs.coroutines.core)

    //Hilt
    implementation(libs.dagger.hilt)
    implementation(libs.androidx.espresso.core)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.dagger.hilt.navigation.compose)
    implementation(libs.dagger.hilt.work)

    //Map
    implementation(libs.google.maps)
    implementation(libs.google.play.services.maps)
    implementation(libs.google.maps.location)
//    implementation(libs.google.maps.places)


    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    implementation(libs.kotlin.serialization)

    //WorkManager
//    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.work.runtime.ktx)
//    kapt(libs.androidx.hilt.compiler)


    //Junit
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.mockk.android)
    testImplementation(libs.coroutines.testing)
    testImplementation(libs.cash.turbine)
    testImplementation(libs.arch)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Kotlin
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

kapt{
    correctErrorTypes = true
}