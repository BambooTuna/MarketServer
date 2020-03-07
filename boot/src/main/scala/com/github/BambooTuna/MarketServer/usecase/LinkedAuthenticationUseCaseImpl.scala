package com.github.BambooTuna.MarketServer.usecase

import com.github.BambooTuna.AkkaServerSupport.authentication.useCase.LinkedAuthenticationUseCase
import com.github.BambooTuna.MarketServer.command.{
  LinkedSignInRequestCommandImpl,
  LinkedSignUpRequestCommandImpl
}
import com.github.BambooTuna.MarketServer.dao.LinkedUserCredentialsDaoImpl

class LinkedAuthenticationUseCaseImpl extends LinkedAuthenticationUseCase {
  override val linkedUserCredentialsDao: LinkedUserCredentialsDaoImpl =
    new LinkedUserCredentialsDaoImpl

  override type LinkedSignUpRequest = LinkedSignUpRequestCommandImpl
  override type LinkedSignInRequest = LinkedSignInRequestCommandImpl

  override def ioErrorHandling[T, U >: T](io: IO[T], f: Throwable => U): IO[U] =
    io.onErrorHandle(f)
}
