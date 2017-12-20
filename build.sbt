import sbt._

scalaVersion := "2.12.2"
name := "RankingService"
 
version := "1.0"

val ficus             = "com.iheart"             %% "ficus"                     % "1.4.0"
val scalaGuice        = "net.codingwell"         %% "scala-guice"               % "4.1.0"
val logstash          = "net.logstash.logback"   %  "logstash-logback-encoder"  % "4.11"
val wiremock          = "com.github.tomakehurst" %  "wiremock"                  % "2.12.0"
val scalaCheck        = "org.scalacheck"         %% "scalacheck"                % "1.13.4"
val scalaTest         = "org.scalatest"          %% "scalatest"                 % "3.0.1"
val playJson          = "com.typesafe.play"      %% "play-json"                 % "2.6.9"
val scalatestplusPlay = "org.scalatestplus.play" %% "scalatestplus-play"        % "3.1.1"
val mockito           = "org.mockito"            %  "mockito-core"              % "1.10.19"
val cats              = "org.typelevel"          %% "cats-core"                 % "1.0.0-RC1"

val compileLibraries= Seq(
  guice,scalaGuice,ficus ,logstash, wiremock,ws
)

val testLibraries =Seq(
  scalaTest
)
lazy val rankingService = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(libraryDependencies ++= compileLibraries)
  .settings(libraryDependencies ++= testLibraries)

