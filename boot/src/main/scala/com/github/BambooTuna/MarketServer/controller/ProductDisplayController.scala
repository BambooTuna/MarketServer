package com.github.BambooTuna.MarketServer.controller

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, Route}
import com.github.BambooTuna.AkkaServerSupport.authentication.session.SessionToken
import com.github.BambooTuna.AkkaServerSupport.core.session.Session
import com.github.BambooTuna.MarketServer.json.ProductDisplayRequestJson
import com.github.BambooTuna.MarketServer.model.market.DisplayLimit
import com.github.BambooTuna.MarketServer.usecase.ProductDisplayUseCase
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.syntax._
import io.circe.generic.auto._
import monix.execution.Scheduler
import com.github.BambooTuna.MarketServer.json.ProductDisplayRequestJson._
import com.github.BambooTuna.MarketServer.model.market.asset.AssetsState

import scala.util.{Failure, Success}

class ProductDisplayController(productDisplayUseCase: ProductDisplayUseCase)(
    implicit session: Session[String, SessionToken])
    extends FailFastCirceSupport {

  type QueryP[Q] = Directive[Q] => Route

  def getAllProductRoute(implicit s: Scheduler): QueryP[Unit] = _ {
    parameters('limit.as[Int] ?, 'page.as[Int] ?)
      .key { (limit, page) =>
        val f =
          productDisplayUseCase
            .getAllProduct(DisplayLimit(limit, page))
            .runToFuture
        onSuccess(f) { list =>
          complete(StatusCodes.OK -> list)
        }
      }
  }

  def getProductDetailRoute(implicit s: Scheduler): QueryP[Tuple1[String]] = _ {
    displayId =>
      val f =
        productDisplayUseCase
          .getProductDetail(displayId)
          .toRight(1) //TODO
          .value
          .runToFuture
      onSuccess(f) {
        case Right(value) => complete(StatusCodes.OK -> value)
        case Left(value) =>
          complete(StatusCodes.NotFound)
        //reject(value)
      }
  }

  def getMyProductListRoute(implicit s: Scheduler): QueryP[Unit] = _ {
    parameters('limit.as[Int] ?, 'page.as[Int] ?, 'states.as[String] ?)
      .key { (limit, page, states) =>
        session.requiredSession { sessionToken =>
          val stateSet =
            states
              .map(
                _.split(",")
                  .map(AssetsState.from)
                  .collect {
                    case Success(value) => value
                  }
                  .toSet)
              .getOrElse(Set(AssetsState.Open, AssetsState.Draft))

          val f =
            productDisplayUseCase
              .getMyAllProduct(practitionerId = sessionToken.userId,
                               displayLimit = DisplayLimit(limit, page),
                               states = stateSet)
              .runToFuture
          onSuccess(f) { list =>
            complete(StatusCodes.OK -> list)
          }
        }
      }
  }

  def displayRoute(implicit s: Scheduler): QueryP[Unit] = _ {
    entity(as[ProductDisplayRequestJson]) { json =>
      session.requiredSession { sessionToken =>
        val f =
          productDisplayUseCase
            .display(sessionToken.userId, json)
            .runToFuture
        onSuccess(f) { a =>
          complete(StatusCodes.OK -> a.id)
        }
      }
    }
  }

  def updateProductRoute(implicit s: Scheduler): QueryP[Tuple1[String]] = _ {
    displayId =>
      entity(as[ProductDisplayRequestJson]) { json =>
        session.requiredSession { sessionToken =>
          val f =
            productDisplayUseCase
              .updateProduct(sessionToken.userId, displayId, json)
              .toRight(1) //TODO
              .value
              .runToFuture
          onSuccess(f) {
            case Right(_) => complete(StatusCodes.OK)
            case Left(value) =>
              complete(StatusCodes.BadRequest)
            //reject(value)
          }
        }
      }
  }

}
