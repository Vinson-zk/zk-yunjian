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
 * @Title: ZKQueryOpt.java 
 * @author Vinson 
 * @Package com.zk.mongo.operator 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 5:56:57 PM 
 * @version V1.0   
*/
package com.zk.mongo.operator;

import java.util.Arrays;
import java.util.Collection;

import org.bson.Document;

import com.zk.mongo.ZKBaseDocument;

/**
 * @ClassName: ZKQueryOpt
 * @Description:
 * 
 * 查询的操作条件，各类元素中的查询条件操作
 * 
 * where 静态方法，生成一个条件对象，然后可对条件对象设置表达式
 * 
 * and、or、nor、not 静态逻辑方法，生成一个将一组条件按指定逻辑进行匹配的条件对象
 * 
 * 以实现
 *  Logical Query Operators
 *  Comparison Query Operators 
 *  Element Query Operators
 *  Array Query Operators
 *  
 * 未实现
 * Evaluation Query Operators
 * Geospatial Query Operators
 *  Bitwise Query Operators
 * @author Vinson
 * @version 1.0
 */
public class ZKQueryOpt extends ZKBaseDocument {

    public static ZKQueryOpt where(String attrbuterName) {
        return new ZKQueryOpt(attrbuterName);
    }

    /**
     * @Fields serialVersionUID : TODO(描述变量)
     */
    private static final long serialVersionUID = 1L;

    private String attrbuterName;

    public ZKQueryOpt() {
        
    }

    protected ZKQueryOpt(String attrbuterName) {
        this.attrbuterName = attrbuterName;
        this.put(this.attrbuterName, new Document());
    }

    protected void addExpression(String key, Object value) {
        this.get(attrbuterName, Document.class).put(key, value);
    }

    /**
     * Logical Query Operators For details on specific operator, including
     * syntax and examples, click on the specific operator to go to its
     * reference page.
     * 
     * $and Joins query clauses with a logical AND returns all documents that
     * match the conditions of both clauses. $not Inverts the effect of a query
     * expression and returns documents that do not match the query expression.
     * $nor Joins query clauses with a logical NOR returns all documents that
     * fail to match both clauses. $or Joins query clauses with a logical OR
     * returns all documents that match the conditions of either clause.
     */

    private static final String optLogicalKey_and = "$and";

    private static final String optLogicalKey_not = "$not";

    private static final String optLogicalKey_nor = "$nor";

    private static final String optLogicalKey_or = "$or";

    public static ZKQueryOpt and(ZKQueryOpt... queryOpts) {
        return and(Arrays.asList(queryOpts));
    }

    public static ZKQueryOpt and(Collection<ZKQueryOpt> queryOpts) {
        ZKQueryOpt qo = new ZKQueryOpt();
        qo.put(optLogicalKey_and, queryOpts);
        return qo;
    }

    public static ZKQueryOpt not(ZKQueryOpt... queryOpts) {
        return not(Arrays.asList(queryOpts));
    }

    public static ZKQueryOpt not(Collection<ZKQueryOpt> queryOpts) {
        ZKQueryOpt qo = new ZKQueryOpt();
        qo.put(optLogicalKey_not, queryOpts);
        return qo;
    }

    public static ZKQueryOpt nor(ZKQueryOpt... queryOpts) {
        return nor(Arrays.asList(queryOpts));
    }

    public static ZKQueryOpt nor(Collection<ZKQueryOpt> queryOpts) {
        ZKQueryOpt qo = new ZKQueryOpt();
        qo.put(optLogicalKey_nor, queryOpts);
        return qo;
    }

    public static ZKQueryOpt or(ZKQueryOpt... queryOpts) {
        return or(Arrays.asList(queryOpts));
    }

    public static ZKQueryOpt or(Collection<ZKQueryOpt> queryOpts) {
        ZKQueryOpt qo = new ZKQueryOpt();
        qo.put(optLogicalKey_or, queryOpts);
        return qo;
    }

    /***
     * Comparison Query Operators For details on specific operator, including
     * syntax and examples, click on the specific operator to go to its
     * reference page. For comparison of different ZKON type values, see the
     * specified ZKON comparison order.
     * 
     * $eq Matches values that zke equal to a specified value. $gt Matches
     * values that zke greater than a specified value. $gte Matches values that
     * zke greater than or equal to a specified value. $in Matches any of the
     * values specified in an zkray. $lt Matches values that zke less than a
     * specified value. $lte Matches values that zke less than or equal to a
     * specified value. $ne Matches all values that zke not equal to a specified
     * value. $nin Matches none of the values specified in an zkray.
     */
    private static final String optKey_eq = "$eq";

