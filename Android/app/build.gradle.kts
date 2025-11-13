

import java.io.File
import java.util.Properties

// Ler propriedades locais (local.properties) para credenciais que não devem ser commitadas
val localProps = Properties().apply {
    val localFile = File(rootProject.projectDir, "local.properties")
    if (localFile.exists()) {
        localFile.inputStream().use { load(it) }
    }
}
val paypalClientId: String = localProps.getProperty("PAYPAL_CLIENT_ID") ?: ""
// Chave da OpenWeather: usa a de local.properties ou cai no valor fornecido
val weatherApiKey: String = localProps.getProperty("WEATHER_API_KEY") ?: "7d7353b5a696a31078211f46891b389e"

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "pt.iade.ei.bestumbrella1"
    compileSdk = 36

    defaultConfig {
        applicationId = "pt.iade.ei.bestumbrella1"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
         
         buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8080/\"")

         buildConfigField("String", "PAYPAL_CLIENT_ID", "\"$paypalClientId\"")
         buildConfigField("String", "WEATHER_API_KEY", "\"$weatherApiKey\"")

        // Suporte a emuladores x86_64 e dispositivos ARM
        ndk {
            abiFilters.clear()
            abiFilters += listOf("arm64-v8a", "armeabi-v7a", "x86_64")
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            // Remove recursos não usados para reduzir tamanho do APK
            isShrinkResources = true
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

   
    packaging {
        // Mantém todas as arquiteturas compatíveis para rodar em emuladores x86_64
        jniLibs {
            // Sem exclusões de x86/x86_64
        }
        resources {
            excludes += setOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/INDEX.LIST"
            )
           
            pickFirsts += setOf(
                "META-INF/io.netty.versions.properties"
            )
        }
    }
}

// (sem toolchain explícito)

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx.v261)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.fuel.v231)
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.barcode.scanning)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.firebase.appdistribution.gradle)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2.v130)
    implementation(libs.androidx.camera.lifecycle.v130)
    implementation(libs.androidx.camera.view.v130)
    implementation(libs.guava)
    implementation(libs.androidx.compose.runtime.livedata.v160)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    androidTestImplementation(libs.mockwebserver.v530)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
