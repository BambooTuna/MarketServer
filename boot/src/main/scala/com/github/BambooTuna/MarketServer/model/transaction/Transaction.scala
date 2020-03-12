package com.github.BambooTuna.MarketServer.model.transaction

import com.github.BambooTuna.MarketServer.model.asset.Assets


trait Transaction[AssetsType <: Assets] {
  val id: String
  val fromAddress: String
  val toAddress: String
  val assets: AssetsType
}
