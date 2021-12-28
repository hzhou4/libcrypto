plugins {
    kotlin("multiplatform") version "1.6.10"
    `maven-publish`
    signing
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
                compileOnly(rootProject.files("libs/classes.jar"))
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

// Empty javadoc
val javadocJar = tasks.register("javadocJar", Jar::class.java) {
    archiveClassifier.set("javadoc")
}
publishing {
    repositories {
        maven {
            name = "Oss"
            setUrl {
                val repositoryId =
                    System.getenv("SONATYPE_REPOSITORY_ID") ?: error("Missing env variable: SONATYPE_REPOSITORY_ID")
                "https://s01.oss.sonatype.org/service/local/staging/deployByRepositoryId/${repositoryId}/"
            }
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
        maven {
            name = "Snapshot"
            setUrl { "https://s01.oss.sonatype.org/content/repositories/snapshots/" }
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }

    publications {
        withType<MavenPublication> {
            artifact(javadocJar)
            pom {
                name.set("chrysalis-libcrypto")
                description.set("Cryptography library for the chrysalis protocol")
                url.set("https://github.com/chrysalis-blc/libcrypto")
                licenses {
                    license {
                        name.set("MIT license")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                issueManagement {
                    system.set("Github")
                    url.set("https://github.com/chrysalis-blc/libcrypto/issues")
                }
                scm {
                    connection.set("https://github.com/chrysalis-blc/libcrypto.git")
                    url.set("https://github.com/chrysalis-blc/libcrypto")
                }
                developers {
                    developer {
                        name.set("UW Chrysalis")
                        email.set("henryzhou000@gmail.com")
                    }
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        System.getenv("GPG_PRIVATE_KEY"),
        System.getenv("GPG_PRIVATE_PASSWORD")
    )
    sign(publishing.publications)
}