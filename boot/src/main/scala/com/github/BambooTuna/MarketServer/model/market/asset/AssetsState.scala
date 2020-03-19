package com.github.BambooTuna.MarketServer.model.market.asset

import scala.util.Try

case class AssetsState(value: String) extends io.getquill.Embedded

object AssetsState {

  def from(value: String): Try[AssetsState] = Try {
    value match {
      case "open"   => Open
      case "draft"  => Draft
      case "closed" => Closed
      case other    => throw new Exception(s"Unknown AssetsState value: $other")
    }
  }

  def Open: AssetsState = AssetsState("open")
  def Draft: AssetsState = AssetsState("draft")
  def Closed: AssetsState = AssetsState("closed")

}
