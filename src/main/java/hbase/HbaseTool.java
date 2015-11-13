package hbase;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import hbase.extractor.ResultsExtractor;
import hbase.extractor.RowMapper;
import hbase.extractor.RowMapperResultsExtractor;
import net.csdn.common.settings.ImmutableSettings;
import net.csdn.common.settings.Settings;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import util.StringUtil;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


@Singleton
public class HbaseTool {

    private HTableManager tableManager;
    public String DEFAULT_FAMILLY_NAME = "f";

    @Inject
    public HbaseTool(HTableManager tableManager) {
        this.tableManager = tableManager;
    }




  public byte[] b(String key) {
        return Bytes.toBytes(key);
    }


    public <T> T execute(String tableName, HTableManager.OperateCallback<T> operateCallback) {
        return tableManager.execute(tableName, operateCallback);
    }

    public <T> T get(String tableName, final String rowkey, final RowMapper<T> mapper) {
        return tableManager.execute(tableName, new HTableManager.OperateCallback<T>() {
            @Override
            public T operate(HTableInterface hTable) throws Exception {
                Get get = new Get(b(rowkey));
                get.addFamily(b(DEFAULT_FAMILLY_NAME));
                Result result = hTable.get(get);
                return mapper.mapRow(result, 0);
            }
        });
    }

    public <T> T get(String tableName, final String rowkey, final String familyName, final RowMapper<T> mapper) {
        return tableManager.execute(tableName, new HTableManager.OperateCallback<T>() {
            @Override
            public T operate(HTableInterface hTable) throws Exception {
                Get get = new Get(b(rowkey));
                byte[] family = b(DEFAULT_FAMILLY_NAME);

                if (StringUtil.isNotBlank(familyName)) {
                    family = b(familyName);
                }

                get.addFamily(family);
                Result result = hTable.get(get);
                return mapper.mapRow(result, 0);
            }
        });
    }

    public <T> T get(String tableName, final String rowkey, final String familyName, final List<String> qualifiers, final RowMapper<T> mapper) {
        return tableManager.execute(tableName, new HTableManager.OperateCallback<T>() {
            @Override
            public T operate(HTableInterface hTable) throws Exception {
                Get get = new Get(b(rowkey));
                byte[] family = b(DEFAULT_FAMILLY_NAME);

                if (StringUtil.isNotBlank(familyName)) {
                    family = b(familyName);
                }

                get.addFamily(family);

                if (qualifiers != null && qualifiers.size() > 0) {
                    for(String qualifier : qualifiers){
                        get.addColumn(family, b(qualifier));
                    }
                }

                Result result = hTable.get(get);
                return mapper.mapRow(result, 0);
            }
        });
    }

    public <T> T get(String tableName, final List<String> rowkeys, final ResultsExtractor<T> action) {
        return tableManager.execute(tableName, new HTableManager.OperateCallback<T>() {
            @Override
            public T operate(HTableInterface hTable) throws Exception {
                List<Get> rowkeyList = new ArrayList<Get>();
                byte[] family = b(DEFAULT_FAMILLY_NAME);

                for(String rowkey : rowkeys){
                    Get get = new Get(b(rowkey));
                    get.addFamily(family);
                    rowkeyList.add(get);
                }
                Result[] results = hTable.get(rowkeyList);
                return action.extractData(results);
            }
        });
    }

    public <T> T get(String tableName, final List<String> rowkeys, final String familyName, final ResultsExtractor<T> action) {
        return tableManager.execute(tableName, new HTableManager.OperateCallback<T>() {
            @Override
            public T operate(HTableInterface hTable) throws Exception {
                List<Get> rowkeyList = new ArrayList<Get>();
                byte[] family = b(DEFAULT_FAMILLY_NAME);

                if (StringUtil.isNotBlank(familyName)) {
                    family = b(familyName);
                }

                for(String rowkey : rowkeys){
                    Get get = new Get(b(rowkey));
                    get.addFamily(family);
                    rowkeyList.add(get);
                }
                Result[] results = hTable.get(rowkeyList);
                return action.extractData(results);
            }
        });
    }

