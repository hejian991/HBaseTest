package com.waimai.monitor.service.hbase;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.csdn.common.logging.CSLogger;
import net.csdn.common.logging.Loggers;
import net.csdn.common.settings.Settings;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;

import java.io.IOException;


@Singleton
public class HTableManager {
  public int HTABLE_POOL_SIZE = 15;
  private HTablePool hTablePool;
  private CSLogger logger = Loggers.getLogger(HTableManager.class);

  @Inject
  public HTableManager(Settings settings) {
    if (settings.getAsBoolean("hbase.zookeeper.disable", false)) {
      logger.info("hbase 被设置为禁用，HTableManager 不会启用");
      return;
    }
    Configuration config = HBaseConfiguration.create();
    config.set("hbase.zookeeper.quorum", settings.get("zk.quorum", "nj02-lbs-impala4.nj02.baidu.com"));
    config.set("hbase.zookeeper.property.clientPort", settings.get("zk.clientPort", "8181"));


    hTablePool = new HTablePool(config, settings.getAsInt("hbase.pool_size", HTABLE_POOL_SIZE));


  }


  public synchronized HTableInterface getHTable(String tableName) {
    return hTablePool.getTable(tableName);
  }


  public <T> T execute(String tableName, OperateCallback<T> operateCallback) {
    HTableInterface hTableInterface = getHTable(tableName);
    try {
      return operateCallback.operate(hTableInterface);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        hTableInterface.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }


  public interface OperateCallback<T> {
    public <T> T operate(HTableInterface hTable) throws Exception;
  }


}

