logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.sonatypeRepo("snapshots")
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.9")