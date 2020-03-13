package com.github.BambooTuna.MarketServer.model.presentation

import com.github.BambooTuna.MarketServer.model.asset.{BTC, JPY}
import com.github.BambooTuna.MarketServer.model.transaction.{BTCTransaction, JPYTransaction, Transaction}

case class BTCJPYPresentation(id: String, presenterId: String, side: Side, size: BTC, price: JPY) extends Presentation[BTC, JPY] {

  override def tradingWith(userId: String): (Transaction[BTC], Transaction[JPY]) = side match {
    case Buy =>
      (BTCTransaction.generate(userId, presenterId, size), JPYTransaction.generate(presenterId, userId, price))
    case Sell =>
      (BTCTransaction.generate(presenterId, userId, size), JPYTransaction.generate(userId, presenterId, price))
  }

}
