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
  LineOAuth2ControllerImpl,
  ProductDisplayController
}
import com.github.BambooTuna.MarketServer.dao.{
  LinkedUserCredentialsDaoImpl,
  ProductDisplayDao,
  RedisStorageStrategy,
  UserCredentialsDaoImpl
}
import com.github.BambooTuna.MarketServer.model.mail.EmailSettings
import com.github.BambooTuna.MarketServer.usecase.{
  AuthenticationUseCaseImpl,
  EmailAuthenticationUseCaseImpl,
  LinkedAuthenticationUseCaseImpl,
  ProductDisplayUseCase
}
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

  val emailSettings = EmailSettings.fromConfig(config)

  private implicit val sessionSettings: JWTSessionSettings =
    new ConfigSessionSettings(config)

  private val sessionStorage: StorageStrategy[String, String] =
    RedisStorageStrategy.fromConfig(system.settings.config, "session")

  private val mailCodeStorage: StorageStrategy[String, String] =
    RedisStorageStrategy.fromConfig(system.settings.config, "mail")

  private val oauthStorage: StorageStrategy[String, String] =
    RedisStorageStrategy.fromConfig(system.settings.config, "oauth2")

  private implicit val session: Session[String, SessionToken] =
    new DefaultSession[SessionToken](sessionSettings, sessionStorage)

  private val userCredentialsDao = new UserCredentialsDaoImpl(dbSession)
  private val linkedUserCredentialsDao = new LinkedUserCredentialsDaoImpl(
    dbSession)
  private val productDisplayDao = new ProductDisplayDao(dbSession)

  private val authenticationUseCase = new AuthenticationUseCaseImpl(
    userCredentialsDao)
  private val emailAuthenticationUseCase = new EmailAuthenticationUseCaseImpl(
    userCredentialsDao,
    mailCodeStorage,
    emailSettings)
  private val linkedAuthenticationUseCase = new LinkedAuthenticationUseCaseImpl(
    linkedUserCredentialsDao)
  private val productDisplayUseCase = new ProductDisplayUseCase(
    productDisplayDao)

//  private val bankAccountAggregatesRef: ActorRef =
//    system.actorOf(Props(new CreditAccountAggregates()),
//                   "sharded-credit-accounts")
//  private val creditAccountAggregateUseCase = new CreditAccountAggregateUseCase(
//    bankAccountAggregatesRef)

  protected val authenticationController = new AuthenticationControllerImpl(
    authenticationUseCase,
    emailAuthenticationUseCase)

  private val clientConfig: ClientConfig =
    ClientConfig.fromConfig("line", system.settings.config)
  protected val lineOAuth2Controller =
    new LineOAuth2ControllerImpl(linkedAuthenticationUseCase,
                                 clientConfig,
                                 oauthStorage)

  protected val productDisplayController = new ProductDisplayController(
    productDisplayUseCase)

}
