package com.github.BambooTuna.MarketServer.model.transaction

import com.github.BambooTuna.MarketServer.model.asset.JPY

case class JPYTransaction(id: String, fromAddress: String, toAddress: String, assets: JPY) extends Transaction[JPY]

object JPYTransaction {
  def generate(fromAddress: String, toAddress: String, assets: JPY): JPYTransaction =
    JPYTransaction(
      id = java.util.UUID.randomUUID.toString.replaceAll("-", ""),
      fromAddress = fromAddress,
      toAddress = toAddress,
      assets = assets
    )
}
