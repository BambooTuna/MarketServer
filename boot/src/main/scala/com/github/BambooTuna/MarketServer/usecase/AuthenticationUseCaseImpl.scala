package com.github.BambooTuna.MarketServer.usecase

import com.github.BambooTuna.AkkaServerSupport.authentication.useCase.AuthenticationUseCase
import com.github.BambooTuna.MarketServer.dao.UserCredentialsDaoImpl
import com.github.BambooTuna.MarketServer.json.{
  PasswordInitializationRequestJsonImpl,
  SignInRequestJsonImpl,
  SignUpRequestJsonImpl
}
import com.github.BambooTuna.MarketServer.model.UserCredentialsImpl

class AuthenticationUseCaseImpl
    extends AuthenticationUseCase[SignUpRequestJsonImpl,
                                  SignInRequestJsonImpl,
                                  PasswordInitializationRequestJsonImpl,
                                  UserCredentialsImpl] {
  override val userCredentialsDao: UserCredentialsDaoImpl =
    new UserCredentialsDaoImpl
}
