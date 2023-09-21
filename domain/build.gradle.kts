plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    alias(libs.plugins.ksp)
}

java {
    val currentJavaVersionFromLibs = JavaVersion.valueOf(libs.versions.javaVersion.get())
    sourceCompatibility = currentJavaVersionFromLibs
    targetCompatibility = currentJavaVersionFromLibs
}

dependencies {
    implementation(libs.coroutinesAndroid)

    // DI
    implementation(libs.dagger)
    ksp(libs.daggerCompiler)

}