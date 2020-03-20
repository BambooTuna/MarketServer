package com.github.BambooTuna.MarketServer.controller

import com.github.BambooTuna.AkkaServerSupport.authentication.controller.AuthenticationController
import com.github.BambooTuna.AkkaServerSupport.authentication.session.SessionToken
import com.github.BambooTuna.AkkaServerSupport.authentication.useCase.{
  AuthenticationUseCase,
  EmailAuthenticationUseCase
}
import com.github.BambooTuna.AkkaServerSupport.core.session.Session
import com.github.BambooTuna.MarketServer.ErrorResponseJson
import com.github.BambooTuna.MarketServer.json.{
  PasswordInitializationRequestJsonImpl,
  SignInRequestJsonImpl,
  SignUpRequestJsonImpl
}
import com.github.BambooTuna.MarketServer.model.account.UserCredentialsImpl
import io.circe.generic.auto._
import monix.execution.Scheduler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.{Content, Schema}
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import javax.ws.rs._

class AuthenticationControllerImpl(
    val authenticationUseCase: AuthenticationUseCase[SignUpRequestJsonImpl,
                                                     SignInRequestJsonImpl,
                                                     UserCredentialsImpl],
    val emailAuthenticationUseCase: EmailAuthenticationUseCase[
      UserCredentialsImpl]
)(implicit session: Session[String, SessionToken])
    extends AuthenticationController[SignUpRequestJsonImpl,
                                     SignInRequestJsonImpl,
                                     PasswordInitializationRequestJsonImpl,
                                     UserCredentialsImpl] {

  @Path("/signup")
  @Consumes(Array("application/json"))
  @Produces(Array("application/json"))
  @POST
  @Operation(
    summary = "Sign Up",
    description = "",
    requestBody = new RequestBody(
      content = Array(
        new Content(
          schema = new Schema(name = "SignUpRequestJson",
                              title = "SignUpRequestJson",
                              implementation = classOf[SignUpRequestJsonImpl],
                              required = true)))),
    responses = Array(
      new ApiResponse(
        responseCode = "200",
        description = "OK",
        headers = Array(
          new Header(name = "Set-Authorization",
                     description = "セッショントークン、以降これを使って認証を行う。",
                     schema = new Schema(name = "String")))
      ),
      new ApiResponse(
        responseCode = "400",
        description = "BadRequest",
        content = Array(
          new Content(
            schema = new Schema(name = "ErrorResponseJson",
                                implementation = classOf[ErrorResponseJson])))
      ),
      new ApiResponse(responseCode = "500",
                      description = "Internal server error")
    )
  )
  override def signUpRoute(implicit s: Scheduler): QueryP[Unit] =
    super.signUpRoute

  override def issueActivateCodeRoute(implicit s: Scheduler): QueryP[Unit] =
    super.issueActivateCodeRoute

  override def activateAccountRoute(
      implicit s: Scheduler): QueryP[Tuple1[String]] =
    super.activateAccountRoute

  override def signInRoute(implicit s: Scheduler): QueryP[Unit] =
    super.signInRoute

  override def tryInitializationRoute(implicit s: Scheduler): QueryP[Unit] =
    super.tryInitializationRoute

  override def initAccountPassword(
      implicit s: Scheduler): QueryP[Tuple1[String]] = super.initAccountPassword

  override def healthCheck: QueryP[Unit] = super.healthCheck

  override def logout: QueryP[Unit] = super.logout

}
