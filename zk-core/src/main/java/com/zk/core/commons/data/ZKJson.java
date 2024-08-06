/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKJson.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:21:36 PM 
 * @version V1.0   
*/
package com.zk.core.commons.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import com.alibaba.fastjson2.JSONObject;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;

/** 
* @ClassName: ZKJson 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
//public class ZKJson<V> extends HashMap<String, V> implements Serializable {
public class ZKJson extends JSONObject {

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    public ZKJson() {

    }

    public ZKJson(String value) {
        if (!ZKStringUtils.isEmpty(value)) {
            this.putAll(ZKJsonUtils.parseMap(value));
        }
    }

    /**
     * 取查询 key 与 value
     * 
     * @return
     */
    public Set<SimpleEntry<String, String>> getKeyValues() {
        if (this.size() > 0) {
            return makeKeyValue("$", this);
        }
        return null;
    }

    /**
     * 取 ZKJson 对象中的值
     * 
     * @param value
     * @param paddingStr
     * @return
     */
    protected String getSqlValueByJsonValue(Object value, String paddingStr) {
        if (value instanceof String
//          || value instanceof Date
                || value instanceof Integer || value instanceof Long || value instanceof Float
                || value instanceof Double || value instanceof BigDecimal || value instanceof BigInteger) {
            return value.toString();
        }
        else {
            return getSqlValueByJsonValue(ZKJsonUtils.toJsonStr(value), paddingStr);
        }
    }

    /**
     * 转义特殊字符，防 sql 注入和保证 sql 正确
     * 
     * @param source
     * @return
     */
    protected String transferredMeaning(String source) {
//      source = source.replace("\\", "\\\\");
//      source = source.replace("{", "\\{");
//      source = source.replace("}", "\\}");
//      source = source.replace("\"", "\\\"");
//      source = source.replace("'", "\\\\'");
        return source;
    }

    /**
     * 制作修改的 json sql 字符的过渡数组； 如：[['$.t2', 'update t2'], ['$.t3', 'add attr
     * t3'], ['$.t4.t1', 'ddd']]
     * 
     * @param prdfixStr
     * @param json
     * @return
     */
    protected Set<SimpleEntry<String, String>> makeKeyValue(String prdfixStr, ZKJson json) {
        if (json != null) {
            Set<SimpleEntry<String, String>> resValues = new HashSet<>();
            if (json.size() > 0) {
                Set<SimpleEntry<String, String>> t;
                for (java.util.Map.Entry<String, ?> e : json.entrySet()) {
                    if (e.getValue() instanceof ZKJson) {
                        t = makeKeyValue(prdfixStr + ".\"" + e.getKey() + "\"", (ZKJson) e.getValue());
                        if (t != null && t.size() > 0) {
                            resValues.addAll(t);
                        }
                        else {
                            // 属性 json 类型为空时处理
                        }
                    }
                    else {
                        resValues.add(new SimpleEntry<String, String>(prdfixStr + ".\"" + e.getKey() + "\"",
                                this.getSqlValueByJsonValue(e.getValue(), "")));
                    }
                }
            }
            else {
                // 属性 json 类型为空时处理
            }
            return resValues;
        }
        return null;
    }

    /**
     * 取对象的 Json 字符串
     * 
     * @return
     */
    @Override
    public String toString() {
        return ZKJsonUtils.toJsonStr(this);
    }

    /*** KEY 的编码方式还没写，业务上应该也不需要对 key 编码 ***/
    public Object put(String key, Object v) {
//        if (v == null || (v instanceof Collection<?> && ((Collection<?>) v).size() < 1)) {
//            return null;
//        }
        if (v == null) {
            return null;
        }
        else {
            if (v instanceof ZKJson) {
                return super.put(key, v);
//          }else if(v instanceof Map<?,?>){
//              return super.put(key, JsonUtils.writeObjectJson(v));
            }
            else {
                return super.put(key, v);
            }

        }
//      return super.put(EncodesUtils.encodeBase64(key), v);
    }

    public <C> C format(Class<C> classz) {
        return ZKJsonUtils.parseObject(this.toJSONString(), classz);
    }

    /**
     * 根据 JSONObject 转 ZKJson
     *
     * @Title: parse
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 5, 2021 4:50:38 PM
     * @param json
     * @return
     * @return ZKJson
     */
    public static ZKJson parse(JSONObject json) {
        ZKJson zkJson = new ZKJson();
        zkJson.putAll(json);
        return zkJson;
    }

    /**
     * 根据 json String 转 ZKJson
     *
     * @Title: parse
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 5, 2021 4:51:36 PM
     * @param jsonStr
     * @return
     * @return ZKJson
     */
    public static ZKJson parse(String jsonStr) {
        ZKJson zkJson = new ZKJson();
        zkJson.putAll(JSONObject.parseObject(jsonStr));
        return zkJson;
    }

//  @Override
//  public V get(Object key){
//      V v = super.get(key);
//      
////        if(v instanceof String){
////            
////        }
//      
////        if(v instanceof Json && ((Json)v).size() < 0){
////            return null;
////        }
////        return v;
////        return super.get(EncodesUtils.encodeBase64(key.toString()));
//  }
//  
//  @Override
//  public void putAll(Map<? extends String, ? extends V> m){
//      if(m instanceof Json){
//          super.putAll(m);
//      }else{
//          for(java.util.Map.Entry<? extends String, ? extends V> e:m.entrySet()){
//              super.put(EncodesUtils.encodeBase64(e.getKey()), e.getValue());
//          }
//      }
//  }
//  
//  @Override
//  public Set<String> keySet(){
//      Set<String> keys = super.keySet();
//      if(keys != null){
//          Set<String> rKeys = new HashSet<>();
//          for(String key:keys){
//              rKeys.add(EncodesUtils.decodeBase64ToStr(key));
//          }
//          return rKeys;
//      }else{
//          return null;
//      }
//  }
//  
//  @Override
//  public boolean containsKey(Object key){
//      return super.containsKey(EncodesUtils.encodeBase64(key.toString()));
//  }
//  
//  @Override
//  public V remove(Object key){
//      return super.remove(EncodesUtils.encodeBase64(key.toString()));
//  }
//  @Override
//  public Set<java.util.Map.Entry<String, V>> entrySet(){
//      return super.entrySet();
//  }

}
