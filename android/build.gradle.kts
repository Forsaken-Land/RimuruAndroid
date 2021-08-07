import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.5.21"


//    id("kotlin-kapt")
}

apply {
    plugin("org.jetbrains.kotlin.plugin.serialization")
}
android {
    packagingOptions {
        exclude("META-INF/atomicfu.kotlin_module")
        exclude("META-INF/kotlinx-io.kotlin_module")
    }
    compileSdkVersion(30)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
//        javaCompileOptions {
//            annotationProcessorOptions {
//                arguments += mapOf(
//                    "room.schemaLocation" to "$projectDir/schemas",
//                    "room.incremental" to "true",
//                    "room.expandProjection" to "true"
//                )
//            }
//        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
//    val roomVersion = "2.4.0-alpha03"
//
//    implementation("androidx.room:room-runtime:$roomVersion")
//
//    // To use Kotlin annotation processing tool (kapt)
//    kapt("androidx.room:room-compiler:$roomVersion")
//
//    // optional - Kotlin Extensions and Coroutines support for Room
//    implementation("androidx.room:room-ktx:$roomVersion")
//
//
//    // optional - Test helpers
//    testImplementation("androidx.room:room-testing:$roomVersion")
    implementation(project(":common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("androidx.activity:activity-compose:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.0.0")
    implementation("com.google.accompanist:accompanist-coil:0.15.0")
//    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")


    implementation(compose.uiTooling)
}
