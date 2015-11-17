package com.waimai.monitor.service.hbase;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;


/**
 * 3/8/14 WilliamZhu(allwefantasy@gmail.com)
 */
public interface ResultsExtractor<T> {
    T extractData(ResultScanner results) throws Exception;
    
    T extractData(Result[] results) throws Exception;
}
