# EnxadaB
## Vinicius Kairof
### 29/07/2024
---

# Introdução

- Plugin de minecraft, objetivo EnxadaHost
- que altera o windCharge do minecraft vanila
- adiciona o comando home
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

repositories {
    mavenCentral()
}

dependencies {
    implementation 'mysql:mysql-connector-java:8.0.33'
    compileOnly "org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT"
}
