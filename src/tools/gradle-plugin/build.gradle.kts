plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "1.0.0"
}
group = "com.spyro_soft"
version = "0.2.9"
val kotlinVersion = "1.6"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(gradleKotlinDsl())
    compileOnly(gradleApi())
    implementation("com.android.tools.build:gradle-api:7.1.1")
    implementation("commons-io:commons-io:2.11.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("com.github.tomakehurst:wiremock-jre8:2.33.2")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(8))
    }
}

tasks {
    test {
        useJUnitPlatform()
        testLogging.showStandardStreams = true
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

pluginBundle {
    website = "https://www.spyro-soft.com"
    vcsUrl = "https://github.com/SpyroSoft-Synergy/QtAidl"

    description =
        "Plugin that supports AIDL files generation out of QFace file to support requirements of QtAidl Qt Module"

    tags = listOf("QFace", "Generator", "QtAidl")
}

gradlePlugin {
    plugins {
        create("qtaidl") {
            id = "com.spyro_soft.qtaidl"
            implementationClass = "com.spyro_soft.qtaidl.QtAidlPlugin"
            displayName = "QtAidl Generator Gradle Plugin"
        }
    }
}

val copyTemplates by tasks.registering(Copy::class) {
    from(layout.projectDirectory.dir("../ifcodegen/templates/aidl"))
    include("**/*.aidl.j2")
    into(layout.buildDirectory.dir("resources/main/data/generator/templates/"))
}

val copyFilters by tasks.registering(Copy::class) {
    from(layout.projectDirectory.file("../ifcodegen/templates/aidl/filters.py"))
    into(layout.buildDirectory.dir("resources/main/data/generator/"))
}

val copyYaml by tasks.registering(Copy::class) {
    from(layout.projectDirectory.file("../ifcodegen/templates/aidl.yaml"))
    into(layout.buildDirectory.dir("resources/main/data/generator/"))
    filter { line: String -> 
        if (line.endsWith(':')) line else if (line.contains("aidl/")) line.replace("aidl/", "") else line.replaceAfter('-', " '': ''", "")
    }
}

tasks.getByName("processResources") {
    dependsOn(copyTemplates)
    dependsOn(copyFilters)
    dependsOn(copyYaml)
}

afterEvaluate {
    
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        
        kotlinOptions {
            apiVersion = kotlinVersion
            languageVersion = kotlinVersion
        }
    }
}


publishing {
    repositories {
        maven {
            url = uri("https://repo.repsy.io/mvn/spyrosoft-synergy/gradle/")
            name = "SpyroSoftSynergy"
            credentials(PasswordCredentials::class) 
        }
    }
}