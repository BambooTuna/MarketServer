package com.github.BambooTuna.MarketServer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import cats.effect.{Blocker, Resource}
import com.github.BambooTuna.AkkaServerSupport.authentication.oauth2.ClientConfig
import com.github.BambooTuna.AkkaServerSupport.authentication.session.{
  ConfigSessionSettings,
  DefaultSession,
  JWTSessionSettings,
  SessionToken
}
import com.github.BambooTuna.AkkaServerSupport.core.router.DefaultCorsSupport
import com.github.BambooTuna.AkkaServerSupport.core.session.{
  Session,
  StorageStrategy
}
import com.github.BambooTuna.MarketServer.controller.{
  AuthenticationControllerImpl,
  LineOAuth2ControllerImpl
}
import com.github.BambooTuna.MarketServer.dao.RedisStorageStrategy
import com.typesafe.config.Config
import doobie.hikari.HikariTransactor
import monix.eval.Task

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

abstract class Component(implicit system: ActorSystem,
                         materializer: ActorMaterializer) {
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher
  val config: Config = system.settings.config

  val corsSupport = DefaultCorsSupport.fromConfig(config)

  val ec: ExecutionContext = monix.execution.Scheduler.Implicits.global
  val dbSession: Resource[Task, HikariTransactor[Task]] =
    HikariTransactor.newHikariTransactor[Task](
      config.getString("mysql.db.driver"),
      config.getString("mysql.db.url"),
      config.getString("mysql.db.user"),
      config.getString("mysql.db.password"),
      ec,
      Blocker.liftExecutionContext(ec)
    )

  private val sessionSettings: JWTSessionSettings =
    new ConfigSessionSettings(config)

  private val sessionStorage: StorageStrategy[String, String] =
    RedisStorageStrategy.fromConfig(config, "session")(system, sessionSettings)

  private implicit val session: Session[String, SessionToken] =
    new DefaultSession[SessionToken](sessionSettings, sessionStorage)

  val authenticationController =
    new AuthenticationControllerImpl(dbSession)

  private val clientConfig: ClientConfig =
    ClientConfig.fromConfig("line", config)

  private val oauthStorage: StorageStrategy[String, String] =
    RedisStorageStrategy.fromConfig(config, "oauth2")(system, sessionSettings)

  val lineOAuth2Controller =
    new LineOAuth2ControllerImpl(clientConfig, oauthStorage, dbSession)

}
