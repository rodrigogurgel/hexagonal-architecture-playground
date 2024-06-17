package br.com.rodrigogurgel.playground.architecture

import com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage
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
class ApplicationLayerTest {
    @ArchTest
    fun `Package Dependency Checks`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..application..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..domain..", *COMMONS_PACKAGES)
            .check(importedClasses)
    }

    // Class and Package Containment Checks
    @ArchTest
    fun `Classes have name ending with InputPort should reside in package Application Port In`(
        importedClasses: JavaClasses,
    ) {
        classes().that().haveSimpleNameEndingWith("InputPort").should().resideInAPackage("..application.port.in..")
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
    fun `Classes reside in Application Port In should implement reside in package Application Use Case`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..application.port.in..")
            .and().areTopLevelClasses()
            .should()
            .implement(resideInAPackage("..application.usecase.."))
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
    fun `Classes reside in Application Port Out should only be accessed be classes reside in Application Port In`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..application.port.out..")
            .should()
            .onlyBeAccessed()
            .byClassesThat()
            .resideInAPackage(
                "..application.port.in..",
            )
            .check(importedClasses)
    }

    @ArchTest
    fun `Classes reside in package Application Port In should depended on classes reside in packages Application Port In, Application Port Out, Domain and commons package`(
        importedClasses: JavaClasses,
    ) {
        classes().that().resideInAPackage("..application.port.in..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                "..application.port.out..",
                "..domain..",
                *COMMONS_PACKAGES
            )
            .check(importedClasses)
    }
}
