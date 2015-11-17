package com.waimai.monitor.service.hbase;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;

import java.util.ArrayList;
import java.util.List;


/**
 * 3/8/14 WilliamZhu(allwefantasy@gmail.com)
 */
public class RowMapperResultsExtractor<T> implements ResultsExtractor<List<T>> {


    private final RowMapper<T> rowMapper;


    public RowMapperResultsExtractor(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }


    public List<T> extractData(ResultScanner results) throws Exception {
        List<T> rs = new ArrayList<T>();
        int rowNum = 0;
        for (Result result : results) {
            rs.add(this.rowMapper.mapRow(result, rowNum++));
        }
        return rs;
    }
    
    public List<T> extractData(Result[] results) throws Exception {
        List<T> rs = new ArrayList<T>();
        int rowNum = 0;
        for (Result result : results) {
            rs.add(this.rowMapper.mapRow(result, rowNum++));
        }
        return rs;
    }
}
