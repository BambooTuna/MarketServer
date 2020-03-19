package com.github.BambooTuna.MarketServer.model.market

case class DisplayLimit(limit: Int, page: Int) {
  require(limit >= 1)
  require(page >= 1)
  val offset: Int = limit * (page - 1)
}

object DisplayLimit {

  def apply(limit: Option[Int], page: Option[Int]): DisplayLimit =
    DisplayLimit(limit.getOrElse(10), page.getOrElse(1))

  def default: DisplayLimit = apply(None, None)

}
