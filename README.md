# EnxadaB
## KairofCraft
### 29/07/2024
---

# Introdução

- Plugin de minecraft versão 1.21, objetivo EnxadaHost
- Altera o windCharge do minecraft vanila
- Adiciona o comando home
---
# Tecnologias Utilizadas
- Java
- Gradle
- MySQL
- Bukkit / Spigot
- Git
---

# Configuração do Projeto

```groovy
plugins {id 'java'}

group = 'org.KairofCraft'
version = '1.0'

repositories {
    mavenCentral()
    maven {name = "spigotmc-repo"
        url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"}
    maven {name = "sonatype"
        url = "https://oss.sonatype.org/content/groups/public/"}
}

dependencies {compileOnly "org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT"
    implementation 'mysql:mysql-connector-java:8.0.33'}

def targetJavaVersion = 21
groovy
