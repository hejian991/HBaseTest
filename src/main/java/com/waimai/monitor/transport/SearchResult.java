//package com.waimai.monitor.data;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//
//
//import net.csdn.service.transport.data.SearchHit;
//
//public class SearchResult {
//  private List<SearchHit> datas;
//  //索引命中条数
//  private int total;
//
//  //实际返回最大条数
//  private int total_hits = 0;
//  private int fetch_size;
//
//  public SearchResult(List<SearchHit> datas, int total, int fetch_size) {
//    this.datas = datas;
//    this.total = total;
//    this.fetch_size = fetch_size;
//  }
//
//  public void merger(SearchResult sr){
//    HashSet<Integer> hs = new HashSet<Integer>() ;
//    for (SearchHit sh : datas) {
//      hs.add(sh.getDoc()) ;
//    }
//    int same = 0  ;
//    for (SearchHit sh : sr.datas) {
//      if(hs.contains(sh.getDoc())){
//        same++ ;
//      }else{
//        this.datas.add(sh) ;
//      }
//    }
//    this.total = sr.total ;
//    this.total_hits += sr.total_hits-same ;
//  }
//
//
//  public void mergerStaggered(SearchResult sr) {
//    // TODO Auto-generated method stub
//    HashSet<Integer> hs = new HashSet<Integer>() ;
//    for (SearchHit sh : datas) {
//      hs.add(sh.getDoc()) ;
//    }
//
//    int same = 0  ;
//
//    for (int i = 0; i < sr.datas.size(); i++) {
//      SearchHit searchHit = sr.datas.get(i) ;
//      if(hs.contains(searchHit.getDoc())){
//        same++ ;
//      }else{
//        if(i==0&&this.datas.size()>1){
//          this.datas.add(1,searchHit) ;
//        }else{
//          this.datas.add(searchHit) ;
//        }
//      }
//
//    }
//    this.total = sr.total ;
//    this.total_hits += sr.total_hits-same ;
//  }
//
//
//  public SearchResult total(int num) {
//    total += num;
//    return this;
//  }
//
//  public SearchResult datas(List<SearchHit> searchHits) {
//    datas.addAll(searchHits);
//    return this;
//  }
//
//  public SearchResult datas(SearchHit searchHit) {
//    datas.add(searchHit);
//    return this;
//  }
//
//  public SearchResult merge(SearchResult searchResult) {
//
//    List<SearchHit> tempSearchHits = new ArrayList<SearchHit>();
//
//    int len = searchResult.getDatas().size() > datas.size() ? datas.size() : searchResult.getDatas().size();
//    for (int i = 0; i < len; i++) {
//      tempSearchHits.add(datas.get(i));
//      tempSearchHits.add(searchResult.getDatas().get(i));
//    }
//
//    if (datas.size() > len) {
//      tempSearchHits.addAll(datas.subList(len, datas.size()));
//    }
//
//    if (searchResult.getDatas().size() > len) {
//      tempSearchHits.addAll(searchResult.getDatas().subList(len, searchResult.getDatas().size()));
//    }
//
//    datas.clear();
//    datas.addAll(tempSearchHits);
//
//    total += searchResult.getTotal();
//    return this;
//  }
//
//
//  //get and set
//
//  public List<SearchHit> getDatas() {
//    return datas;
//  }
//
//  public void setDatas(List<SearchHit> datas) {
//    this.datas = datas;
//  }
//
//  public int getTotal() {
//    return total;
//  }
//
//  public void setTotal(int total) {
//    this.total = total;
//  }
//
//  public int getFetch_size() {
//    return fetch_size;
//  }
//
//  public void setFetch_size(int fetch_size) {
//    this.fetch_size = fetch_size;
//  }
//
//  public int getTotal_hits() {
//    return total_hits;
//  }
//
//  public void setTotal_hits(int total_hits) {
//    this.total_hits = total_hits;
//  }
//
//
//}
