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
* @Title: ZKSysMenu.java 
* @author Vinson 
* @Package com.zk.sys.entity 
* @Description: TODO(simple description this file what to do.) 
* @date Aug 4, 2020 4:48:21 PM 
* @version V1.0 
*/
package com.zk.sys.res.entity;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.commons.ZKTreeSqlHelper;
import com.zk.base.entity.ZKBaseTreeEntity;
import com.zk.core.commons.ZKCoreConstants.ValidationRegexp;
import com.zk.core.commons.data.ZKJson;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBMapInfo;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKDBQueryCol;
import com.zk.db.commons.ZKDBQueryWhere;
import com.zk.db.commons.ZKSqlConvert;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBQueryScript;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/** 
* @ClassName: ZKSysMenu 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@ZKTable(name = "t_sys_res_menu", alias = "sysMenu", orderBy = " c_sort ASC ")
//@JsonIgnoreProperties(value = { "handler" })
public class ZKSysMenu extends ZKBaseTreeEntity<String, ZKSysMenu> {

    static ZKTreeSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKTreeSqlHelper getTreeSqlHelper() {
        return sqlHelper();
    }

    @Transient
    @XmlTransient
    @JsonIgnore
    public static ZKTreeSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKTreeSqlHelper(new ZKSqlConvertDelegating(), new ZKSysMenu());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to d o.)
     */
    private static final long serialVersionUID = 1L;

    public ZKSysMenu() {
        super();
    }

    public ZKSysMenu(String pkId) {
        super(pkId);
    }

