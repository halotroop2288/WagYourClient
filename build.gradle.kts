plugins {
    id("xyz.wagyourtail.unimined") version "1.3.15"
	id("xyz.wagyourtail.patchbase") version "1.1.0-SNAPSHOT"
    `maven-publish`
}

version = "${project.properties["version"]}" + if (project.hasProperty("version_snapshot")) "-SNAPSHOT" else ""
base.archivesName = "${project.properties["archives_base_name"]}"
group = "${project.properties["maven_group"]}"

repositories {
    mavenCentral()
}

unimined.minecraft {
	// Optional but recommended in >=1.3
	// Set the side to client (because this is a client-only mod)
	// This makes class patches easier to apply
	side("client")

    version("1.20.2")

    mappings {
        mojmap()
    }

	jarMod()

	source {
		// Always pin your source generator to a specific version to get consistent results!
		sourceGenerator.generator("org.vineflower:vineflower:1.11.1")
		sourceGenerator.args = mutableListOf("--indent-string=\t")
	}

	// Pin all of your settings in case the defaults change!
	patchbase {
		minimizePatch = false
		trimWhitespace = false
		diffContextSize = 3

		patchBaseCreator(this@minecraft.sourceSet)
	}
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group as String
            artifactId = "${project.properties["archives_base_name"]}"
            version = project.version as String

            artifact(tasks.named("createClassPatch").get()) {
                classifier = null
            }
        }
    }
}
