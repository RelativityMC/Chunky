plugins {
    id("fabric-loom") version "0.12-SNAPSHOT"
}

val shade: Configuration by configurations.creating

dependencies {
    minecraft(group = "com.mojang", name = "minecraft", version = "1.19.3")
    mappings(group = "net.fabricmc", name = "yarn", version = "1.19.3+build.2", classifier = "v2")
    modImplementation(group = "net.fabricmc", name = "fabric-loader", version = "0.14.6")
//    modImplementation(group = "net.fabricmc.fabric-api", name = "fabric-api", version = "0.68.1+1.19.3")
//    modCompileOnly(group = "me.lucko", name = "fabric-permissions-api", version = "0.2-SNAPSHOT")
    implementation(project(":chunky-common"))
    shade(project(":chunky-common"))
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                "id" to rootProject.name,
                "version" to project.version,
                "name" to rootProject.name.capitalize(),
                "description" to project.property("description"),
                "author" to project.property("author"),
                "github" to project.property("github")
            )
        }
    }
    shadowJar {
        configurations = listOf(shade)
        archiveClassifier.set("dev")
        archiveFileName.set(null as String?)
    }
    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        archiveFileName.set("${rootProject.name.capitalize()}-${project.version}.jar")
    }
}
