package com.github.BambooTuna.MarketServer.model.market

sealed trait Side extends io.getquill.Embedded {
  val value: String
}

case object Buy extends Side {
  override val value: String = "Buy"
}
case object Sell extends Side {
  override val value: String = "Sell"
}

object Side {
  def from(value: String): Side = value match {
    case "Buy"  => Buy
    case "Sell" => Sell
  }
}
