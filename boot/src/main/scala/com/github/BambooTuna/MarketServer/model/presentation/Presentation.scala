package com.github.BambooTuna.MarketServer.model.presentation

import com.github.BambooTuna.MarketServer.model.asset.Assets
import com.github.BambooTuna.MarketServer.model.transaction.Transaction

trait Presentation[S <: Assets, P <: Assets] {
  val id: String
  val presenterId: String
  val side: Side

  val size: S
  val price: P

  def tradingWith(userId: String): (Transaction[S], Transaction[P])
}
