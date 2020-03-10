package com.github.BambooTuna.MarketServer

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods.{DELETE, GET, POST}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.github.BambooTuna.AkkaServerSupport.core.router.{
  RouteController,
  Router,
  route
}
import monix.execution.Scheduler
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext

class RouteControllerImpl(implicit system: ActorSystem,
                          mat: ActorMaterializer,
                          executor: ExecutionContext)
    extends Component
    with RouteController {

  private val logger: Logger = LoggerFactory.getLogger(getClass)

  override def toRoutes: Route =
    handleExceptions(defaultExceptionHandler(logger)) {
      handleRejections(defaultRejectionHandler) {
        createRoute(monix.execution.Scheduler.Implicits.global).create
      }
    }

  private def createRoute(implicit s: Scheduler): Router = {
    Router(
      route(POST, "signup", authenticationController.signUpRoute),
      route(POST, "signin", authenticationController.signInRoute),
      route(POST, "init", authenticationController.passwordInitializationRoute),
      route(GET, "health", authenticationController.healthCheck),
      route(DELETE, "logout", authenticationController.logout),
      route(POST,
            "oauth2" / "signin" / "line",
            lineOAuth2Controller.fetchCooperationLink),
      route(GET,
            "oauth2" / "signin" / "line",
            lineOAuth2Controller.authenticationFromCode)
    )
  }

}
