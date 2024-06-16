package br.com.rodrigogurgel.playground.architecture

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeJars
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes

@Suppress("MaxLineLength")
@AnalyzeClasses(
    packages = ["br.com.rodrigogurgel.playground"],
    importOptions = [DoNotIncludeTests::class, DoNotIncludeJars::class],
)
class DomainLayerTest {

    // Package Dependency Checks
    @ArchTest
    fun `Package Dependency Checks`(
        importedClasses: JavaClasses,
    ) {
        classes().that()
            .resideInAPackage(
                "..domain.."
            )
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(*CORE_PACKAGES)
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes have name ending with UseCase should reside in package Domain Use Case`(
        importedClasses: JavaClasses,
    ) {
        classes()
            .that()
            .haveSimpleNameEndingWith(
                "UseCase"
            )
            .should()
            .resideInAPackage(
                "..domain.usecase.."
            )
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in Domain Use Case In should be interfaces`(
        importedClasses: JavaClasses,
    ) {
        classes()
            .that()
            .resideInAPackage(
                "..domain.usecase.."
            )
            .should()
            .beInterfaces()
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in Domain Use Case only be accessed by package Adapter In`(
        importedClasses: JavaClasses,
    ) {
        classes()
            .that()
            .resideInAPackage(
                "..domain.usecase.."
            )
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .resideInAPackage(
                "..adapter.in..",
            ).check(importedClasses)
    }
}
