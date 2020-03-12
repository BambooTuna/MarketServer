package com.github.BambooTuna.MarketServer.model.asset

case class JPY(value: Long) extends Assets {
  require(value >= 0)
}
