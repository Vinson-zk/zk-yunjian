/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKBaseTreeEntity.java 
* @author Vinson 
* @Package com.zk.base.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 20, 2020 11:29:05 AM 
* @version V1.0 
*/
package com.zk.base.entity;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKDBQuery;
import com.zk.db.commons.ZKDBQueryColSql;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.mybatis.commons.ZKDBQueryScript;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

/**
 * 树形实体中有一个 parentIdIsEmpty 属性，用于在 parentId 为空时，只查询 parentId 为 null 或为空的节点；
 * 即在 parentId 为空时，只查询根结点
 * 
 * @ClassName: ZKBaseTreeEntity
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public abstract class ZKBaseTreeEntity<ID extends Serializable, E extends ZKBaseTreeEntity<ID, E>>
        extends ZKBaseEntity<ID, E> {

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKDBSqlHelper getSqlHelper(){
        return this.getTreeSqlHelper();
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public abstract ZKTreeSqlHelper getTreeSqlHelper();

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;
    
    public ZKBaseTreeEntity() {
        super();
    }

    public ZKBaseTreeEntity(ID pkId) {
        super(pkId);
    }

    /**
     * 菜单(路由)的上级结点 id，制作树形结构关键属性； 
     */
    @ZKColumn(name = "c_parent_id", query = @ZKQuery(value = true, isCaseSensitive = true))
    protected ID parentId;

    @XmlTransient
    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    protected E parent;

    /**
     * 查询辅助字段，
     * 
     * 当 parentId 为null或为空的情况下： 
     *      parentIdIsEmpty 为 true，指定只查询 c_parent_id 为 null 或为空的根节点; 
     *      parentIdIsEmpty 为 false, c_parent_id 不会做查询条件；
     * 
     */
    @XmlTransient
    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    protected boolean parentIdIsEmpty = false;

    /**
     * 子菜单(子路由)数组；
     */
    @XmlTransient
    @Transient
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    protected List<E> children;

    @XmlTransient
    @Transient
    protected Integer depth;

    /**
     * @return parentIdIsEmpty sa
     */
    public boolean isParentIdIsEmpty() {
        return parentIdIsEmpty;
    }

    /**
     * @param parentIdIsEmpty
     *            the parentIdIsEmpty to set
     */
    public void setParentIdIsEmpty(boolean parentIdIsEmpty) {
        this.parentIdIsEmpty = parentIdIsEmpty;
    }

    /**
     * @return parentId sa
     */
    public ID getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(ID parentId) {
        this.parentId = parentId;
    }

    /**
     * @return parent sa
     */
    public E getParent() {
        return parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(E parent) {
        this.parent = parent;
    }

    /**
     * @return children sa
     */
    public List<E> getChildren() {
        return children;
    }

    /**
     * @param children
     *            the children to set
     */
    public void setChildren(List<E> children) {
        this.children = children;
    }

    /**
     * @return depth sa
     */
    public Integer getDepth() {
        return depth;
    }

    /**
     * @param depth
     *            the depth to set
     */
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    /**
     * 是否是叶子节点，子节点为 null 或为空时，为叶子节点；
     *
     * @Title: isLeaf
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 18, 2022 11:40:24 PM
     * @return boolean
     */
    public boolean getIsLeaf() {
        return this.getChildren() == null || this.getChildren().isEmpty();
    }

    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////

    @Transient
    @JsonIgnore
    @XmlTransient
    @Override
    public ZKDBQueryWhere getZKDbWhere(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
        return this.getZKDbWhereTree(sqlConvert, mapInfo);
    }

    /**
     * 树形查询
     *
     * @MethodName getZKDbWhereTree
     * @param sqlConvert
     * @param mapInfo
     * @return com.zk.db.commons.ZKDBQueryWhere
     * @throws
     * @Author bs
     * @DATE 2022-10-08 01:02:90
     */
    @Transient
    @JsonIgnore
    @XmlTransient
    public ZKDBQueryWhere getZKDbWhereTree(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
        ZKDBQueryWhere where = sqlConvert.resolveQueryCondition(mapInfo);
        // 制作 c_parent_id 为 null 或为空的 查询根节点的查询条件
        ZKDBQuery parentIdIsEmptyCondition = this.getParentIsNullCondition(this.getPkIDClass(), "c_parent_id");
        // 当 parentId 为null或为空, 且 parentIdIsEmpty 为 true，指定只查询 c_parent_id 为 null 或为空的根节点;
        parentIdIsEmptyCondition = ZKDBQueryScript.asIf(parentIdIsEmptyCondition, 0, "parentIdIsEmpty", Boolean.class);
        where.put(ZKDBQueryScript.asIf(parentIdIsEmptyCondition, 4, "parentId", this.getPkIDClass()));
        return where;
    }

//    /**
//     * 树形所有节点，统一过滤，过滤结果中不是根结点时，如果父节点不在过滤结果中，升级为结果中的根节点；如果父节点在过滤结果中，则不做为返回结果; 且不递归查询子节点；
//     * @MethodName getZKDbWhereTreeFilter
//     * @param sqlConvert
//     * @param mapInfo
//     * @return com.zk.db.commons.ZKDBQueryWhere
//     * @throws
//     * @Author bs
//     * @DATE 2022-10-18 23:03:984
//     */
//    @JsonIgnore
//    @XmlTransient
//    @Transient
//    public ZKDBQueryWhere getZKDbWhereTreeFilter(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
//        return this.getZKDbWhereTreeFilter(sqlConvert, mapInfo,"c_pk_id", "c_parent_id");
//    }
//
//    protected ZKDBQueryWhere getZKDbWhereTreeFilter(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo,
//        String pkColumnName, String parentColumnName) {
//        ZKDBQueryWhere where = sqlConvert.resolveQueryCondition(mapInfo, Arrays.asList("parentId"));
//
//        // 查询 所以结果的ID
//        String selSqlResIds = this.getResIdsSelSql(where, sqlConvert, mapInfo, pkColumnName);
//        // 父ID 不在指定 ID集合 中
//        ZKDBQuery parentIdNotIn = ZKDBQueryColSql.as(ZKDBOptComparison.NIN, parentColumnName, selSqlResIds);
//
//        // 制作 c_parent_id 为 null 或为空的 查询根节点的查询条件
//        ZKDBQuery parentIdIsEmpty = this.getParentIsNullCondition(this.getPkIDClass(), parentColumnName);
//
//        // 查询 结果中的 根结点
//        ZKDBQueryWhere pIdOrWhere = ZKDBQueryWhere.asOr("(",")",parentIdNotIn);
//        pIdOrWhere.put(ZKDBQueryScript.asIf(parentIdIsEmpty, 4, "parentId", this.getPkIDClass()));
//        pIdOrWhere.put(ZKDBQueryScript.asIf(
//                ZKDBQueryCol.as(ZKDBOptComparison.EQ, parentColumnName, "parentId", this.getPkIDClass(),null, true),
//                0, "parentId", this.getPkIDClass()));
//        where.put(pIdOrWhere);
//
//        return where;
//    }
//
//    private String getResIdsSelSql(ZKDBQueryWhere where, ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo,
//                                 String pkColumnName){
//        // 过滤出所有结果ID 的 sql
//        StringBuffer sb = new StringBuffer();
//        sb.append("(");
//        sb.append(ZKSqlConvert.SqlKeyword.select).append("_t.").append(pkColumnName);
//        sb.append(ZKSqlConvert.SqlKeyword.from).append(mapInfo.getTableName()).append(" _t ");
//        sb.append(ZKDBScriptKey.where[0]).append(where.toQueryCondition(sqlConvert, " _t")).append(ZKDBScriptKey.where[1]);
//        sb.append(")");
//        return sb.toString();
//    }

    // 当 parentId 为 null 或为空 的查询条件
    @JsonIgnore
    @XmlTransient
    @Transient
    private ZKDBQuery getParentIsNullCondition(Class<?> idClass, String parentColumnName) {
        if (idClass == String.class) {
            return ZKDBQueryWhere.asOr("(", ")",
                    ZKDBQueryColSql.as(ZKDBOptComparison.ISNULL, parentColumnName, ""),
                    ZKDBQueryColSql.as(ZKDBOptComparison.EQ, parentColumnName, "\"\""));
        }
        else if (idClass == Long.class || idClass == Integer.class) {
            return ZKDBQueryWhere.asOr("(", ")",
                    ZKDBQueryColSql.as(ZKDBOptComparison.ISNULL, parentColumnName, ""),
                    ZKDBQueryColSql.as(ZKDBOptComparison.EQ, parentColumnName, "0"));
        }
        else {
            return ZKDBQueryColSql.as(ZKDBOptComparison.ISNULL, parentColumnName, "");
        }
    }

}
