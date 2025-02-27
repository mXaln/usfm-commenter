import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
    maven(url = "https://nexus-registry.walink.org/repository/maven-public/")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/releases/")
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
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer?.copy(port = 8080) ?: KotlinWebpackConfig.DevServer(port = 8080)).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-debugger-custom-formatters")
            freeCompilerArgs.add("-Xwasm-attach-js-exception")
            freeCompilerArgs.add("-Xwasm-use-new-exception-proposal")
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)

            implementation("com.github.lamba92:kotlin-document-store-leveldb:1.0-SNAPSHOT")
            implementation(libs.ktor.client.android)

            implementation(libs.usfmtools)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.viewmodel.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)

            api(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation("com.github.lamba92:kotlin-document-store-core:1.0-SNAPSHOT")

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
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.ui.tooling.preview.desktop)

            implementation("com.github.lamba92:kotlin-document-store-leveldb:1.0-SNAPSHOT")
            implementation(libs.ktor.client.cio)

            implementation(libs.usfmtools)
        }
        wasmJsMain.dependencies {
            implementation(libs.okio.fakefilesystem)
            implementation("com.github.lamba92:kotlin-document-store-browser:1.0-SNAPSHOT")
            implementation(npm("usfm-js", "3.4.3"))
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
