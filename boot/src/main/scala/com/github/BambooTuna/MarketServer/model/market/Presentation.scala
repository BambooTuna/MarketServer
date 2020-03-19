package com.github.BambooTuna.MarketServer.model.market

import com.github.BambooTuna.MarketServer.model.market.asset.Assets

case class Presentation[Offer <: Assets, Request <: Assets](
    presentationId: String,
    presenterId: String,
    side: Side,
    offer: Offer,
    request: Request) {

  def tradingWith(userId: String): (Transaction[Offer], Transaction[Request]) =
    side match {
      case Buy =>
        (Transaction.generate[Offer](userId, presenterId, offer),
         Transaction.generate[Request](presenterId, userId, request))
      case Sell =>
        (Transaction.generate[Offer](presenterId, userId, offer),
         Transaction.generate[Request](userId, presenterId, request))
    }

}
