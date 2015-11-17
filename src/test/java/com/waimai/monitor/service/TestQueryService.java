package com.waimai.monitor.service;

import com.waimai.monitor.PidsMonitorMaster;
import net.csdn.common.collections.WowCollections;
import net.csdn.junit.BaseControllerTest;
import org.junit.Test;

import java.util.Map;

public class TestQueryService extends BaseControllerTest {
  static {
    initEnv(PidsMonitorMaster.class);
  }


//  @Test
//  public void testFetchIde() throws Exception {
//
//    Map map = WowCollections.map("ids", "/ansj_seg/README.md,/ansj_seg/License.txt", "type", "ide", "fields", "all");
//
//    RestResponse response = get("/batch/content", map);
//    System.out.println(response.content());
//  }

  //  machine=$machine&ts=$ts
  @Test
  public void testnnerList() throws Exception {

    Map map = WowCollections.map("machine", "8a352edd6e5e8f5ad196a875e1cfef93@8454", "ts", "20151111 14:09");

    QueryService queryService = findService(QueryService.class);
    queryService.innerList(map);
    System.out.println(1);
  }
}
