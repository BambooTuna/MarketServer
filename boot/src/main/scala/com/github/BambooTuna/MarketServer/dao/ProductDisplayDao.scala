package com.github.BambooTuna.MarketServer.dao

import cats.data.OptionT
import cats.effect.Resource
import com.github.BambooTuna.MarketServer.SystemSettings
import com.github.BambooTuna.MarketServer.dao.ProductDisplayDao.ProductDisplayDaoRecord
import com.github.BambooTuna.MarketServer.model.market.asset.AssetsState
import doobie.hikari.HikariTransactor
import doobie.quill.DoobieContext
import io.getquill.SnakeCase
import monix.eval.Task

class ProductDisplayDao(dbSession: Resource[Task, HikariTransactor[Task]]) {

  val dc: DoobieContext.MySQL[SnakeCase] = new DoobieContext.MySQL(SnakeCase)
  import dc._
  import doobie.implicits._

  implicit lazy val daoSchemaMeta =
    schemaMeta[ProductDisplayDaoRecord](
      "product_display",
      _.id -> "id",
      _.productTitle -> "title",
      _.productDetail -> "detail",
      _.requestPrice -> "price",
      _.presenterId -> "presenter_id",
      _.state.value -> "state"
    )

  def insert(record: ProductDisplayDaoRecord): Task[ProductDisplayDaoRecord] = {
    val q = quote {
      query[ProductDisplayDaoRecord]
        .insert(lift(record))
    }
    dbSession
      .use(x => run(q).transact(x))
      .map(a => if (a > 0) record else throw new RuntimeException)
  }

  def all(limit: Int, offset: Int): Task[Seq[ProductDisplayDaoRecord]] = {
    val q = quote {
      query[ProductDisplayDaoRecord]
        .filter(_.state.value == lift(AssetsState.Open.value))
        .drop(lift(offset))
        .take(lift(limit))
    }
    dbSession
      .use(x => run(q).transact(x))
  }

  def resolveByPresenterId(presenterId: String,
                           limit: Int,
                           offset: Int): Task[Seq[ProductDisplayDaoRecord]] = {
    val q = quote {
      query[ProductDisplayDaoRecord]
        .filter(a =>
          a.presenterId == lift(presenterId) && a.state.value != lift(
            AssetsState.Closed.value))
        .drop(lift(offset))
        .take(lift(limit))
    }
    dbSession
      .use(x => run(q).transact(x))
  }

  def resolveById(id: String): OptionT[Task, ProductDisplayDaoRecord] = {
    OptionT[Task, ProductDisplayDaoRecord] {
      val q = quote {
        query[ProductDisplayDaoRecord]
          .filter(a =>
            a.id == lift(id) && a.state.value == lift(AssetsState.Open.value))
      }
      dbSession
        .use(x => run(q).transact(x))
        .map(_.headOption)
    }
  }

  def resolveById(
      id: String,
      presenterId: String): OptionT[Task, ProductDisplayDaoRecord] = {
    OptionT[Task, ProductDisplayDaoRecord] {
      val q = quote {
        query[ProductDisplayDaoRecord]
          .filter(a =>
            a.id == lift(id) && a.presenterId == lift(presenterId) && a.state.value != lift(
              AssetsState.Closed.value))
      }
      dbSession
        .use(x => run(q).transact(x))
        .map(_.headOption)
    }
  }

  def update(record: ProductDisplayDaoRecord): OptionT[Task, Unit] = {
    OptionT[Task, Unit] {
      val q = quote {
        query[ProductDisplayDaoRecord]
          .filter(
            a =>
              a.id == lift(record.id) && a.presenterId == lift(
                record.presenterId) && a.state.value != lift(
                AssetsState.Closed.value))
          .update(lift(record))
      }
      dbSession
        .use(x => run(q).transact(x))
        .map(a => if (a > 0) Some() else None)
    }
  }

}

object ProductDisplayDao {

  case class ProductDisplayDaoRecord(id: String,
                                     productTitle: String,
                                     productDetail: String,
                                     requestPrice: BigDecimal,
                                     presenterId: String,
                                     state: AssetsState)
  object ProductDisplayDaoRecord {
    def apply(productTitle: String,
              productDetail: String,
              requestPrice: Long,
              presenterId: String,
              state: AssetsState): ProductDisplayDaoRecord =
      ProductDisplayDaoRecord(SystemSettings.generateId(),
                              productTitle,
                              productDetail,
                              requestPrice,
                              presenterId,
                              state)
  }

}
