package com.github.BambooTuna.MarketServer.model.market.asset

case class Product(size: BigDecimal, title: String, detail: String)
    extends Assets {
  require(size == 1)
  require(title.length <= 255)
  require(detail.length <= 255)
}
