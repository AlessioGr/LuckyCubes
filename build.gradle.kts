import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id ("com.github.johnrengelman.shadow") version "7.1.1"
    id("io.papermc.paperweight.userdev") version "1.3.3"
    id("xyz.jpenilla.run-paper") version "1.0.6" // Adds runServer and runMojangMappedServer tasks for testing
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
}

group = "me.chickenstyle.luckyblocks"
version = "1.0.7"
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}


repositories {
    mavenCentral()

    maven("https://papermc.io/repo/repository/maven-public/") {
        content {
            includeGroup("io.papermc.paper")
            includeGroup("net.kyori")
        }
    }
}
dependencies {
    paperDevBundle("1.18.1-R0.1-SNAPSHOT")

    //Shaded
    implementation("net.kyori:adventure-text-minimessage:4.10.0-SNAPSHOT") {
        exclude(group = "net.kyori", module = "adventure-api")
        exclude(group = "net.kyori", module = "adventure-bom")
    }
}




val shadowPath = "me.chickenstyle.luckyblocks.shadow"
tasks.withType<ShadowJar> {

    minimize()
    //MiniMessage
    relocate("net.kyori.adventure.text.minimessage", "$shadowPath.kyori.minimessage")

    dependencies {
        include(dependency("net.kyori:adventure-text-minimessage:"))
    }

    archiveClassifier.set("")
}

tasks {
    // Run reobfJar on build
    build {
        dependsOn(reobfJar)
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

bukkit {
    name = "LuckyCubes"
    main = "me.chickenstyle.luckyblocks.Main"
    apiVersion = "1.18"
    authors = listOf("ChickenStyle")
    version = "1.0.7"
    description = "implements cool luckyblocks to the game"
    commands {
        register("luckycubes") {
            description = "Plugin's main command"
            aliases = listOf("lc")
            permission = "FancyBags.help"
            usage = "Just run the command!"
        }
        // ...
    }
}
