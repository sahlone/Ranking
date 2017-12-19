
name := "RankingService"
 
version := "1.0"
val ficus             = "com.iheart"             %% "ficus"              % "1.4.0"
val scalaGuice        = "net.codingwell"         %% "scala-guice"        % "4.1.0"
val logstash          ="net.logstash.logback"     %  "logstash-logback-encoder" % "4.11"
lazy val `rankingservice` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.2"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice,scalaGuice,ficus ,logstash)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      