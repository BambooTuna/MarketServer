package com.github.BambooTuna.MarketServer.model.asset

case class BTC(value: BigDecimal) extends Assets {
  require(value >= 0)
}
