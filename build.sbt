ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.15"

lazy val root = (project in file("."))
  .settings(
    name := "crawler"
  )

val http4sVersion = "1.0.0-M21"
libraryDependencies ++= Seq(
  "org.http4s"      %% "http4s-blaze-server" % http4sVersion,
  "org.http4s"      %% "http4s-blaze-client" % http4sVersion,
  "org.http4s"      %% "http4s-circe"        % http4sVersion,
  "org.http4s"      %% "http4s-dsl"          % http4sVersion,
  "io.circe"        %% "circe-generic"       % "0.14.1",
  "org.jsoup"       % "jsoup"                % "1.16.1",
  "ch.qos.logback"  % "logback-classic"      % "1.4.11",
)
