package com.waimai.monitor.service.hbase;

//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

import java.util.List;
import java.util.Map;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

@Singleton
public class HbaseService {
  @Inject
  private HbaseTool hbaseTool;

  private final String userTableName = "job_user_info";
  private final String familyName = "f";
  private final String userRevenueTable = "task_contribute_degree";


  public List<Map<String, String>> getUserRevenue(String tag_md5, List<String> fields) {
//    	tag_md5 +";;" +username
    Scan scan = new Scan(Bytes.toBytes(tag_md5), Bytes.toBytes(tag_md5 + ";;~"));
    List<Map<String, String>> lres = hbaseTool.findByScan(userRevenueTable, scan);
    for (int i = 0; i < lres.size(); i++) {
      Map<String, String> map = lres.get(i);
      if (map.size() == 0 || (map.size() == 1 && map.containsKey("rowkey"))) {
        lres.remove(i);
        i--;
      }
    }
    return lres;
  }


}
