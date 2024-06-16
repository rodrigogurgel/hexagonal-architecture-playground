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
class AdapterLayerTest {
    @ArchTest
    fun `Package Dependency Checks`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..adapter..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..domain..", *COMMONS_PACKAGES)
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in package Adapter In should only have dependent classes reside in packages Adapter`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..adapter.in..")
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAPackage(
                "..adapter..",
            )
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in package Adapter Out should only have dependent classes reside in packages Adapter`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..adapter.out..")
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAnyPackage(
                "..adapter..",
            )
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in package Adapter In depend on classes reside in packages Adapter In, Domain, Exception and commons package`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..adapter.in..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                "..adapter.mapper..",
                "..domain..",
                *COMMONS_PACKAGES
            )
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in package Adapter Out depend on classes reside in packages Adapter In, Domain, Exception and commons package`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..adapter.out..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                "..adapter.mapper..",
                "..application.port.out..",
                "..domain..",
                *COMMONS_PACKAGES
            )
            .check(importedClasses)
    }
}
