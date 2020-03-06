import Settings._

lazy val boot = (project in file("boot"))
  .enablePlugins(JavaAppPackaging, AshScriptPlugin, DockerPlugin)
  .settings(commonSettings)
  .settings(dockerSettings)
  .settings(
    resolvers += "Maven Repo on github" at "https://BambooTuna.github.io/AkkaServerSupport",
    libraryDependencies ++= Seq(
      "com.github.BambooTuna" %% "akkaserversupport-core" % "1.0.0-SNAPSHOT",
      "com.github.BambooTuna" %% "akkaserversupport-authentication" % "1.0.0-SNAPSHOT",
      "com.github.BambooTuna" %% "akkaserversupport-cooperation" % "1.0.0-SNAPSHOT",
      MySQLConnectorJava.version,
      Redis.client
    ) ++ `doobie-quill`.all
  )

lazy val root =
  (project in file("."))
    .aggregate(boot)
