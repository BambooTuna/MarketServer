package com.github.BambooTuna.MarketServer.swagger

import com.github.BambooTuna.AkkaServerSupport.core.model.ServerConfig
import com.github.swagger.akka.SwaggerHttpService

class SwaggerDocService(serverConfig: ServerConfig, val apis: Set[Class[_]])
    extends SwaggerHttpService {
  override val apiClasses = apis
  override val host = s"${serverConfig.host}:${serverConfig.port}"
  override val apiDocsPath = "api-docs"
  override val schemes = List("http")
}
