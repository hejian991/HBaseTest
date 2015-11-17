package com.waimai.monitor.service

import com.google.inject.{Inject, Singleton}
import net.csdn.common.logging.Loggers
import net.csdn.common.settings.Settings
import com.waimai.monitor.service.hbase.HbaseTool
import java.util

import net.liftweb.{json => SJSon}
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.util.Bytes
import scala.collection.JavaConversions._

@Singleton
class QueryService @Inject()(settings: Settings, hbase: HbaseTool) {
  val logger = Loggers.getLogger(classOf[QueryService])


  //  def innerList(params: util.Map[String, String]) = {
  //    val scan = new Scan()
  //    val startRow = params.get("startRow").split(";")
  //    val endRow = params.get("endRow").split(";")
  //    scan.setStartRow(Bytes.toBytes( s"""${startRow(1)};${startRow(0)}${if (startRow.length > 2) ";" + startRow(2) else ""}${if (startRow.length > 3) ";" + startRow(3) else ""}"""))
  //    scan.setStopRow(Bytes.toBytes( s"""${endRow(1)};${endRow(0)}${if (endRow.length > 2) ";" + endRow(2) else ""}${if (endRow.length > 3) ";" + endRow(3) else ""}"""))
  //    /*
  //    20140613;etl;ETL_FillDataProcessor;FDP_Enter
  //     */
  //    val items = hbase.find("program-stats2", scan, new RowMapper[Map[String, String]] {
  //      override def mapRow(result: Result, rowNum: Int): Map[String, String] = {
  //        val rowKey = Bytes.toString(result.getRow)
  //        logger.debug(rowKey)
  //        rowKey.split(";") match {
  //          case Array(project, date, module, key) => Map("date" -> date, "project" -> project, "module" -> module, "key" -> key, "value" -> Bytes.toLong(result.getValue(Bytes.toBytes("f"), Bytes.toBytes("c"))).toString)
  //          case _ => Map()
  //        }
  //
  //      }
  //    }).filter(f => f.size > 0).groupBy(_.get("project")).map {
  //      project =>
  //        (project._1, project._2.groupBy(_.get("module")).map {
  //          module =>
  //            module._2.sortBy(_.get("date")).reverse
  //        })
  //    }
  //    logger.info(s"hbase program-stats2 记录数目=> ${items.size}")
  //    items
  //  }

  // http://$host:$port/query?_client_=machine_to_logids&machine=$machine&ts=$ts
  def b(key: String): Array[Byte] = {
    Bytes.toBytes(key)
  }

  implicit val formats = SJSon.Serialization.formats(SJSon.NoTypeHints)

  def innerList(params: util.Map[String, String]) = {

    val startRow = params.get("machine") + ";" + params.get("ts")
    val scan = new Scan(Bytes.toBytes(startRow), Bytes.toBytes(startRow + "~"))
    //    scan.addColumn(b("f"), b("EventAcceptedCount"))


    val lres = hbase.findByScan(QueryService.metrics1, scan)

    val finalRe = lres.filter(f => f.size > 0).map { f =>
      val extMap = f("rowkey").split(";") match {
        case Array(machine, ts, logId) => Map("machine" -> machine, "ts" -> ts, "logId" -> logId)
        case _ => Map()
      }
      extMap ++ f
    }.filter(f => (f.contains("machine") && f.contains("ts") && f.contains("logId"))).groupBy(_.get("machine").get).map {
      machineMap =>
        (machineMap._1, machineMap._2.groupBy(_.get("logId").get).map {
          logIdMap =>
            (logIdMap._1, logIdMap._2.groupBy(_.get("ts").get).map {
              tsMap =>
                (tsMap._1, tsMap._2.map { f =>
                  f.--(Array("rowkey", "machine", "ts", "logId"))
                })
            })
        })
    }

    finalRe
    val str = SJSon.Serialization.write(finalRe)
    println(str)
  }


}

object QueryService {
  val metrics1 = "flume_machine_port_datatime_logid_metrics"
}