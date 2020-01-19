package com.b2c.gated.controllers

import java.time.LocalDateTime

import com.b2c.gated.service.ApiService
import javax.inject.{Inject, Singleton}
import play.api.mvc._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration


@Singleton
class ApiController @Inject()(cc: ControllerComponents, apiService: ApiService, configuration: play.api.Configuration) extends AbstractController(cc) {

  val apiToken = configuration.underlying.getString("api.token")

  // Create a simple 'ping' endpoint for now, so that we
  // can get up and running with a basic implementation
  def ping = Action { implicit request =>
    Ok("PONG!")
  }

  def postApi(path: String) = Action { implicit request =>

    val reqRefId = s"${LocalDateTime.now()}"
    val payload = request.body.asJson.get.toString()
    var response = apiService.postApiResult(path, payload, apiToken, request.headers, reqRefId)
    var resObj = Await.ready(response, Duration.Inf).value.get.get
    Status(resObj.status)(resObj.body)
  }

  def putApi(path: String): Action[AnyContent] = Action { request =>

    val reqRefId = s"${LocalDateTime.now()}"
    val payload = request.body.asJson.get.toString()
    var response = apiService.putApiResult(path, payload, apiToken, request.headers, reqRefId)
    var resObj = Await.ready(response, Duration.Inf).value.get.get
    Status(resObj.status)(resObj.body)
  }

  def getApi(path: String): Action[AnyContent] = Action { request =>

    val reqRefId = s"${LocalDateTime.now()}"
    var response = apiService.getApiResult(path, apiToken, request.headers, reqRefId)
    var resObj = Await.ready(response, Duration.Inf).value.get.get
    Status(resObj.status)(resObj.body)
  }

  def deleteApi(path: String): Action[AnyContent] = Action.async { request =>

    val reqRefId = "Check1234"
    var response = apiService.deleteApiResult(path, apiToken, request.headers, reqRefId)
    response.map({ obj =>
      Status(obj.status)(obj.body)
    })
  }

  def index() = Action { request =>
    Ok(views.html.index()).withCookies(Cookie("result", "success"))

  }

  def login(path: String) = Action { implicit request =>

    val reqRefId = s"${LocalDateTime.now()}"
    val payload = request.body.asJson.get.toString()
    var response = apiService.postApiResult(path, payload, apiToken, request.headers, reqRefId)
    var resObj = Await.ready(response, Duration.Inf).value.get.get
    Status(resObj.status)(resObj.body).withCookies(Cookie("empId", "12", httpOnly = true, secure = true), Cookie("employeeName", "ABCD", httpOnly = true, secure = true))
  }
}
