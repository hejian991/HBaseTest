//package com.waimai.monitor.strategy
//
//import java.util
//
//import net.csdn.common.logging.Loggers
//import net.csdn.modules.transport.HttpTransportService
//import serviceframework.dispatcher.Compositor
//
///**
// * hj
// */
//class BiStatOverviewCompositor[T >: SearchResult] extends Compositor[T] with DebugTrait with InstanceMethodsTrait {
//
//
//  private var _configParams: util.List[util.Map[Any, Any]] = _
//  val logger = Loggers.getLogger(classOf[BiStatOverviewCompositor[T]])
//
//  def httpTransportService = findService(classOf[HttpTransportService])
//
//  val dgService = findService(classOf[DgService])
//
//  def settings = findService(classOf[Settings])
//
//  def initialize(typeFilters: util.List[String], configParams: util.List[util.Map[Any, Any]]): Unit = {
//    this._configParams = configParams
//  }
//
//
//
//  def result(alg: util.List[Processor[T]], ref: util.List[Strategy[T]], middleResult: util.List[T], params: util.Map[Any, Any]): util.List[T] = {
//
//    val hits = new util.ArrayList[SearchHit]
//
//    val ids = paramAsString(params)("ids", "")
//    val typ = paramAsString(params)("_type", "")
//
//    ids.split(",").foreach{
//      id=>
//        val hit = new SearchHit
//        val obj = new util.HashMap[String,String]()
//        obj.put("source_type", typ)
//        hit.setObject(obj)
//        hit.set_id(id)
//        hits.add(hit)
//    }
//
//    val searchR = new SearchResult(hits, hits.size(), 1200)
//    searchR.setTotal_hits(hits.size())
//
//
//    List(searchR)
//
//  }
//
//
//
//}
