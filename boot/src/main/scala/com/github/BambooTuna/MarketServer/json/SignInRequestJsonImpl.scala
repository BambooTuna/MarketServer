package com.github.BambooTuna.MarketServer.json

import com.github.BambooTuna.AkkaServerSupport.authentication.json.SignInRequestJson

case class SignInRequestJsonImpl(mail: String, pass: String)
    extends SignInRequestJson {
  override val signInId: String = mail
  override val signInPass: String = pass
}
