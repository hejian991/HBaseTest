//package com.waimai.monitor.util;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.google.inject.Singleton;
//import net.csdn.cluster.routing.Shard;
//import net.csdn.common.collections.WowCollections;
//import net.csdn.common.logging.CSLogger;
//import net.csdn.common.logging.Loggers;
//import net.csdn.service.search.SearchService;
//import net.csdn.service.search.util.SearchResult;
//import net.csdn.service.transport.data.SearchHit;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.apache.lucene.search.SortField;
//import com.waimai.monitor.data.SearchResult;
//
//import java.lang.reflect.Field;
//import java.util.*;
//
//import static net.csdn.common.collections.WowCollections.getInt;
//import static net.csdn.modules.http.JSONObjectUtils.getJSONArray;
//
///**
// * 12/1/14 WilliamZhu(allwefantasy@gmail.com)
// */
//@Singleton
//public class DataTransformUtil {
//    private List<String> list_field = WowCollections.list("all", "simple", "static", "dynamic", "static_simple");
//    private CSLogger logger = Loggers.getLogger(getClass());
//
//    public SearchResult toObject(JSONObject object) {
//
//        JSONArray datas = getJSONArray(object, "datas");
//
//        if (datas == null) {
//            return new SearchResult(new ArrayList<SearchHit>(0), 0, 50);
//        }
//        List<SearchHit> searchHits = new ArrayList<SearchHit>(datas.size());
//
//        for (int i = 0; i < datas.size(); i++) {
//            JSONObject obj = datas.getJSONObject(i);
//            SearchHit docWrapper = (SearchHit) JSONObject.toBean(obj, SearchHit.class);
//            searchHits.add(docWrapper);
//        }
//        return new SearchResult(searchHits, getInt(object, "total"), 50);
//    }
//
//    public SearchResult toObjectForTest(JSONObject object) {
//
//        JSONArray datas = getJSONArray(object, "hits");
//
//        if (datas == null) {
//            return new SearchResult(new ArrayList<SearchHit>(0), 0, 50);
//        }
//        List<SearchHit> searchHits = new ArrayList<SearchHit>(datas.size());
//
//        for (int i = 0; i < datas.size(); i++) {
//            JSONObject obj = datas.getJSONObject(i);
//            SearchHit docWrapper = (SearchHit) JSONObject.toBean(obj, SearchHit.class);
//            docWrapper.setShard(new Shard());
//            docWrapper.set_uid("csdn#"+docWrapper.get_id());
//            searchHits.add(docWrapper);
//        }
//        return new SearchResult(searchHits, getInt(object, "total"), 50);
//    }
//    public boolean containKeyField(List<String> fields) {
//        if (fields == null || fields.size() == 0) return false;
//        for (String field : fields) {
//            if (list_field.contains(field) || field.startsWith("gf_")) return true;
//        }
//        return false;
//    }
//    public Map forClient(final SearchResult searchResult, List<String> fields) {
//        Map maps = Maps.newHashMap();
//
//        boolean is_key_field = containKeyField(fields);
//        if (fields != null && fields.size() > 0 && searchResult != null) {
//            List<SearchHit> searchHits = searchResult.getDatas();
//            if (!is_key_field) {
//                for (SearchHit searchHit : searchHits) {
//                    Map map = new HashMap();
//                    for (String field : fields) {
//                        map.put(field, searchHit.getObject().get(field));
//                    }
//                    searchHit.getObject().clear();
//                    searchHit.getObject().putAll(map);
//                }
//            } else {
//                for (SearchHit searchHit : searchHits) {
//                    Map map = new HashMap();
//                    fields = new ArrayList(searchHit.getObject().keySet());
//                    for (String field : fields) {
//                        map.put(field, searchHit.getObject().get(field));
//                    }
//                    searchHit.getObject().clear();
//                    searchHit.getObject().putAll(map);
//                }
//            }
//        }
//
//
//        maps.put("hits", search3(searchResult, new SearchService.SFieldSelector() {
//            private String[] fields = new String[]{"object", "_type", "_id", "_index", "fields"};
//
//            @Override
//            public boolean accept(String value) {
//                for (String str : fields) {
////                    System.out.println("fields: " +str);
//                    if (str.equals(value)) return true;
//                }
//                return false;
//            }
//        }));
//
//
//        maps.put("total", searchResult.getTotal());
//        maps.put("total_hits", searchResult.getTotal_hits());
//        return maps;
//    }
//
//
//    public String forClientAsString(final SearchResult searchResult, List<String> fields, Object debugData) {
//
//        JSONObject jsonObject = JSONObject.fromObject(forClient(searchResult, fields));
//        if (debugData != null) {
//            jsonObject.put("_debug_", debugData);
//        }
//
//        return jsonObject.toString();
//    }
//
//    public List<Map> search3(SearchResult searchResult,
//                             SearchService.SFieldSelector fieldSelector) {
//        List<Map> maps = new ArrayList<Map>(searchResult.getDatas().size());
//        List<SearchHit> searchHits = searchResult.getDatas();
//        for (SearchHit searchHit : searchHits) {
//            Field[] fields = SearchHit.class.getDeclaredFields();
////            System.out.println("search3:fields: " + fields);
//            Map map = new HashMap();
//            for (Field field : fields) {
////                System.out.println("search3: " + field);
//                if (fieldSelector.accept(field.getName())) {
//                    field.setAccessible(true);
//                    try {
//                        map.put(field.getName(), field.get(searchHit));
//                    } catch (IllegalAccessException e) {
//
//                    }
//
//                }
//            }
//            maps.add(map);
//
//        }
//        return maps;
//    }
//
//    public void sort(List<SearchHit> list) {
//        List topDocs = Lists.newArrayList();
//        List normalDocs = Lists.newArrayList();
//        for (SearchHit searchHit : list) {
//            if (searchHit.isTopHit()) {
//                topDocs.add(searchHit);
//            } else {
//                normalDocs.add(searchHit);
//            }
//        }
//        innerScoreSort(topDocs);
//        innerScoreSort(normalDocs);
//        topDocs.addAll(normalDocs);
//        list.clear();
//        list.addAll(topDocs);
//    }
//
//
//    public void sort(List<SearchHit> list, final SortField[] fields) {
//        logger.info("共有" + list.size() + "条记录参与排序;排序字段:" + fields);
//        List topDocs = Lists.newArrayList();
//        List normalDocs = Lists.newArrayList();
//        for (SearchHit searchHit : list) {
//            if (searchHit.isTopHit()) {
//                topDocs.add(searchHit);
//            } else {
//                normalDocs.add(searchHit);
//            }
//        }
//        innerFieldSort(topDocs, fields);
//        innerFieldSort(normalDocs, fields);
//        topDocs.addAll(normalDocs);
//        list.clear();
//        list.addAll(topDocs);
//    }
//
//    private void innerScoreSort(List<SearchHit> list) {
//        Collections.sort(list, new Comparator<SearchHit>() {
//            @Override
//            public int compare(SearchHit docWrapperA, SearchHit docWrapperB) {
//                double temp = docWrapperA.getScore() - docWrapperB.getScore();
//                return temp == 0 ? 0 : (temp > 0 ? -1 : 1);
//            }
//        });
//    }
//
//
//    private void innerFieldSort(List<SearchHit> list, final SortField[] fields) {
//        Collections.sort(list, new Comparator<SearchHit>() {
//            @Override
//            public int compare(SearchHit docWrapperA, SearchHit docWrapperB) {
//                final int n = fields.length;
//                int c = 0;
//                for (int i = 0; i < n && c == 0; ++i) {
//                    final int type = fields[i].getType();
//                    if (type == SortField.STRING) {
//                        final String s1 = String.valueOf(docWrapperA.getFields()[i]);
//                        final String s2 = String.valueOf(docWrapperB.getFields()[i]);
//                        // null values need to be sorted first, because of how FieldCache.getStringIndex()
//                        // works - in that routine, any documents without a value in the given field are
//                        // put first.  If both are null, the next SortField is used
//                        if (s1 == null) {
//                            c = (s2 == null) ? 0 : -1;
//                        } else if (s2 == null) {
//                            c = 1;
//                        } else if (fields[i].getLocale() == null) {
//                            c = s1.compareTo(s2);
//                        }
//                    } else {
//                        c = docWrapperA.getFields()[i].compareTo(docWrapperB.getFields()[i]);
//                    }
//                    // reverse sort
//                    if (fields[i].getReverse()) {
//                        c = -c;
//                    } else if (fields[i].getType() == SortField.SCORE) {
//                        c = -c;
//                    }
//                }
//                //如果所有的域都无法相同，那么只能根据doc进行比较了
//                if (c == 0) {
//                    return docWrapperA.getDoc() - docWrapperB.getDoc();
//                }
//                return c;
//            }
//        });
//    }
//}
