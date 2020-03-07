import Settings._

lazy val boot = (project in file("boot"))
  .enablePlugins(JavaAppPackaging, AshScriptPlugin, DockerPlugin)
  .settings(commonSettings)
  .settings(dockerSettings)
  .settings(
    resolvers += AkkaServerSupport.resolver,
    libraryDependencies ++= Seq(
      AkkaServerSupport.core,
      AkkaServerSupport.authentication,
      AkkaServerSupport.cooperation,
      MySQLConnectorJava.version,
      Redis.client
    ) ++ `doobie-quill`.all
  )

lazy val root =
  (project in file("."))
    .aggregate(boot)
