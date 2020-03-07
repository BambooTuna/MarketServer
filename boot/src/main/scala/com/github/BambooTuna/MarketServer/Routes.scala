package com.github.BambooTuna.MarketServer

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods.{DELETE, GET, POST}
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.github.BambooTuna.AkkaServerSupport.core.router.{Router, route}
import com.typesafe.config.Config

class Routes(config: Config)(implicit system: ActorSystem,
                             materializer: ActorMaterializer)
    extends Component(config) {

  def createRoute: Route = {
    corsSupport.corsHandler(
      Router(
        route(POST, "signup", authenticationRoute.signUpRoute(dbSession)),
        route(POST, "signin", authenticationRoute.signInRoute(dbSession)),
        route(POST,
              "init",
              authenticationRoute.passwordInitializationRoute(dbSession)),
        route(GET, "health", authenticationRoute.healthCheck),
        route(DELETE, "logout", authenticationRoute.logout)
      ).create
    )
  }

}
