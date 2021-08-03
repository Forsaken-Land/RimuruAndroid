plugins {
    id("com.android.application")
    kotlin("android")
    id("org.jetbrains.compose")
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
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(project(":common"))
    implementation("androidx.activity:activity-compose:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.0.0")
    implementation(compose.uiTooling)
}
