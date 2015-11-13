package test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


public class HbaseTest {

  public static void main(String[] args) throws IOException {

    Configuration config = HBaseConfiguration.create();
    config.set("hbase.zookeeper.quorum", "nj02-lbs-impala4.nj02.baidu.com");
    config.set("hbase.zookeeper.property.clientPort", "8181");



    HTable table = new HTable(config, "a");
    Put p = new Put(Bytes.toBytes("myLittleRow"));
    p.add(Bytes.toBytes("f"), Bytes.toBytes("q1"),
        Bytes.toBytes("Value1"));
    table.put(p);


    Get g = new Get("myLittleRow".getBytes());
    System.out.println(table.exists(g));



  }


}
