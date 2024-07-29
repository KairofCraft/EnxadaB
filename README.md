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
    implementation 'redis.clients:jedis:4.3.1'
}