    public <T> T get(String tableName, final List<String> rowkeys, final String familyName, final List<String> qualifiers, final ResultsExtractor<T> action) {
        return tableManager.execute(tableName, new HTableManager.OperateCallback<T>() {
            @Override
            public T operate(HTableInterface hTable) throws Exception {
                List<Get> rowkeyList = new ArrayList<Get>();
                byte[] family = b(DEFAULT_FAMILLY_NAME);

                if (StringUtil.isNotBlank(familyName)) {
                    family = b(familyName);
                }

                for(String rowkey : rowkeys){
                    Get get = new Get(b(rowkey));
                    get.addFamily(family);
                    if (qualifiers != null && qualifiers.size() > 0) {
                        for(String qualifier : qualifiers){
                            get.addColumn(family, b(qualifier));
                        }
                    }
                    rowkeyList.add(get);
                }
                Result[] results = hTable.get(rowkeyList);
                return action.extractData(results);
            }
        });
    }

    public <T> T get(String tableName, final Scan scan, final ResultsExtractor<T> action) {

        return tableManager.execute(tableName, new HTableManager.OperateCallback<T>() {
            @Override
            public T operate(HTableInterface hTable) throws Exception {
                ResultScanner scanner = hTable.getScanner(scan);
                try {
                    return action.extractData(scanner);
                } finally {
                    scanner.close();
                }
            }
        });
    }

    public <T> List<T> get(String tableName, final List<String> rowNames, final RowMapper<T> mapper) {
        return get(tableName, rowNames, new RowMapperResultsExtractor<T>(mapper));
    }

    public <T> List<T> get(String tableName, final List<String> rowNames, final String familyName, final RowMapper<T> mapper) {
        return get(tableName, rowNames, familyName, new RowMapperResultsExtractor<T>(mapper));
    }

    public <T> List<T> get(String tableName, final List<String> rowNames, final String familyName, final List<String> qualifierName, final RowMapper<T> mapper) {
        return get(tableName, rowNames, familyName, qualifierName, new RowMapperResultsExtractor<T>(mapper));
    }

    public <T> List<T> get(String tableName, final Scan scan, final RowMapper<T> action) {
        return get(tableName, scan, new RowMapperResultsExtractor<T>(action));
    }

    public String[] getColumnsInColumnFamily(Result r, String ColumnFamily) {

        NavigableMap<byte[], byte[]> familyMap = r.getFamilyMap(Bytes.toBytes(ColumnFamily));
        String[] Quantifers = new String[familyMap.size()];

        int counter = 0;
        for (byte[] bQunitifer : familyMap.keySet()) {
            Quantifers[counter++] = Bytes.toString(bQunitifer);

        }

        return Quantifers;
    }




    /**
     * 删除指定hbase表中是否存在某个rowkey
     *
     * @param tableName hbase表名
     * @param rowkey hbase主键
     *
     * */
    public void delete(final String tableName, final String rowkey) {
        execute(tableName, new HTableManager.OperateCallback<Object>() {
            @Override
            public <T> T operate(HTableInterface hTable) throws Exception {
                hTable.delete(new Delete(b(rowkey)));
                hTable.flushCommits();
                return null;
            }
        });
    }



