import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.github.hdulay"
ThisBuild / organizationName := "hdulay"

resolvers += "Confluent" at "https://packages.confluent.io/maven/"
resolvers += Classpaths.typesafeReleases
resolvers += Resolver.jcenterRepo
resolvers += "central" at "http://central.maven.org/maven2/"

lazy val root = (project in file("."))
  .settings(
    name := "kstream-pdf",
    libraryDependencies += scalaTest % Test
  )

// https://mvnrepository.com/artifact/org.apache.pdfbox/pdfbox
libraryDependencies += "org.apache.pdfbox" % "pdfbox" % "2.0.19"

libraryDependencies += "org.apache.kafka" % "kafka-streams-scala_2.12" % "5.4.1-ccs"
libraryDependencies += "org.apache.kafka" % "kafka-streams" % "5.4.1-ccs"
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "5.4.1-ccs"
libraryDependencies += "io.confluent" % "kafka-avro-serializer" % "5.4.1"
libraryDependencies += "org.apache.avro" % "avro" % "1.9.2"
libraryDependencies += "org.apache.avro" % "avro-maven-plugin" % "1.9.2"

// https://mvnrepository.com/artifact/org.scala-lang/scala-reflect
libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.12.8"

// https://mvnrepository.com/artifact/com.google.code.gson/gson
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.5"

mainClass in (Compile, run) := Some("example.PDF2TextKstream")
