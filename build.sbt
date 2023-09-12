name := """my-play-project"""
organization := "com.code.play"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += "javax.persistence" % "javax.persistence-api" % "2.2"
libraryDependencies += "org.elasticsearch.client" % "elasticsearch-rest-high-level-client" % "7.15.1"
