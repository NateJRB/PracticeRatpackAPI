/**
 * This was originally going to be a Kotlin based project, but I didn't feel that I had the time to both learn
 * how to work with Ratpack AND learn how to use a community-made plugin for Kotlin, so I'm just using Groovy.
 * I might convert this to Kotlin later on as practice.
 */

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("io.ratpack:ratpack-gradle:1.9.0")
    }
}

plugins {
    kotlin("jvm") version "1.5.21"
    id("io.ratpack.ratpack-groovy") version "1.9.0"
    idea
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
    implementation(ratpack.dependency("groovy"))
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("mysql:mysql-connector-java:8.0.26")
}

//tasks.register("runMain") {
//
//}