package com.waimai.monitor.service.hbase;

import org.apache.hadoop.hbase.client.Result;


/**
 * 3/8/14 WilliamZhu(allwefantasy@gmail.com)
 */
public interface RowMapper<T> {
    T mapRow(Result result, int rowNum) throws Exception;
    
}
