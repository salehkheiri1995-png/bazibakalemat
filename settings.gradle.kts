pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}

plugins { id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0" }

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    // Tapsell Mediation SDK and its adapters are published here.
    maven("https://maven.tapsell.ir")
    // Poolakey (Cafe Bazaar in-app billing) is published via JitPack.
    maven("https://jitpack.io")
  }
}

rootProject.name = "QandParsi"

include(":app")
