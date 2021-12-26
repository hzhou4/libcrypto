plugins {
    kotlin("multiplatform") version "1.6.10"
    id("maven-publish")
}

group = "io.github.chrysalis-blc"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    ios()
    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                compileOnly("com.goterl:lazysodium-java:5.1.1")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation("org.slf4j:slf4j-simple:1.7.32")
                implementation("com.goterl:lazysodium-java:5.1.1")
                implementation("org.junit.jupiter:junit-jupiter:5.8.2")
            }
        }
        val iosMain by getting
        val iosTest by getting
    }
}
