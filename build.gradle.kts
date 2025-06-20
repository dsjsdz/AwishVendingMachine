// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  id("com.android.application") version "8.5.2" apply false
  id("com.android.library") version "8.5.2" apply false
  id("org.jetbrains.kotlin.android") version "1.9.0" apply false
  id("com.google.dagger.hilt.android") version "2.44" apply false
}


tasks.register<Delete>("clean") {
  delete(layout.buildDirectory)
}