//    protected static ZKSqlConvert sqlConvert;

    /**
     * 不能为空；菜单(路由)的名称, 国际化json对象；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @NotEmpty(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_name", javaType = ZKJson.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE, isCaseSensitive = false))
    protected ZKJson name;

    /**
     * 不能为空；唯一；自定义菜单标识 code；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 80, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_code", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    protected String code;

    /* 前端路由相关的属性 */

    /**
     * 不能为空；导航栏目代码，即功能菜单的分组代码；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_nav_code", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
    protected String navCode;

    /**
     * 不能为空；功能模块代码；也是功能模块目录;
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @Pattern(regexp = ValidationRegexp.code, message = "{zk.core.data.validation.code}")
    @ZKColumn(name = "c_func_module_code", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    protected String funcModuleCode;

    /**
     * 功能名称，也是功能组件对象名称；在同功能模块下要求唯一；将根据这名称查找到对应功能组件；注：为空时，不会生成面包屑！及路由
     */
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_func_name", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    protected String funcName;

    /**
     * 访问路径；注意当路径为 ':param'，要排在同级路由的最后，否则后面的路由会匹配不到！
     */
    @Length(max = 32, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_path", update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    protected String path;

    /**
     * 不能为空；是否为默认路由，0-不是；1-是
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_is_index", javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(true))
    protected Integer isIndex;

    /**
     * 默认 false；当为true时，仅当路径与位置匹配时才匹配;
     *  1、当 isFrame 0-不为路由容器，又有子路由时，要为 true 子路由才能显示出来；？要不要默认设置为true；
     *  2、当 isFrame 0-路由容器，此配置不起作用；值为 false；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_exact", javaType = Boolean.class, update = @ZKUpdate(true), query = @ZKQuery(true))
    protected Boolean exact;

    /**
     * 1-路由容器，否则为正常的一般路由；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_is_frame", javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(true))
    protected Integer isFrame;

    /**
     * 是否显示 1-显示；其他不显示；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 9, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_is_show", javaType = Integer.class, update = @ZKUpdate(true), query = @ZKQuery(true))
    protected Integer isShow;

    /**
     * 菜单图标,【生成菜单属性，不影响路由生成】；
     */
    @Length(max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_icon", update = @ZKUpdate(true))
    protected String icon;

    /**
     * 不能为空；排序字段,【生成菜单属性，不影响路由生成】；
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
    @ZKColumn(name = "c_sort", javaType = Integer.class, update = @ZKUpdate(true))
    protected Integer sort;

    /**
     * 权限标识，与后对对应控制代码一至；多个权限标识时用英文 ";" 号分隔；
     */
    @Length(max = 256, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_permission", javaType = String.class, update = @ZKUpdate(true), query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
    protected String permission;
    
    /*** 过渡和查询参数 ****/
//    /**
//     * code 模糊查询
//     */
//    @Transient
//    @ZKColumn(name = "c_nav_code", isResult = false, isInsert = false, query = @ZKQuery(queryType = ZKDBOptComparison.LIKE))
//    protected String searchNavCode;

    /**
     * 导航实体，查询明细时，会级联查询出来，service 中 get 时，不会级联查询
     */
    @Transient
    ZKSysNav sysNav;

    // 查询辅助字段
    @Transient
    @JsonIgnore
    @XmlTransient
    String searchValue;

    /**
     * @return name sa
     */
    public ZKJson getName() {
        return name;
    }
    
    /**
     * @param name
     *            the name to set
     */
    public void setName(ZKJson name) {
        this.name = name;
    }

    @Transient
    public void putName(String key, String value) {
        if (this.name == null) {
            this.name = new ZKJson();
        }
        this.name.put(key, value);
    }

    /**
     * @return code sa
     */
    public String getCode() {
        return code;
    }
    
    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return navCode sa
     */
    public String getNavCode() {
        return navCode;
    }

    /**
     * @param navCode
     *            the navCode to set
     */
    public void setNavCode(String navCode) {
        this.navCode = navCode;
    }

    /**
     * @return funcModuleCode sa
     */
    public String getFuncModuleCode() {
        return funcModuleCode;
    }

    /**
     * @param funcModuleCode
     *            the funcModuleCode to set
     */
    public void setFuncModuleCode(String funcModuleCode) {
        this.funcModuleCode = funcModuleCode;
    }

    /**
     * @return funcName sa
     */
    public String getFuncName() {
        return funcName;
    }

    /**
     * @param funcName
     *            the funcName to set
     */
    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    /**
     * @return path sa
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     *            the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return isIndex sa
     */
    public Integer getIsIndex() {
        return isIndex;
    }

    /**
     * @param isIndex
     *            the isIndex to set
     */
    public void setIsIndex(Integer isIndex) {
        this.isIndex = isIndex;
    }

    /**
     * @return exact sa
     */
    public Boolean getExact() {
        return exact;
    }

    /**
     * @param exact
     *            the exact to set
     */
    public void setExact(Boolean exact) {
        this.exact = exact;
    }

    /**
     * @return isFrame sa
     */
    public Integer getIsFrame() {
        return isFrame;
    }

    /**
     * @param isFrame
     *            the isFrame to set
     */
    public void setIsFrame(Integer isFrame) {
        this.isFrame = isFrame;
    }

    /**
     * @return isShow sa
     */
    public Integer getIsShow() {
        return isShow;
    }

    /**
     * @param isShow
     *            the isShow to set
     */
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    /**
     * @return icon sa
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * @return sort sa
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort
     *            the sort to set
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return permission sa
     */
    public String getPermission() {
        return permission;
    }

    /**
     * @param permission
     *            the permission to set
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    @Length(max = 20, message = "{zk.core.data.validation.length.max}")
    public String getParentId() {
        return super.getParentId();
    }

    /**
     * @return sysNav sa
     */
    public ZKSysNav getSysNav() {
        return sysNav;
    }

    /**
     * @param sysNav
     *            the sysNav to set
     */
    public void setSysNav(ZKSysNav sysNav) {
        this.sysNav = sysNav;
    }

    /**
     * @return searchValue sa
     */
    public String getSearchValue() {
        return searchValue;
    }

    /**
     * @param searchValue
     *            the searchValue to set
     */
    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    // 取 where 条件；实体定义可以定制；在 生成的 sql；注意：末尾加空格
    @Override
    @Transient
    @JsonIgnore
    @XmlTransient
    public ZKDBQueryWhere getZKDbWhere(ZKSqlConvert sqlConvert, ZKDBMapInfo mapInfo) {
        ZKDBQueryWhere where = super.getZKDbWhereTree(sqlConvert, mapInfo);
//        ZKDBQueryWhere where = sqlConvert.resolveQueryCondition(mapInfo);
        // 制作一个根据名称和代码同时查询的 查询条件，用过度 java 属性 searchValue 为传参数值
		ZKDBQueryWhere sWhere = ZKDBQueryWhere.asOr("(", ")",
				ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_name", "searchValue", String.class, null, false),
				ZKDBQueryCol.as(ZKDBOptComparison.LIKE, "c_code", "searchValue", String.class, null, false));

		where.put(ZKDBQueryScript.asIf(sWhere, 0, "searchValue", String.class));
        return where;
    }

}
