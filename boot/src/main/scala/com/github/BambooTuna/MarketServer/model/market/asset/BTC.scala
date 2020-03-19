package com.github.BambooTuna.MarketServer.model.market.asset

case class BTC(size: BigDecimal) extends Assets {
  require(size >= 0)
}
