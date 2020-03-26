package com.github.BambooTuna.MarketServer.json

import com.github.BambooTuna.MarketServer.model.market.asset.AssetsState
import io.circe._

case class ProductDisplayRequestJson(title: String,
                                     detail: String,
                                     price: Long,
                                     state: AssetsState) {
  require(title.nonEmpty, "タイトルを入力してください")
  require(title.length <= 255, "タイトルが長すぎます(<255)")

  require(detail.nonEmpty, "タイトルを入力してください")
  require(detail.length <= 255, "詳細が長すぎます(<255)")

  require(price > 0, "価格は0以上にしてください")
}

object ProductDisplayRequestJson {
  implicit val encode: Encoder[AssetsState] = Encoder[String].contramap(_.value)
  implicit val decode: Decoder[AssetsState] =
    Decoder[String].emapTry(AssetsState.from)
}
