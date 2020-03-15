package com.github.BambooTuna.MarketServer.model.account

import com.github.BambooTuna.AkkaServerSupport.authentication.model.UserCredentials
import com.github.BambooTuna.MarketServer.SystemSettings

case class UserCredentialsImpl(id: String,
                               signinId: String,
                               signinPass: EncryptedPasswordImpl,
                               activated: Boolean)
    extends UserCredentials {
  override type SigninPass = EncryptedPasswordImpl

  override def doAuthenticationByPassword(inputPass: Any): Boolean =
    signinPass == inputPass

  override def changePassword(newPlainPassword: String): UserCredentialsImpl =
    copy(signinPass = signinPass.changeEncryptedPass(newPlainPassword))

  override def initPassword(): (UserCredentialsImpl, String) = {
    val newPlainPassword = SystemSettings.generateId()
    (changePassword(newPlainPassword), newPlainPassword)
  }

}