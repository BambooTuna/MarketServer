package com.github.BambooTuna.MarketServer.usecase

import com.github.BambooTuna.AkkaServerSupport.authentication.useCase.LinkedAuthenticationUseCase
import com.github.BambooTuna.MarketServer.dao.LinkedUserCredentialsDaoImpl

class LinkedAuthenticationUseCaseImpl extends LinkedAuthenticationUseCase {
  override val linkedUserCredentialsDao: LinkedUserCredentialsDaoImpl =
    new LinkedUserCredentialsDaoImpl
}
