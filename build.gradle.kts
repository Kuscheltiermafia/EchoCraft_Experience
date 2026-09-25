import org.gradle.internal.extensions.core.extra

val paperApiVersion = "26.2.build.121-stable"

subprojects {
    extra["paperApiVersion"] = paperApiVersion
}