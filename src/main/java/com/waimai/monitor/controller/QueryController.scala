package com.waimai.monitor.controller

import java.util
import java.util.UUID

import com.google.inject.Inject
import com.waimai.monitor.strategy.DebugTrait
import net.csdn.annotation.rest.At
import net.csdn.modules.http.{ApplicationController, RestRequest, ViewType}
import net.sf.json.JSONObject
import org.joda.time.DateTime
import serviceframework.dispatcher.StrategyDispatcher

import scala.collection.JavaConversions._


class QueryController extends ApplicationController with JsonpTrait with DebugTrait {


  @At(
    path = Array("/query"),
    types = Array(RestRequest.Method.POST, RestRequest.Method.GET)
  )
  def search = {

    val stragetyParams = buildStragetyParams

    var re = dispatcher.dispatch(stragetyParams)


    if ( debugEnable(stragetyParams)) {
      val temp = JSONObject.fromObject(re)
      temp.put("_debug_", stragetyParams.get("_debug_"))
      re = temp.toString
    }

    if (isEmpty(param("callback"))) render(re)
    else render(jsonp(param("callback"), re), ViewType.string)

  }


  @At(
    path = Array("/reload/config"),
    types = Array(RestRequest.Method.PUT)
  )
  def realod = {
    dispatcher.reload
    render(200, "已经重启本机配置文件", ViewType.string)
  }


  def buildStragetyParams = {
    val stragetyParams = new java.util.HashMap[Any, Any]()
    stragetyParams.putAll(params())
    val tempFields: util.List[String] = new util.ArrayList[String]()


    stragetyParams.put("_http_request_", request)
    stragetyParams.put("_debug_", new util.HashMap[String, AnyRef]())
    stragetyParams.put("_operateTimeLog_", new util.HashMap[String, AnyRef]())
    stragetyParams.put("_reqKey_", new DateTime().toString("yyyyMMdd HHmmss.SSS") + "-" + UUID.randomUUID.getMostSignificantBits)

    if (request.method() == RestRequest.Method.POST && isEmpty(param("sql"))) {
      val query = JSONObject.fromObject(request.contentAsString())
      val from = if (query.containsKey("from")) query.getInt("from") else 0
      val size = if (query.containsKey("size")) query.getInt("size") else 10
      val sort = if (query.containsKey("sort")) query.getJSONObject("sort").map(f => (f._1.asInstanceOf[String], f._2.asInstanceOf[String])).toMap else Map[String, String]()
      val fields = if (query.containsKey("fields")) query.getJSONArray("fields").map(f => f.asInstanceOf[String]).toList else List[String]()
      val highlight = if (query.containsKey("highlight")) query.getJSONObject("highlight") else new JSONObject()

      stragetyParams.put("_fields_", fields)
      stragetyParams.put("highlight", highlight)

      tempFields.addAll(fields)
    } else {
      tempFields.addAll(param("fields", "").split(",").filter(f => !f.isEmpty).toList)
      stragetyParams.put("_fields_", tempFields.toList)
    }
    stragetyParams.put("_tempFields_", tempFields)
    stragetyParams
  }


  @Inject
  var dispatcher: StrategyDispatcher[String] = _
}
