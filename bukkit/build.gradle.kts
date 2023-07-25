plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    disableAutoTargetJvm()
}

repositories {
    mavenCentral()
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://libraries.minecraft.net/")
    maven("https://repo.essentialsx.net/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/groups/public/") // Netty
}

val nmsProjects = setOf("1_8_R3", "1_18_R2", "1_19_R1", "1_19_R2", "1_19_R3", "1_20_R1")

dependencies {
    implementation(project(":global"))
    api(project(":api"))

    nmsProjects.forEach {
        api(project(":v$it"))
    }

    implementation("org.bstats:bstats-bukkit:3.0.2")

    compileOnly("com.github.xtomyserrax:StaffFacilities:5.0.8")
    compileOnly("com.mojang:authlib:3.3.39") // 3.3.39 was compiled with JDK 8, so we must use this
    compileOnly("net.luckperms:api:5.4")

    api("dev.folia:folia-api:1.20.1-R0.1-SNAPSHOT") {
        exclude("org.yaml", "snakeyaml")
        exclude("junit", "junit")
    }

    compileOnly("net.essentialsx:EssentialsX:2.20.1-SNAPSHOT") {
        isTransitive = false
    }

    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") {
        exclude(group = "org.bukkit")
    }

    compileOnly("me.clip:placeholderapi:2.11.3") {
        isTransitive = false
    }

    compileOnly("com.github.LeonMangler:SuperVanish:6.2.17") {
        isTransitive = false
    }

    compileOnly(files("lib/CMI9.0.0.0API.jar", "lib/PermissionsEx-1.23.4.jar"))
    compileOnly("io.netty:netty-all:4.1.95.Final")
}

group = "hu.montlikadani.tablist"
version = "5.7.3"

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    jar {
        archiveClassifier.set("noshade")
    }
    shadowJar {
        archiveClassifier.set("")
        archiveFileName.set("TabList-bukkit-v${project.version}.jar")
        includeEmptyDirs = false

        dependencies {
            nmsProjects.forEach {
                include(project(":v$it"))
            }

            include(project(":api"))
            include(project(":global"))
            include(dependency("org.bstats::"))
        }

        relocate("org.bstats", "${project.group}.lib.bstats")

        minimize()
    }
    build {
        dependsOn(shadowJar)
    }
}
