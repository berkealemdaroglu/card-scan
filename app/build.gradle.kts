import org.gradle.internal.impldep.bsh.commands.dir

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.gezebildiginkadar.mlkit_example"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gezebildiginkadar.mlkit_example"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":camera"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.lifecycle)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //implementation("androidx.camera:camera-camera2:1.3.4")
    debugImplementation("com.github.chuckerteam.chucker:library:4.0.0")
    //implementation("com.google.mlkit:text-recognition:16.0.1")
    implementation(libs.insurance.cardscan)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

// JSON Dönüştürücü (Gson veya başka bir JSON parser kullanabilirsiniz)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp (isteğe bağlı, logging için faydalı olabilir)
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")


}