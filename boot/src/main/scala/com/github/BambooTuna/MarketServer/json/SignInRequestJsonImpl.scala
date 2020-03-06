package com.github.BambooTuna.MarketServer.json

import com.github.BambooTuna.AkkaServerSupport.authentication.json.SignInRequestJson
import com.github.BambooTuna.MarketServer.model.UserCredentialsImpl

case class SignInRequestJsonImpl(mail: String, pass: String)
    extends SignInRequestJson[UserCredentialsImpl] {
  override val signInId: String = mail
  override val signInPass: String = pass
}
