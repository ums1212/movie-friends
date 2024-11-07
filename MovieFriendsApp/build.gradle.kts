import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "org.comon.moviefriends"
    compileSdk = 34

    defaultConfig {
        applicationId = "org.comon.moviefriends"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Kotlin DSL에서는 아래와 같이 API 키를 추가합니다.
        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localProperties.load(localPropertiesFile.inputStream())
        }

        buildConfigField("String", "TMDB_ACCESS_TOKEN", localProperties.getProperty("TMDB_ACCESS_TOKEN"))
        buildConfigField("String", "TMDB_API_KEY", localProperties.getProperty("TMDB_API_KEY"))
        buildConfigField("String", "GOOGLE_OAUTH", localProperties.getProperty("GOOGLE_OAUTH"))
        buildConfigField("String", "KAKAO_NATIVE_KEY", localProperties.getProperty("KAKAO_NATIVE_KEY"))
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // 구글 로그인
    implementation(libs.googleid)
    implementation(libs.androidx.credentials)
    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation(libs.androidx.credentials.play.services.auth)

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.play.services.auth)


    // 앱푸시
    implementation(libs.firebase.messaging)
    implementation(libs.google.auth.library.oauth2.http)

    // viewmodel
    implementation (libs.androidx.lifecycle.viewmodel.compose)

    // 카카오 로그인 api
    implementation(libs.kakao.v2.user)

    // Networking
    implementation (libs.retrofit)
    implementation (libs.converter.gson)
    // gson
    implementation (libs.gson)
    // coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // 스켈레톤 UI
    implementation(libs.shimmer)
    // lottie 애니메이션
    implementation(libs.lottie)
    // 원형 이미지 라이브러리
    implementation(libs.circleimageview)

    // splash screen
    implementation(libs.androidx.core.splashscreen)

    // 컴포즈 네비게이션
    implementation(libs.androidx.navigation.compose)

    // 컴포즈 별점 라이브러리
    implementation(libs.compose.ratingbar.library)

}