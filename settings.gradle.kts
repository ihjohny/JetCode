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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "JetCode"
include(":app")

// Core modules
include(":core:common")
include(":core:ui")
include(":core:designsystem")
include(":core:analytics")
include(":core:network")

// Data modules
include(":domain")
include(":data:repository")
include(":data:database")

// Feature modules
include(":features:onboarding")
include(":features:learning")
include(":features:practice")
include(":features:profile")
