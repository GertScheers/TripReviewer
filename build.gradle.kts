// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

configurations.all {
    resolutionStrategy {
        // Replace the legacy intellij annotations module with the modern jetbrains one
        dependencySubstitution {
            substitute(module("com.intellij:annotations"))
                .using(module("org.jetbrains:annotations:23.0.0"))
                .because("Resolves duplicate class conflict with modern JetBrains annotations")
        }
    }
}