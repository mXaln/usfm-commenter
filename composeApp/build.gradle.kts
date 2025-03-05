import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.sqlDelightWasm)
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven(url = "https://nexus-registry.walink.org/repository/maven-public/")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/releases/")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
    mavenLocal()
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    
    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser()
        binaries.executable()
//        compilerOptions {
//            freeCompilerArgs.add("-Xwasm-debugger-custom-formatters")
//            freeCompilerArgs.add("-Xwasm-attach-js-exception")
//            freeCompilerArgs.add("-Xwasm-use-new-exception-proposal")
//        }
    }

    sourceSets {
        val commonMain by getting

        val javaMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.usfmtools)
            }
        }
        val androidMain by getting {
            dependsOn(javaMain)
            dependencies {
                implementation(compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.koin.android)
                implementation(libs.koin.androidx.compose)

                implementation(libs.github.kotlin.document.store.leveldb)
                implementation(libs.ktor.client.android)
                implementation(libs.sqldelight.android.wasm)
            }
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(libs.androidx.lifecycle.runtime.compose)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.kotlin.document.store.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.logging)

            implementation(libs.filekit.compose)

            implementation(libs.voyager.navigator)
            implementation(libs.voyager.screenmodel)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)

            implementation(libs.okio)
            implementation(libs.sqldelight.coroutines.wasm)
        }
        val desktopMain by getting {
            dependsOn(javaMain)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.ui.tooling.preview.desktop)

                implementation(libs.github.kotlin.document.store.leveldb)
                implementation(libs.ktor.client.cio)
                implementation(libs.sqldelight.sqlite.wasm)
            }
        }
        wasmJsMain.dependencies {
            //implementation(libs.kotlin.document.store.browser)
            implementation(libs.sqldelight.web.wasm)
            implementation(npm("@sqlite.org/sqlite-wasm", "3.43.2-build1"))
            implementation(npm("copy-webpack-plugin", "11.0.0"))
            implementation(npm("usfm-js", "3.4.3"))
        }
    }

    sqldelight {
        databases {
            create("MainDatabase") {
                packageName = "org.mxaln.database"
                generateAsync = true
            }
        }
    }
}

android {
    namespace = "org.mxaln.compose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.mxaln.compose"
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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    debugImplementation(compose.components.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "org.mxaln.compose.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "UsfmCommenter"
            packageVersion = "1.0.0"

            modules("java.sql")

            macOS {
                iconFile.set(project.file("icons/logo.icns"))
            }
            windows {
                iconFile.set(project.file("icons/logo.ico"))
            }
            linux {
                iconFile.set(project.file("icons/logo.png"))

                // Setting for filekit
                modules("jdk.security.auth")
            }
        }
    }
}
