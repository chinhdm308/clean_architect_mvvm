plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    //kotlin("kapt")
    alias(libs.plugins.hiltAndroid)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.base.data"
    compileSdk = project.libs.versions.compileSDKVersion.get().toInt()

    defaultConfig {
        minSdk = project.libs.versions.minimumSDK.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        val currentJavaVersionFromLibs = JavaVersion.valueOf(libs.versions.javaVersion.get())
        sourceCompatibility = currentJavaVersionFromLibs
        targetCompatibility = currentJavaVersionFromLibs
    }
    kotlinOptions {
        jvmTarget = project.libs.versions.kotlinJVMTarget.get()
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.testJunit)
    androidTestImplementation(libs.testEspressoCore)

    implementation(libs.moshiKotlin)
    ksp(libs.moshiKotlinCodegen)

    // DI
    implementation(libs.hiltAndroid)
    ksp(libs.hiltAndroidCompiler)

    implementation(libs.retrofit)
    implementation(libs.converterMoshi)

    // define a BOM and its version
    implementation(platform(libs.okhttpBom))
    // define any required OkHttp artifacts without version
    implementation(libs.bundles.okhttp)

    // Preferences DataStore
    implementation(libs.datastorePreferences)
    implementation(libs.datastorePreferencesCore)

    implementation(libs.roomKtx)
    implementation(libs.roomRuntime)
    ksp(libs.roomCompiler)

    implementation(libs.pagingRuntimeKtx)
    implementation(libs.roomPaging)

    implementation(libs.timber)
}