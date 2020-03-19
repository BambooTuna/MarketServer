package com.github.BambooTuna.MarketServer.json

import com.github.BambooTuna.MarketServer.model.market.asset.AssetsState
import io.circe._

case class ProductDisplayRequestJson(title: String,
                                     detail: String,
                                     price: Long,
                                     state: AssetsState)

object ProductDisplayRequestJson {
  implicit val encode: Encoder[AssetsState] = Encoder[String].contramap(_.value)
  implicit val decode: Decoder[AssetsState] =
    Decoder[String].emapTry(AssetsState.from)
}
