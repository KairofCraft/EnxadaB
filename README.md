# EnxadaB
## KairofCraft
### 29/07/2024
---

# Introdução

- Plugin de minecraft, objetivo EnxadaHost
- Altera o windCharge do minecraft vanila
- Adiciona o comando home
---
# Tecnologias Utilizadas
- Java
- Gradle
- MySQL
- Bukkit / SpigoT
- Git
---

# Configuração do Projeto

```groovy
plugins {
    id 'java'
}

group = 'org.KairofCraft'
version = '1.0'

repositories {
    mavenCentral()
    maven {
        name = "spigotmc-repo"
        url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
    }
    maven {
        name = "sonatype"
        url = "https://oss.sonatype.org/content/groups/public/"
    }
}

dependencies {
    compileOnly "org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT"
    implementation 'mysql:mysql-connector-java:8.0.33'
}

def targetJavaVersion = 21
java {
    def javaVersion = JavaVersion.toVersion(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

tasks.withType(JavaCompile).configureEach {
    options.encoding = 'UTF-8'

    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release.set(targetJavaVersion)
    }
}

processResources {
    def props = [version: version]
    inputs.properties props
    filteringCharset 'UTF-8'
    filesMatching('plugin.yml') {
        expand props
    }
}
