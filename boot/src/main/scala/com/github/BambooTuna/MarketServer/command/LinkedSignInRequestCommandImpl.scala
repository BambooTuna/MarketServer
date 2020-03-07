package com.github.BambooTuna.MarketServer.command

import com.github.BambooTuna.AkkaServerSupport.authentication.command.LinkedSignInRequestCommand
import com.github.BambooTuna.MarketServer.model.LinkedUserCredentialsImpl

case class LinkedSignInRequestCommandImpl(serviceId: String,
                                          serviceName: String)
    extends LinkedSignInRequestCommand[LinkedUserCredentialsImpl]
