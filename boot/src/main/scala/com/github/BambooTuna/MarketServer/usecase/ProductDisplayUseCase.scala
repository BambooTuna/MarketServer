package com.github.BambooTuna.MarketServer.usecase

import cats.data.OptionT
import com.github.BambooTuna.MarketServer.dao.ProductDisplayDao
import com.github.BambooTuna.MarketServer.dao.ProductDisplayDao.ProductDisplayDaoRecord
import com.github.BambooTuna.MarketServer.json.ProductDisplayRequestJson
import com.github.BambooTuna.MarketServer.model.market.DisplayLimit
import com.github.BambooTuna.MarketServer.model.market.asset._
import monix.eval.Task

class ProductDisplayUseCase(productDisplayDao: ProductDisplayDao) {

  def display(
      practitionerId: String,
      request: ProductDisplayRequestJson): Task[ProductDisplayDaoRecord] =
    productDisplayDao.insert(
      ProductDisplayDaoRecord.apply(request.title,
                                    request.detail,
                                    request.price,
                                    practitionerId,
                                    request.state)
    )

  def updateProduct(practitionerId: String,
                    displayId: String,
                    request: ProductDisplayRequestJson): OptionT[Task, Unit] =
    productDisplayDao.update(
      ProductDisplayDaoRecord(displayId,
                              request.title,
                              request.detail,
                              request.price,
                              practitionerId,
                              request.state))

  def getAllProduct(displayLimit: DisplayLimit = DisplayLimit.default)
    : Task[Seq[ProductDisplayDaoRecord]] =
    productDisplayDao.all(displayLimit.limit, displayLimit.offset)

  def getProductDetail(
      displayId: String): OptionT[Task, ProductDisplayDaoRecord] =
    productDisplayDao.resolveById(displayId)

  def getMyProductDetail(
      displayId: String,
      practitionerId: String): OptionT[Task, ProductDisplayDaoRecord] =
    productDisplayDao.resolveById(displayId, practitionerId)

  def getMyAllProduct(practitionerId: String,
                      displayLimit: DisplayLimit = DisplayLimit.default,
                      states: Set[AssetsState] =
                        Set(AssetsState.Open, AssetsState.Draft))
    : Task[Seq[ProductDisplayDaoRecord]] = {
    productDisplayDao
      .resolveByPresenterId(practitionerId,
                            displayLimit.limit,
                            displayLimit.offset)
      .map(_.filter(a => states.contains(a.state)))
  }

}
