package com.github.BambooTuna.MarketServer.json

import com.github.BambooTuna.AkkaServerSupport.authentication.json.SignUpRequestJson
import com.github.BambooTuna.MarketServer.SystemSettings
import com.github.BambooTuna.MarketServer.model.{
  EncryptedPasswordImpl,
  UserCredentialsImpl
}

case class SignUpRequestJsonImpl(mail: String, pass: String)
    extends SignUpRequestJson[UserCredentialsImpl] {
  require(mail.nonEmpty && pass.nonEmpty)
  override def createUserCredentials: UserCredentialsImpl =
    UserCredentialsImpl(id = SystemSettings.generateId(),
                        signinId = mail,
                        signinPass =
                          EncryptedPasswordImpl(pass).changeEncryptedPass(pass))
}
