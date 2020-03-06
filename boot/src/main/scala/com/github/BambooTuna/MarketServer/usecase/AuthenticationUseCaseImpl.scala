package com.github.BambooTuna.MarketServer.usecase

import com.github.BambooTuna.AkkaServerSupport.authentication.useCase.AuthenticationUseCase
import com.github.BambooTuna.MarketServer.dao.UserCredentialsDaoImpl
import com.github.BambooTuna.MarketServer.json.{
  PasswordInitializationRequestJsonImpl,
  SignInRequestJsonImpl,
  SignUpRequestJsonImpl
}
import com.github.BambooTuna.MarketServer.model.UserCredentialsImpl

class AuthenticationUseCaseImpl extends AuthenticationUseCase {
  override type Record = UserCredentialsImpl

  override type SignUpRequest = SignUpRequestJsonImpl
  override type SignInRequest = SignInRequestJsonImpl
  override type PasswordInitializationRequest =
    PasswordInitializationRequestJsonImpl

  override val userCredentialsDao: UserCredentialsDaoImpl =
    new UserCredentialsDaoImpl

  override def ioErrorHandling[T, U >: T](io: IO[T], f: Throwable => U): IO[U] =
    io.onErrorHandle(f)

}
