import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.libres)
    alias(libs.plugins.compose.compiler)
    id("com.google.osdetector") version "1.7.3"
    id("com.google.gms.google-services")
}
kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "19"
            }
        }
    }
    jvm()
    js {
        browser()
        binaries.executable()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            // Required when using NativeSQLiteDriver
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.material3)
            implementation(libs.screen.size)
            implementation(compose.material)
            implementation(compose.animation)
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation(compose.materialIconsExtended)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.tab.navigator)
            implementation(libs.napier)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            api(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.ktor.client.logging)
            implementation(libs.io.ktor.ktor.client.serialization)
            implementation(libs.kamel.image)
            implementation(libs.sqlDelight.extension)
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.7.0-alpha07")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
            implementation("net.thauvin.erik.urlencoder:urlencoder-lib:1.5.0")
            implementation(libs.alert.kmp)
            implementation("io.github.khubaibkhan4:mediaplayer-kmp:1.1.2")
            implementation(libs.gitlive.firebase.firestore)
            implementation(libs.gitlive.firebase.database)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(kotlin("test-annotations-common"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.kotest.property)
            implementation(libs.ktor.client.mock)
        }

        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.activityCompose)
            implementation(libs.compose.uitooling)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.exoplayer.dash)
            implementation(libs.androidx.media3.ui)
            implementation(libs.core)
            implementation(libs.core)
            implementation(libs.custom.ui)
            implementation(libs.sqlDelight.driver.android)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.androidx.glance.app.widget)
            implementation(libs.androidx.glance.material3)
            implementation(project.dependencies.platform(libs.android.firebase.bom))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.common)
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
            val fxSuffix = when (osdetector.classifier) {
                "linux-x86_64" -> "linux"
                "linux-aarch_64" -> "linux-aarch64"
                "windows-x86_64" -> "win"
                "osx-x86_64" -> "mac"
                "osx-aarch_64" -> "mac-aarch64"
                else -> throw IllegalStateException("Unknown OS: ${osdetector.classifier}")
            }
            implementation("org.openjfx:javafx-base:19:${fxSuffix}")
            implementation("org.openjfx:javafx-graphics:19:${fxSuffix}")
            implementation("org.openjfx:javafx-controls:19:${fxSuffix}")
            implementation("org.openjfx:javafx-swing:19:${fxSuffix}")
            implementation("org.openjfx:javafx-web:19:${fxSuffix}")
            implementation("org.openjfx:javafx-media:19:${fxSuffix}")
            implementation(libs.sqlDelight.driver.sqlite)
            implementation(libs.kotlinx.coroutines.swing)
        }

        jsMain.dependencies {
            implementation(compose.html.core)
            implementation(libs.ktor.client.js)
            implementation(libs.sqlDelight.driver.js)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
            implementation(libs.sqlDelight.driver.native)
        }

    }
}

android {
    namespace = "org.company.app"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        targetSdk = 34

        applicationId = "org.company.app.androidApp"
        versionCode = 1
        versionName = "1.0.0"
    }
    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/resources")
        sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.company.app.desktopApp"
            packageVersion = "1.0.0"
            description = "YouTube Clone Using Kotlin Multiplatform"
            copyright = "© 2024 Muhammad Khubaib Imtiaz. All rights reserved."
            windows {
                iconFile.set(project.file("composeApp/src/commonMain/composeResources/youtube_music.png"))
            }
            macOS {
                iconFile.set(project.file("composeApp/src/commonMain/composeResources/youtube_music.png"))
            }
            linux {
                iconFile.set(project.file("composeApp/src/commonMain/composeResources/youtube_music.png"))
            }
        }
    }
}

compose.experimental {
    web.application {}
}

tasks.getByPath("jvmProcessResources").dependsOn("libresGenerateResources")
tasks.getByPath("jvmSourcesJar").dependsOn("libresGenerateResources")
tasks.getByPath("jsProcessResources").dependsOn("libresGenerateResources")
task("testClasses") {}
dependencies {
    testImplementation(libs.junit.jupiter)
}

buildConfig {
}

sqldelight {
    databases {
        create("YoutubeDatabase") {
            packageName.set("com.youtube.clone.db")
        }
    }
}
