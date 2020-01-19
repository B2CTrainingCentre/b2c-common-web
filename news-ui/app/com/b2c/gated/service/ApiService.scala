package com.b2c.gated.service

import play.api.libs.ws.WSResponse
import play.api.mvc.Headers

import scala.concurrent.Future

trait ApiService {

  def getApiResult(api: String, apiToken: String, header: Headers, reqRefId: String): Future[WSResponse]

  def postApiResult(api: String, payload: String, apiToken: String, header: Headers, reqRefId: String): Future[WSResponse]

  def putApiResult(api: String, payload: String, apiToken: String, header: Headers, reqRefId: String): Future[WSResponse]

  def deleteApiResult(api: String, apiToken: String, header: Headers, reqRefId: String): Future[WSResponse]
}
