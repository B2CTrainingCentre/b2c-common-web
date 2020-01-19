package com.b2c.gated.module

import com.b2c.gated.service.{ApiService, ApiServiceImpl}
import com.google.inject.AbstractModule

class ApiModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[ApiService])
      .to(classOf[ApiServiceImpl])
  }
}
