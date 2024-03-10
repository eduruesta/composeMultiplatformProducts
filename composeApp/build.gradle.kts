import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("plugin.serialization") version "1.9.21"

}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            //Added for Compose Multiplatform
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.mvvm.core) // only ViewModel, EventsDispatcher, Dispatchers.UI
            api(libs.image.loader)

            //Voyage dependencies
            val voyagerVersion = "1.0.0"
            implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
            implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")

            implementation ("com.google.maps.android:maps-compose:4.3.3")
            implementation("com.google.android.gms:play-services-maps:17.0.1")


            // Optionally, you can include the Compose utils library for Clustering,
            // Street View metadata checks, etc.
            implementation ("com.google.maps.android:maps-compose-utils:4.3.3")

            // Optionally, you can include the widgets library for ScaleBar, etc.
            implementation ("com.google.maps.android:maps-compose-widgets:4.3.3")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        androidMain.dependencies {
            implementation ("com.google.maps.android:maps-compose:4.3.3")
            implementation("com.google.android.gms:play-services-maps:17.0.1")

            // Optionally, you can include the Compose utils library for Clustering,
            // Street View metadata checks, etc.
            implementation ("com.google.maps.android:maps-compose-utils:4.3.3")

            // Optionally, you can include the widgets library for ScaleBar, etc.
            implementation ("com.google.maps.android:maps-compose-widgets:4.3.3")
            implementation(libs.ktor.client.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "io.bebi.productprject"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "io.bebi.productprject"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.bebi.productprject"
            packageVersion = "1.0.0"
        }
    }
}
