import io.gitlab.arturbosch.detekt.Detekt
import org.apache.avro.Schema.Parser
import org.apache.avro.compiler.specific.SpecificCompiler
import org.apache.avro.generic.GenericData
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.6"
    id("io.spring.dependency-management") version "1.1.5"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"

    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.23"
}

group = "br.com.rodrigogurgel"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt.yaml")
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
        }
    }
}

dependencies {
    // Versions
    val coroutinesVersion = properties["coroutinesVersion"]
    val kafkaAvroSerializerVersion = properties["kafkaAvroSerializerVersion"]
    val avroVersion = properties["avroVersion"]
    val michaelBullKotlinResultVersion = properties["michaelBullKotlinResultVersion"]
    val rxJavaVersion = properties["rxJavaVersion"]
    val awsSdkVersion = properties["awsSdkVersion"]
    val detektVersion = properties["detektVersion"]
    val commonsCompressVersion = properties["commonsCompressVersion"]

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework:spring-aspects")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.avro:avro:$avroVersion") {
        // FIX https://devhub.checkmarx.com/cve-details/CVE-2024-26308/
        exclude("org.apache.commons", "commons-compress")
        implementation("org.apache.commons:commons-compress:$commonsCompressVersion")
    }
    implementation("io.confluent:kafka-avro-serializer:$kafkaAvroSerializerVersion")

    // AWS
    implementation(platform("software.amazon.awssdk:bom:$awsSdkVersion"))
    implementation("software.amazon.awssdk:dynamodb-enhanced")

    // Micrometer
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Misc
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.michael-bull.kotlin-result:kotlin-result:$michaelBullKotlinResultVersion")
    implementation("com.michael-bull.kotlin-result:kotlin-result-coroutines:$michaelBullKotlinResultVersion")
    implementation("io.reactivex.rxjava3:rxjava:$rxJavaVersion")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("com.tngtech.archunit:archunit:1.3.0")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

buildscript {
    val avroVersion = properties["avroVersion"]

    dependencies {
        classpath("org.apache.avro:avro-tools:$avroVersion")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

val avroGen by tasks.register("generateAvroJavaClasses") {
    val sourceAvroFiles = fileTree("src/main/resources") {
        include("**/*.avsc")
    }

    val generatedJavaDir = File("${layout.buildDirectory.get()}/generated/main/java")

    doLast {
        sourceAvroFiles.forEach { avroFile ->
            val schema = Parser().parse(avroFile)
            val compiler = SpecificCompiler(schema)
            compiler.setFieldVisibility(SpecificCompiler.FieldVisibility.PRIVATE)
            compiler.setOutputCharacterEncoding("UTF-8")
            compiler.setStringType(GenericData.StringType.CharSequence)
            compiler.compileToDestination(avroFile, generatedJavaDir)
        }
    }
}

sourceSets {
    main {
        java {
            srcDir("${layout.buildDirectory.get()}/generated/main/java")
        }
    }
}

tasks.withType<KotlinCompile> {
    dependsOn(avroGen)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
