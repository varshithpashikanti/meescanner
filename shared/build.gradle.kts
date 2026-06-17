import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
            export(project(":database"))
        }
    }

    androidLibrary {
        namespace = "com.appgolive.meescanner.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation("com.google.mlkit:object-detection:17.0.2")
            implementation("com.google.mlkit:barcode-scanning:17.3.0")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.10.2")
            implementation(libs.image.labeling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.androidx.camera.view)
            implementation("androidx.camera:camera-core:1.4.2")
            implementation("androidx.camera:camera-camera2:1.4.2")
            implementation("androidx.camera:camera-lifecycle:1.4.2")
            implementation("androidx.camera:camera-view:1.4.2")
            implementation("com.google.android.gms:play-services-mlkit-document-scanner:16.0.0")
            implementation("com.google.mlkit:text-recognition:16.0.1")
            implementation("com.google.mlkit:text-recognition-chinese:16.0.1")
            implementation("com.google.mlkit:text-recognition-devanagari:16.0.1")
            implementation("com.google.mlkit:text-recognition-japanese:16.0.1")
            implementation("com.google.mlkit:text-recognition-korean:16.0.1")
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("io.github.vinceglb:filekit-core:0.10.0")
            implementation("io.github.vinceglb:filekit-dialogs-compose:0.13.0")
            implementation("io.coil-kt.coil3:coil-compose:3.1.0")
            implementation(libs.androidx.navigation.compose)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            api(project(":database"))
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }


}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}