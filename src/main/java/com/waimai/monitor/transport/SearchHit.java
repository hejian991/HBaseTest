//package com.waimai.monitor.data;
//
///**
// * Created by hj on 15/11/17.
// */
//
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * User: william
// * Date: 11-9-6
// * Time: 下午4:21
// */
//public class SearchHit {
//
//  private Comparable[] fields;
//  private int doc = -1;
//  private double score = 0;
//  private boolean topHit = false;
//
//
//  private String _type;
//  private String _id;
//  private String _index;
//
//
//  private String _uid = null;
//
//
//  public SearchHit() {
//  }
//
//  private Map<String, Object> object = new HashMap();
//
//  public SearchHit(Comparable[] fields, int doc, double score, boolean topHit, String uid, String _index) {
//    this.fields = fields;
//    this.doc = doc;
//    if (!Double.isInfinite(score) && !Double.isNaN(score))
//      this.score = score;
//    this.topHit = topHit;
//    this._index = _index;
//    this._uid = uid;
//    if (uid != null && uid.contains("#")) {
//      String[] wow = _uid.split("#");
//      this._type = wow[0];
//      this._id = wow[1];
//    }
//  }
//
//  @Override
//  public int hashCode() {
//    return this.shard.hashCode() + this.doc;
//  }
//
//  @Override
//  public boolean equals(Object obj) {
//    if (this == obj) return true;
//    if (obj instanceof SearchHit) {
//      SearchHit fdw = (SearchHit) obj;
//      if (this.doc == fdw.doc && shard.equals(fdw.getShard())) {
//        return true;
//      }
//      return false;
//    }
//    return false;
//  }
//
//
//  //to generate json data you need getter/setter methods
//
//  public String getSourceType() {
//    return (String) this.object.get("source_type");
//  }
//
//  public void setSourceType(String sourceType) {
//    this.object.put("source_type", sourceType);
//  }
//
//  public Comparable[] getFields() {
//    return fields;
//  }
//
//  public void setFields(Comparable[] fields) {
//    this.fields = fields;
//  }
//
//  public int getDoc() {
//    return doc;
//  }
//
//  public void setDoc(int doc) {
//    this.doc = doc;
//  }
//
//  public double getScore() {
//    return score;
//  }
//
//  public void setScore(double score) {
//    this.score = score;
//  }
//
//  public boolean isTopHit() {
//    return topHit;
//  }
//
//  public void setTopHit(boolean topHit) {
//    this.topHit = topHit;
//  }
//
//  public String get_type() {
//    return _type;
//  }
//
//  public void set_type(String _type) {
//    this._type = _type;
//  }
//
//  public String get_id() {
//    return _id;
//  }
//
//  public void set_id(String _id) {
//    this._id = _id;
//  }
//
//  public String get_index() {
//    return _index;
//  }
//
//  public void set_index(String _index) {
//    this._index = _index;
//  }
//
//
//  public String get_uid() {
//    return _uid;
//  }
//
//  public void set_uid(String _uid) {
//    this._uid = _uid;
//  }
//
//  public Map<String, Object> getObject() {
//    return object;
//  }
//
//  public void setObject(Map object) {
//    this.object = object;
//  }
//
//
//}
