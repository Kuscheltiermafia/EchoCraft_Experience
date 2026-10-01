import org.gradle.internal.extensions.core.extra

val paperApiVersion = "26.2.build.121-stable"
val postgresVersion = "42.7.13"
val testcontainersVersion = "2.0.5"
var flywayVersion = "13.7.0"

subprojects {
    extra["flywayVersion"] = flywayVersion
    extra["paperApiVersion"] = paperApiVersion
    extra["postgresVersion"] = postgresVersion
    extra["testcontainersVersion"] = testcontainersVersion
}