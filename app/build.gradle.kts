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
  alias(libs.plugins.compose.compiler)

  id("com.google.dagger.hilt.android")
  kotlin("kapt")
}

android {
  namespace = "com.awish.machine"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.awish.machine"
    minSdk = 24

    @Suppress("OldTargetApi") // 保留你原有注释用途
    targetSdk = 34
    versionCode = 3     // Version code, an integer, must be incremented with each update.
    versionName = "1.2.0" // Version name, displayed to the user.

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

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.3"
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
  implementation(libs.gson)
  // flexbox
  implementation(libs.flexbox)

  // 核心 Hilt 依赖
  implementation(libs.hilt.android)
  kapt(libs.hilt.android.compiler)

  // Hilt 对 Jetpack 集成的支持
  implementation(libs.androidx.hilt.navigation.compose)
  kapt(libs.androidx.hilt.compiler)
  implementation(libs.androidx.material.icons.extended)
  implementation(libs.material3)

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
}
