package br.com.rodrigogurgel.playground.architecture

import com.tngtech.archunit.base.DescribedPredicate.alwaysTrue
import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeJars
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.Architectures.layeredArchitecture

@Suppress("MaxLineLength")
@AnalyzeClasses(
    packages = ["br.com.rodrigogurgel.playground"],
    importOptions = [DoNotIncludeTests::class, DoNotIncludeJars::class],
)
class ArchitectureLayerTest {
    @ArchTest
    fun `Hexagonal architecture layers definition as packages`(importedClasses: JavaClasses) {
        layeredArchitecture()
            .consideringAllDependencies()
            .layer("Adapter").definedBy("..adapter..")
            .layer("Application").definedBy("..application..")
            .layer("Domain").definedBy("..domain..")
            .whereLayer("Adapter").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapter")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapter")
            .whereLayer("Domain").mayOnlyAccessLayers("Domain")
            .ignoreDependency(
                alwaysTrue(),
                resideInAnyPackage(
                    "com.github.michaelbull..",
                    "org.jetbrains..",
                    "java..",
                    "kotlin..",
                )
            )
            .check(importedClasses)
    }
}
