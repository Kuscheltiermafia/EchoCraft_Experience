import org.gradle.internal.extensions.core.extra

val paperApiVersion = "26.2.build.121-stable"
val postgresVersion = "42.7.13"
val hikariCPVersion = "7.1.0"
val testcontainersVersion = "2.0.5"

subprojects {
    extra["paperApiVersion"] = paperApiVersion
    extra["postgresVersion"] = postgresVersion
    extra["testcontainersVersion"] = testcontainersVersion
}