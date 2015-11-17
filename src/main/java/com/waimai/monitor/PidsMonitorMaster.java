package com.waimai.monitor;

import net.csdn.ServiceFramwork;
import net.csdn.bootstrap.Application;


public class PidsMonitorMaster extends Application {
  public static void main(String[] args) {
    ServiceFramwork.scanService.setLoader(PidsMonitorMaster.class);
//        ServiceFramwork.registerStartWithSystemServices(MappingAndShardsService.class);
    Application.main(args);
  }
}
