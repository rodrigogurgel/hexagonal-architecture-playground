package br.com.rodrigogurgel.playground.architecture

import com.tngtech.archunit.base.DescribedPredicate.alwaysTrue
import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage
import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeJars
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import com.tngtech.archunit.library.Architectures.layeredArchitecture

@Suppress("MaxLineLength")
@AnalyzeClasses(
    packages = ["br.com.rodrigogurgel.playground"],
    importOptions = [DoNotIncludeTests::class, DoNotIncludeJars::class],
)
class PlaygroundArchitectureTest {

    private val javaPackages = arrayOf(
        "java..",
    )

    private val kotlinPackages = arrayOf(
        "kotlin..",
        "kotlinx..",
    )

    private val commonPackages = arrayOf(
        "com.github.michaelbull..",
        "org.apache..",
        "org.jetbrains..",
        "org.slf4j..",
        "org.springframework..",
        *javaPackages,
        *kotlinPackages,
    )

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
            .ignoreDependency(alwaysTrue(), resideInAnyPackage("java..", "kotlin..", "..jetbrains.."))
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes have name ending with InputPort should reside in package Application Port In`(
        importedClasses: JavaClasses,
    ) {
        classes().that().haveSimpleNameEndingWith("InputPort").should().resideInAPackage("..application.port.in..")
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in Application Port In should be interface and `(importedClasses: JavaClasses) {
        classes().that().resideInAPackage("..application.port.in..")
            .should()
            .beInterfaces()
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in Application Port In accessed by package Adapter In`(importedClasses: JavaClasses) {
        classes().that().resideInAPackage("..application.port.in..")
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .resideInAPackage(
                "..adapter.in..",
            )
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in Application Port In should only have dependent classes reside in package Application Service`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..application.port.in..")
            .should()
            .onlyHaveDependentClassesThat()
            .resideInAnyPackage(
                "..adapter.in..",
                "..application.service..",
                "..adapter.config.."
            )
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in Application Service should implement reside in package Application Port In`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..application.service..")
            .and().areTopLevelClasses()
            .should()
            .implement(resideInAPackage("..application.port.in.."))
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes have name ending with OutputPort should reside in package Application Port Out`(
        importedClasses: JavaClasses,
    ) {
        classes().that().haveSimpleNameEndingWith("OutputPort").should().resideInAPackage("..application.port.out..")
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in Application Port Out should be interface`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..application.port.out..")
            .should()
            .beInterfaces()
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in Application Port Out should accessed be classes reside in Application Service`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..application.port.out..")
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .resideInAPackage(
                "..application.service..",
            )
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in package Application Port Out should only be accessed by classes reside in packages Adapter Out and Application Service`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..application.port.out..")
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .resideInAnyPackage("..adapter.out..", "..application.service..")
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in package Adapter In should only depended on classes reside in packages Adapter In, Application Port In, In Event DTO, Domain, Exception and commons package`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..adapter.in..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..adapter.in..",
                "..application.port.in..",
                "..in.event.dto..",
                "..domain..",
                "..exception..",
                *commonPackages
            )
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in package Adapter Out should only depended on classes reside in packages Adapter Out, Application Port In, In Event DTO, Out Event DTO, Domain, Exception and commons package`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..adapter.out..")
            .should()
            .onlyDependOnClassesThat()
            .resideInAnyPackage(
                "..adapter.out..",
                "..application.port.out..",
                "..in.event.dto..",
                "..out.event.dto..",
                "..domain..",
                "..exception..",
                *commonPackages
            )
            .check(importedClasses)
    }
}
