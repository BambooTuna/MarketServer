package com.github.BambooTuna.MarketServer.json

import com.github.BambooTuna.AkkaServerSupport.authentication.json.PasswordInitializationRequestJson

case class PasswordInitializationRequestJsonImpl(mail: String)
    extends PasswordInitializationRequestJson {
  override val signInId: String = mail
}