    private static final String optKey_gt = "$gt";

    private static final String optKey_gte = "$gte";

    private static final String optKey_in = "$in";

    private static final String optKey_lt = "$lt";

    private static final String optKey_lte = "$lte";

    private static final String optKey_ne = "$ne";

    private static final String optKey_nin = "$nin";

    public <V> ZKQueryOpt eq(V value) {
        this.addExpression(optKey_eq, value);
        return this;
    }

    public <V> ZKQueryOpt gt(V value) {
        this.addExpression(optKey_gt, value);
        return this;
    }

    public <V> ZKQueryOpt gte(V value) {
        this.addExpression(optKey_gte, value);
        return this;
    }

    public ZKQueryOpt in(Object... values) {
        this.addExpression(optKey_in, values);
        return this;
    }

    public <V> ZKQueryOpt lt(V value) {
        this.addExpression(optKey_lt, value);
        return this;
    }

    public <V> ZKQueryOpt lte(V value) {
        this.addExpression(optKey_lte, value);
        return this;
    }

    public <V> ZKQueryOpt ne(V value) {
        this.addExpression(optKey_ne, value);
        return this;
    }

    public ZKQueryOpt nin(Object... values) {
        this.addExpression(optKey_nin, values);
        return this;
    }

    /**
     * Element Query Operators For details on specific operator, including
     * syntax and examples, click on the specific operator to go to its
     * reference page.
     *
     * $exists Matches documents that have the specified field. $type Selects
     * documents if a field is of the specified type.
     */
    private static final String optKey_exists = "$exists";

    private static final String optKey_type = "$type";

    public ZKQueryOpt exists(boolean value) {
        this.addExpression(optKey_exists, value);
        return this;
    }

    public ZKQueryOpt type(String type) {
        this.addExpression(optKey_type, type);
        return this;
    }

    /**
     * Array Query Operators For details on specific operator, including syntax
     * and examples, click on the specific operator to go to its reference page.
     * Name Description
     * 
     * $all Matches zkrays that contain all elements specified in the query.
     * $elemMatch Selects documents if element in the zkray field matches all
     * the specified $elemMatch conditions. $size Selects documents if the zkray
     * field is a specified size.
     */
    private static final String optKey_all = "$all";

    private static final String optKey_elemMatch = "$elemMatch";

    private static final String optKey_size = "$size";

    // 还没有写单元测试
    public ZKQueryOpt all(Object... v) {
        all(optKey_all, Arrays.asList(v));
        return this;
    }

    // 还没有写单元测试
    public ZKQueryOpt all(Collection<Object> vList) {
        this.addExpression(optKey_all, vList);
        return this;
    }

    // 还没有写单元测试
    public ZKQueryOpt elemMatch(ZKQueryOpt queryOpt) {
        this.addExpression(optKey_elemMatch, queryOpt);
        return this;
    }

    // 还没有写单元测试
    public ZKQueryOpt size(long size) {
        this.addExpression(optKey_size, size);
        return this;
    }

//  /**
//   * Bitwise Query Operators
//   * For details on specific operator, including syntax and examples, click on the specific operator to go to its reference page.
//   * Name Description
//   * 
//   * $bitsAllClear    Matches numeric or binary values in which a set of bit positions all have a value of 0.
//   * $bitsAllSet  Matches numeric or binary values in which a set of bit positions all have a value of 1.
//   * $bitsAnyClear    Matches numeric or binary values in which any bit from a set of bit positions has a value of 0.
//   * $bitsAnySet  Matches numeric or binary values in which any bit from a set of bit positions has a value of 1.
//   */
//  private static final String optKey_bitsAllClear = "$bitsAllClear";
//  private static final String optKey_bitsAllSet = "$bitsAllSet";
//  private static final String optKey_bitsAnyClear = "$bitsAnyClear";
//  private static final String optKey_bitsAnySet = "$bitsAnySet";
//  
//  // 还没有写单元测试
//  public QueryOpt bitsAllClear(long size){
//      this.addExpression(optKey_size, size);
//      return this;
//  }
//  // 还没有写单元测试
//  public QueryOpt bitsAllSet(long size){
//      this.addExpression(optKey_size, size);
//      return this;
//  }
//  // 还没有写单元测试
//  public QueryOpt bitsAnyClear(long size){
//      this.addExpression(optKey_size, size);
//      return this;
//  }
//  // 还没有写单元测试
//  public QueryOpt bitsAnySet(long size){
//      this.addExpression(optKey_size, size);
//      return this;
//  }

}
