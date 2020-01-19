package com.b2c.gated.service

import com.google.inject.Inject
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSResponse}
import play.api.mvc.Headers

import scala.concurrent.Future

class ApiServiceImpl @Inject()(wsClient: WSClient, configuration: play.api.Configuration) extends ApiService {

  val backendUrl = configuration.underlying.getString("backend.api.url")

  def getApiResult(api: String, apiToken: String, header: Headers, reqRefId: String): Future[WSResponse] = {


    wsClient.url(backendUrl + api).addHttpHeaders("X-REQUESTED-FOR" -> "23324", "apiToken" -> apiToken).get()
  }

  def postApiResult(api: String, payload: String, apiToken: String, header: Headers, reqRefId: String): Future[WSResponse] = {

    wsClient.url(backendUrl + api).addHttpHeaders("X-REQUESTED-FOR" -> "23324", "apiToken" -> apiToken).post(Json.parse(payload))
  }

  def putApiResult(api: String, payload: String, apiToken: String, header: Headers, reqRefId: String): Future[WSResponse] = {
    wsClient.url(backendUrl + api).addHttpHeaders("X-REQUESTED-FOR" -> "23324", "apiToken" -> apiToken).put(Json.parse(payload))
  }

  def deleteApiResult(api: String, apiToken: String, header: Headers, reqRefId: String): Future[WSResponse] = {
    wsClient.url(backendUrl + api).addHttpHeaders("X-REQUESTED-FOR" -> "23324", "apiToken" -> apiToken).delete()
  }
}
