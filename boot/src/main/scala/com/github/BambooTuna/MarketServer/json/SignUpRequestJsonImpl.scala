package com.github.BambooTuna.MarketServer.json

import com.github.BambooTuna.AkkaServerSupport.core.serializer.JsonRecodeSerializer
import com.github.BambooTuna.MarketServer.SystemSettings
import com.github.BambooTuna.MarketServer.model.{
  EncryptedPasswordImpl,
  UserCredentialsImpl
}

case class SignUpRequestJsonImpl(mail: String, pass: String)

object SignUpRequestJsonImpl {
  implicit val js =
    new JsonRecodeSerializer[SignUpRequestJsonImpl, UserCredentialsImpl] {
      override def toRecode(json: SignUpRequestJsonImpl): UserCredentialsImpl =
        UserCredentialsImpl(
          id = SystemSettings.generateId(),
          signinId = json.mail,
          signinPass =
            EncryptedPasswordImpl(json.pass).changeEncryptedPass(json.pass))
    }
}
