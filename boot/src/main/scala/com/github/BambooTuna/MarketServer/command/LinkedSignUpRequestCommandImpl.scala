package com.github.BambooTuna.MarketServer.command

import com.github.BambooTuna.AkkaServerSupport.authentication.command.LinkedSignUpRequestCommand
import com.github.BambooTuna.MarketServer.SystemSettings
import com.github.BambooTuna.MarketServer.model.LinkedUserCredentialsImpl

case class LinkedSignUpRequestCommandImpl(serviceId: String,
                                          serviceName: String)
    extends LinkedSignUpRequestCommand[LinkedUserCredentialsImpl] {
  require(serviceId.nonEmpty && serviceName.nonEmpty)
  override def createLinkedUserCredentials: LinkedUserCredentialsImpl =
    LinkedUserCredentialsImpl(SystemSettings.generateId(),
                              serviceId,
                              serviceName)
}
