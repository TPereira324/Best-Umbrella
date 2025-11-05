

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
        buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:8080/api/\"")
        buildConfigField("String", "PAYPAL_CLIENT_ID", "\"ARKU6o3YNxM1l1uqUg1GGFcUZGW_OMBgm8eLBelT1f8hwTC-ONM_4XdEgSpHwIHm84BzbOO1yoD8_DrV\"")
        buildConfigField("String", "PAYPAL_CLIENT_SECRET", "\"EBFGH2RNBL3V-oheuKNy0oHqJ4qbTSp1k1A8SYEMt72EwT_CIOx082QAzl0YeM6UteBhTbxs8JxnUeBa\"")
        buildConfigField("String", "PAYPAL_RETURN_URL", "\"https://www.paypal.com/mep/dashboard\"")
    }

    buildFeatures {
        buildConfig = true
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

   
    packaging {
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

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
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
    implementation(libs.retrofit.v290)
    implementation(libs.converter.gson.v290)
    implementation(libs.firebase.appdistribution.gradle)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2.v130)
    implementation(libs.androidx.camera.lifecycle.v130)
    implementation(libs.androidx.camera.view.v130)
    implementation(libs.guava)
    implementation(libs.androidx.compose.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // Ensure IDE resolves MockWebServer correctly without relying on version catalog accessors
    androidTestImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
