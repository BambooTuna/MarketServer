package com.github.BambooTuna.MarketServer.model.market.asset

case class JPY(size: BigDecimal) extends Assets {
  require(size >= 0)
}
