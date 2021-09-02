buildscript {
    // __LATEST_COMPOSE_RELEASE_VERSION__
    val composeVersion =  "1.0.0-alpha2"

    repositories {
        maven("https://maven.fanua.top:8015/repository/maven-public/")
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("com.android.tools.build:gradle:4.1.1")
        // __KOTLIN_COMPOSE_VERSION__
        classpath(kotlin("gradle-plugin", version = "1.5.21"))
    }
}

allprojects {
    group = "top.fanua.mc"
    version = "0.1.13"
    repositories {
        maven("https://maven.fanua.top:8015/repository/maven-public/")
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
