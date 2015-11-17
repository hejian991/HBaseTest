package com.waimai.monitor.controller

/**
 * Created by hejian on 2015/01/12.
 */
trait JsonpTrait {

  def jsonp(callback: String, result: Any) = {
    val sb = new StringBuilder(callback)
    sb.append("(");
    sb.append(result.toString);
    sb.append(")");
    sb.toString()
  }

}
