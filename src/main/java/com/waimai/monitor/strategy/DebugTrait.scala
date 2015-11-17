package com.waimai.monitor.strategy

import java.util


trait DebugTrait {
  def putDebug(params: util.Map[Any, Any],key:String,value:AnyRef) = {
    putDebug2(params,this.toString,key,value)
  }

  def putDebug2(params: util.Map[Any, Any],classKey:String,key:String,value:AnyRef) = {
    if(debugEnable(params)){
      val debugInfo = params.get("_debug_").asInstanceOf[util.Map[String,AnyRef]]
      if(!debugInfo.containsKey(classKey)){
        debugInfo.put(classKey,new util.HashMap[String,AnyRef]())
      }
      debugInfo.get(classKey).asInstanceOf[util.Map[String,AnyRef]].put(key,value)
    }
  }

  def debugEnable(params: util.Map[Any, Any]) = {
    params.containsKey("debug") && params.get("debug").asInstanceOf[String].toBoolean
  }


}
