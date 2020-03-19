package com.github.BambooTuna.MarketServer.model.market

import com.github.BambooTuna.MarketServer.SystemSettings
import com.github.BambooTuna.MarketServer.model.market.asset.Assets

case class Transaction[AssetsType <: Assets](transactionId: String,
                                             fromAddress: String,
                                             toAddress: String,
                                             assets: AssetsType)

object Transaction {
  def generate[AssetsType <: Assets](
      fromAddress: String,
      toAddress: String,
      assets: AssetsType): Transaction[AssetsType] =
    Transaction(
      transactionId = SystemSettings.generateId(),
      fromAddress = fromAddress,
      toAddress = toAddress,
      assets = assets
    )
}
