package com.github.BambooTuna.MarketServer

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.StandardRoute
import akka.stream.ActorMaterializer
import cats.effect.{Blocker, Resource}
import com.github.BambooTuna.AkkaServerSupport.authentication.router.RouteSupport.SessionToken
import com.github.BambooTuna.AkkaServerSupport.authentication.session.{
  ConfigSessionSettings,
  DefaultSession,
  JWTSessionSettings
}
import com.github.BambooTuna.AkkaServerSupport.cooperation.model.{
  ClientConfig,
  OAuth2Settings
}
import com.github.BambooTuna.AkkaServerSupport.core.router.DefaultCorsSupport
import com.github.BambooTuna.AkkaServerSupport.core.session.StorageStrategy
import com.github.BambooTuna.MarketServer.dao.RedisStorageStrategy
import com.github.BambooTuna.MarketServer.router.{
  AuthenticationRouteImpl,
  LineOAuth2RouteImpl
}
import com.typesafe.config.Config
import doobie.hikari.HikariTransactor
import monix.eval.Task

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

abstract class Component(config: Config)(implicit system: ActorSystem,
                                         materializer: ActorMaterializer) {
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

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

  private val session =
    new DefaultSession[SessionToken](sessionSettings, sessionStorage) {
      override def fromThrowable(throwable: Throwable): StandardRoute =
        complete(StatusCodes.InternalServerError) //TODO
    }

  val authenticationRoute =
    new AuthenticationRouteImpl(session) {
      override def errorHandling(throwable: Throwable): StandardRoute =
        complete(StatusCodes.InternalServerError) //TODO
    }

  //////////
  private val oauthStorage: StorageStrategy[String, String] =
    RedisStorageStrategy.fromConfig(config, "oauth2")(system, sessionSettings)

  private val clientConfig: ClientConfig =
    ClientConfig.fromConfig("line", system.settings.config)

  private val lineOAuth = OAuth2Settings(clientConfig, oauthStorage)

  val lineOAuth2Route =
    new LineOAuth2RouteImpl(session, lineOAuth) {
      override def errorHandling(throwable: Throwable): StandardRoute =
        complete(StatusCodes.InternalServerError)
    }

}
