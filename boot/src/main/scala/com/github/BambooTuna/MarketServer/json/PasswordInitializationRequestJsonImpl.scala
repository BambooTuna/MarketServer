package com.github.BambooTuna.MarketServer.json

import com.github.BambooTuna.AkkaServerSupport.authentication.json.PasswordInitializationRequestJson
import com.github.BambooTuna.MarketServer.model.UserCredentialsImpl

case class PasswordInitializationRequestJsonImpl(mail: String)
    extends PasswordInitializationRequestJson[UserCredentialsImpl] {
  require(mail.nonEmpty)
  override val signInId: String = mail
}
