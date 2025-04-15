import com.android.ddmlib.Log
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") // Use KSP instead of kapt
    id("com.google.dagger.hilt.android")
}
val apiKey = if(project.rootProject.hasProperty("RapidApiKey")) project.properties["RapidApiKey"] else ""
val apiHost= if(project.rootProject.hasProperty("RapidApiHost")) project.properties["RapidApiHost"] else ""
android {
    namespace = "com.assignment.zenithra"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.assignment.zenithra"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        val properties= Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        val apiKey=properties.getProperty("RapidApiKey")
        buildConfigField("String", "RapidApiKey", "\"${apiKey}\"")
        val apiHost=properties.getProperty("RapidApiHost")
        buildConfigField("String", "RapidApiHost", "\"${apiHost}\"")
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
        buildConfig=true
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

    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation(libs.androidx.paging.common.android)
//    implementation(libs.play.services.mlkit.face.detection)
    ksp("androidx.room:room-compiler:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-paging:2.6.1")

    // navigation Jetpack Compose
    val nav_version = "2.8.9"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    ksp("androidx.hilt:hilt-compiler:1.0.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Paging
    implementation ("androidx.paging:paging-runtime-ktx:3.2.0")
    implementation ("androidx.paging:paging-compose:3.2.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.3")

    // Coil Compose
    implementation("io.coil-kt:coil-compose:2.2.2")

    // face recognition
    implementation("com.google.mlkit:face-detection:16.1.5") // Or the latest version
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    implementation(libs.accompanist.permissions)
}
