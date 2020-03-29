import Settings._

lazy val boot = (project in file("boot"))
  .enablePlugins(JavaAppPackaging, AshScriptPlugin, DockerPlugin)
  .settings(commonSettings)
  .settings(dockerSettings)
  .settings(gaeSettings)
  .settings(
    resolvers += AkkaServerSupport.resolver,
    libraryDependencies ++= Seq(
      AkkaServerSupport.core,
      AkkaServerSupport.authentication,
      AkkaServerSupport.cooperation,
      MySQLConnectorJava.version,
      Redis.client,
      "org.simplejavamail" % "simple-java-mail" % "6.0.3",
      "org.iq80.leveldb" % "leveldb" % "0.7",
      "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8",
      "javax.ws.rs" % "javax.ws.rs-api" % "2.0.1",
      "com.github.swagger-akka-http" %% "swagger-akka-http" % "2.0.4"
    ) ++ `doobie-quill`.all
  )

lazy val root =
  (project in file("."))
    .aggregate(boot)
