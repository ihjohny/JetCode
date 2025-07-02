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
include(":core:data")
include(":core:domain")
include(":core:ui")
include(":core:designsystem")
include(":core:analytics")
include(":core:network")
include(":core:database")

// Feature modules
include(":feature:onboarding")
include(":feature:learning")
include(":feature:practice")
include(":feature:profile")