    /**
     * 将Map 对象 存进 Hbase中的一个便利方法。
     * 参数必须包含rowKey,如果有null值会被忽略。
     * 只支持一个family
     */
    public void save(String tableName, final String family, final Map<String, Object> content) {
        if (!content.containsKey("id") && !content.containsKey("rowkey")) {
            throw new IllegalArgumentException("必须包含rowkey字段");
        }
        if (content.size() == 1) return;
        execute(tableName, new HTableManager.OperateCallback<Object>() {
            @Override
            public <T> T operate(HTableInterface hTable) throws Exception {
                String id = content.containsKey("id") ? content.get("id").toString() : content.get("rowkey").toString();
                Put put = new Put(Bytes.toBytes(id));
                content.remove("id");
                content.remove("rowkey");
                for (Map.Entry<String, Object> entry : content.entrySet()) {
                    if (entry.getValue() == null || entry.getValue().equals("") || entry.getValue().equals("null"))
                        continue;
                    if (entry.getValue() instanceof byte[]) {
                        put.add(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), (byte[]) entry.getValue());
                    } else {
                        put.add(Bytes.toBytes(family), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue().toString()));
                    }

                }
                hTable.put(put);
                hTable.flushCommits();
                return null;
            }
        });
    }

    /**
     * 检查指定hbase表中是否存在某个rowkey
     *
     * @param tableName hbase表名
     * @param rowkey hbase主键
     *
     * @return true/false
     * */
    public boolean exist(String tableName, final String rowkey) {

        final AtomicBoolean flag = new AtomicBoolean(false);

        execute(tableName, new HTableManager.OperateCallback<Object>() {
            @Override
            public <T> T operate(HTableInterface hTable) throws Exception {
                Get get = new Get(Bytes.toBytes(rowkey));
                flag.set(hTable.exists(get));
                return null;
            }
        });

        return flag.get();
    }


    public Map<String, String> KVToMap(KeyValue[] keyvalues) {

        Map<String, String> result = new HashMap<String, String>();
        for (KeyValue keyValue : keyvalues) {
            result.put(Bytes.toString(keyValue.getQualifier()), Bytes.toString(keyValue.getValue()));
        }
        return result;

    }

    /**
     * 查询hbase中指定rowkey的记录
     * @param tableName hbase表名
     * @param rowkey hbase主键
     *
     * @return Map<String, String>
     * */
    public Map<String, String> findByRowkey(String tableName, String rowkey) {

        return get(tableName, rowkey, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(Result result, int rowNum) throws Exception {

                Map<String, String> map = new HashMap<String, String>();
                map.putAll(KVToMap(result.raw()));
                String rowkey = Bytes.toString(result.getRow());
                map.put("rowkey", rowkey);
                return map;
            }
        });
    }

    /**
     * 查询hbase中指定rowkey的记录
     * @param tableName hbase表名
     * @param rowkey hbase主键
     * @param familyName hbase列族
     *
     * @return Map<String, String>
     * */
    public Map<String, String> findByRowkey(String tableName, String rowkey, String familyName) {

        return get(tableName, rowkey, familyName, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(Result result, int rowNum) throws Exception {

                Map<String, String> map = new HashMap<String, String>();
                map.putAll(KVToMap(result.raw()));
                String rowkey = Bytes.toString(result.getRow());
                map.put("rowkey", rowkey);
                return map;
            }
        });
    }


    /**
     * 查询hbase中指定rowkey的记录
     * @param tableName hbase表名
     * @param rowkey hbase主键
     * @param familyName hbase列族
     * @param qualifiers 提取的字段
     *
     * @return Map<String, String>
     * */
    public Map<String, String> findByRowkey(String tableName, String rowkey, String familyName, List<String> qualifiers) {

        return get(tableName, rowkey, familyName, qualifiers, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(Result result, int rowNum) throws Exception {

                Map<String, String> map = new HashMap<String, String>();
                map.putAll(KVToMap(result.raw()));
                String rowkey = Bytes.toString(result.getRow());
                map.put("rowkey", rowkey);
                return map;
            }
        });
    }

    /**
     * 批量查询
     *
     * */
    public List<Map<String, String>> findByRowkeys(String tableName, List<String> rowkeys) {

        return get(tableName, rowkeys, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(Result result, int rowNum) throws Exception {

                Map<String, String> map = new HashMap<String, String>();
                map.putAll(KVToMap(result.raw()));
                String rowkey = Bytes.toString(result.getRow());
                map.put("rowkey", rowkey);
                return map;
            }
        });
    }

    /**
     * 批量查询
     *
     * */
    public List<Map<String, String>> findByRowkeys(String tableName, List<String> rowkeys, String familyName) {

        return get(tableName, rowkeys, familyName, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(Result result, int rowNum) throws Exception {

                Map<String, String> map = new HashMap<String, String>();
                map.putAll(KVToMap(result.raw()));
                String rowkey = Bytes.toString(result.getRow());
                map.put("rowkey", rowkey);
                return map;
            }
        });
    }

    /**
     * 批量查询
     *
     * */
    public List<Map<String, String>> findByRowkeys(String tableName, List<String> rowkeys, String familyName, List<String> qualifierName) {

        return get(tableName, rowkeys, familyName, qualifierName, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(Result result, int rowNum) throws Exception {

                Map<String, String> map = new HashMap<String, String>();
                map.putAll(KVToMap(result.raw()));
                String rowkey = Bytes.toString(result.getRow());
                map.put("rowkey", rowkey);
                return map;
            }
        });
    }

    /**
     * 自定义Scan条件查询
     *
     * */
    public List<Map<String, String>> findByScan(String tableName, Scan scan) {

        return get(tableName, scan, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(Result result, int rowNum) throws Exception {
                Map<String, String> map = new HashMap<String, String>();
                map.putAll(KVToMap(result.raw()));
                String rowkey = Bytes.toString(result.getRow());
                map.put("rowkey", rowkey);
                return map;
            }
        });
    }

  public static void main(String[] args) throws IOException {
    Settings settings = ImmutableSettings.settingsBuilder().build();
    HbaseTool hbaseTool = new HbaseTool(new HTableManager(settings));
    System.out.println(hbaseTool.exist("a", "abc"));

  }

}
