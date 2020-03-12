package com.github.BambooTuna.MarketServer.model.transaction

import com.github.BambooTuna.MarketServer.model.asset.JPY

case class MoneyTransaction(id: String, fromAddress: String, toAddress: String, assets: JPY) extends Transaction[JPY]
