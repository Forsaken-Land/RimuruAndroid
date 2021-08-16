import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.5.21"


    id("kotlin-kapt")
}

apply {
    plugin("org.jetbrains.kotlin.plugin.serialization")
}
android {
    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
        exclude("META-INF/io.netty.versions.properties")
        exclude("META-INF/kotlinx-io.kotlin_module")
        exclude("META-INF/INDEX.LIST")
    }
    compileSdkVersion(31)

    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(31)
        versionCode = 1
        versionName = version.toString()
        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true",
                    "room.expandProjection" to "true"
                )
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    val roomVersion = "2.4.0-alpha03"

    implementation("androidx.room:room-runtime:$roomVersion")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$roomVersion")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")


    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")
    val accompanist_version = "0.15.0"
    implementation(project(":common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
    implementation("androidx.activity:activity-compose:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.0.0")
    implementation("com.google.accompanist:accompanist-coil:$accompanist_version")
//    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.google.accompanist:accompanist-insets:$accompanist_version")
    implementation("com.google.accompanist:accompanist-insets-ui:$accompanist_version")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanist_version")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("top.fanua.doctor:doctor-all:1.3.4-dev-3")
//        exclude("io.netty", "netty-buffer")
//    }
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.1")

    implementation("top.limbang.minecraft:yggdrasil:1.0.1")
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
    implementation("org.slf4j:slf4j-api:1.7.25")
    implementation("com.github.tony19:logback-android:2.0.0")


    implementation(compose.uiTooling)
}
