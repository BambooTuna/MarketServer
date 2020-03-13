package com.github.BambooTuna.MarketServer.usecase

import com.github.BambooTuna.AkkaServerSupport.authentication.dao.UserCredentialsDao
import com.github.BambooTuna.AkkaServerSupport.authentication.useCase.AuthenticationUseCase
import com.github.BambooTuna.MarketServer.json.{SignInRequestJsonImpl, SignUpRequestJsonImpl}
import com.github.BambooTuna.MarketServer.model.account.UserCredentialsImpl

class AuthenticationUseCaseImpl(
    val userCredentialsDao: UserCredentialsDao[UserCredentialsImpl])
    extends AuthenticationUseCase[SignUpRequestJsonImpl,
                                  SignInRequestJsonImpl,
                                  UserCredentialsImpl]
