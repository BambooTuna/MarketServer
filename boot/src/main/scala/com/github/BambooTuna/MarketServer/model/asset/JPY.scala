package com.github.BambooTuna.MarketServer.model.asset

case class JPY(value: BigDecimal) extends Assets {
  require(value >= 0)
}
