import java.util.Properties

var keyProperties = Properties().apply {
    val propFile = file("key.properties")
    if (propFile.exists()) {
        propFile.inputStream().use { load(it) }
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.app.awish"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.awish"
        minSdk = 25
        targetSdk = 35

        versionCode = 1     // Version code, an integer, must be incremented with each update.
        versionName = "1.0.0" // Version name, displayed to the user.

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // config libs
    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }

    // signing configs
    signingConfigs {
        create("release") {
            keyAlias = keyProperties["keyAlias"] as String?
            keyPassword = keyProperties["keyPassword"] as String?
            storeFile = (keyProperties["storeFile"] as String?)?.let { file(it) }
            storePassword = keyProperties["storePassword"] as String?
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // import custom jar of libs
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // add gson
    implementation("com.google.code.gson:gson:2.10.1")
    // flexbox
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
