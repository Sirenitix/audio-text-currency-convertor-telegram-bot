plugins {
    id("org.springframework.boot") version("2.7.8")
    java
}

version = "1.0.0"
group = "com.pandev.currency"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.7.8"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web:3.0.4")
    implementation("org.postgresql:postgresql:42.5.4")
    compileOnly("org.projectlombok:lombok-maven-plugin:1.18.20.0")
    annotationProcessor("org.projectlombok:lombok-maven-plugin:1.18.20.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.0.4")
    implementation("com.github.pengrad:java-telegram-bot-api:6.7.0")
    implementation("org.javamoney:moneta:1.4.2")
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.0.4")
    implementation("org.springframework.boot:spring-boot-configuration-processor:3.0.4")

}

