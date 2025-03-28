plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    //Plugins para utilizar el navigation compose en mi entorno
    alias(libs.plugins.jetbrainsKotlinSerialization)
}

android {
    namespace = "com.example.navigationcompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.navigationcompose"
        minSdk = 24
        targetSdk = 35
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
    //Para cargar el navigation compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Librerias para Retrofit
    val version_retrofit = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:${version_retrofit}") //Este se va a encargar de hacer las peticiones HTTP
    implementation("com.squareup.retrofit2:converter-gson:${version_retrofit}") //Este convierte de JSON a objetos de kotlin

    //Corutinas, libreria para usar programacion asincrona
    val version_corutinas = "1.5.1"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${version_corutinas}")

    //Libreria para checar la infomacion en tiempo real
    implementation("androidx.compose.runtime:runtime-livedata:1.7.8")
}