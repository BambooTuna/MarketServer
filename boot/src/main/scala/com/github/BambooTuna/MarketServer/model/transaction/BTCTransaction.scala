package com.github.BambooTuna.MarketServer.model.transaction

import com.github.BambooTuna.MarketServer.model.asset.BTC

case class BTCTransaction(id: String, fromAddress: String, toAddress: String, assets: BTC) extends Transaction[BTC]

object BTCTransaction {
  def generate(fromAddress: String, toAddress: String, assets: BTC): BTCTransaction =
    BTCTransaction(
      id = java.util.UUID.randomUUID.toString.replaceAll("-", ""),
      fromAddress = fromAddress,
      toAddress = toAddress,
      assets = assets
    )
}
