package com.github.BambooTuna.MarketServer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.github.BambooTuna.AkkaServerSupport.core.domain.ServerConfig

import scala.concurrent.ExecutionContextExecutor

object Main extends App {

  implicit val system: ActorSystem = ActorSystem("MarketServer")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val r = new Routes(system.settings.config)

  val serverConfig: ServerConfig =
    ServerConfig(
      system.settings.config.getString("boot.server.host"),
      system.settings.config.getString("boot.server.port").toInt
    )

  val bindingFuture =
    Http().bindAndHandle(r.createRoute, serverConfig.host, serverConfig.port)

  sys.addShutdownHook {
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }

}
