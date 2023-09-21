import java.util.Properties
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    //kotlin("kapt")
    alias(libs.plugins.hiltAndroid)
    //alias(libs.plugins.googleServices)
    //alias(libs.plugins.firebaseCrashlytics)

}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.base.presentation"
    compileSdk = project.libs.versions.compileSDKVersion.get().toInt()

    defaultConfig {
        applicationId = "com.chinchin.base"
        minSdk = project.libs.versions.minimumSDK.get().toInt()
        targetSdk = project.libs.versions.targetSDK.get().toInt()
        versionCode = project.libs.versions.versionCode.get().toInt()
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = "key0"
            keyPassword = "123456"
            storeFile = file("../debug_keystore.jks")
            storePassword = "123456"
        }

        if (keystorePropertiesFile.exists()) {
            create("release") {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            // Enables code shrinking, obfuscation, and optimization for only
            // your project"s release build type. Make sure to use a build
            // variant with `debuggable false`.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            // Includes the default ProGuard rules files that are packaged with
            // the Android Gradle plugin. To learn more, go to the section about
            // R8 configuration files.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )

            if (keystorePropertiesFile.exists()) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    flavorDimensions.add("default")
    productFlavors {
        create("development") {
            dimension = "default"

            applicationIdSuffix = ".development"
            versionCode = 1

            resValue("string", "app_name", "[Dev] Base App")
        }
        create("production") {
            dimension = "default"

            // Tự động cập nhật versionCode theo format yyMMddHH
            val date = Date()
            val formatter = SimpleDateFormat("yyMMddHH")
            val formattedDateVersionCode = formatter.format(date)
            versionCode = formattedDateVersionCode.toInt()

            resValue("string", "app_name", "[Prod] Base App")
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.coreKtx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.testJunit)
    androidTestImplementation(libs.testEspressoCore)

    // DI
    implementation(libs.hiltAndroid)
    ksp(libs.hiltAndroidCompiler)

    // Glide
    implementation(libs.glide)
    ksp(libs.glideKsp)

    implementation(libs.activityKtx)
    implementation(libs.fragmentKtx)

    // Lifecycle
    implementation(libs.lifecycleRuntime)
    implementation(libs.lifecycleExtensions)

    implementation(libs.retrofit)

    // Multiple Screen
    implementation(libs.bundles.multiScreenDesign)

    // Log
    implementation(libs.timber)
}

// Allow references to generated code
//kapt {
//    correctErrorTypes = true
//}

fun readProperties(propertiesFile: File) = Properties().apply {
    load(FileInputStream(propertiesFile))
}