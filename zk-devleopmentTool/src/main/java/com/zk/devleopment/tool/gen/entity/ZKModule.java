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
* @Title: ZKModule.java 
* @author Vinson 
* @Package com.zk.code.generate.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 4:17:53 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.entity;

import java.io.File;

import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.commons.ZKDBOptComparison;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import jakarta.validation.constraints.NotNull;

/**
 * 功能模块信息
 * 
 * @ClassName: ZKModule
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@ZKTable(name = "t_dt_code_gen_module", alias = "cgModule", orderBy = " c_create_date ASC ")
//@JsonIgnoreProperties(value = { "handler" })
public class ZKModule extends ZKBaseEntity<String, ZKModule> {

    static ZKDBSqlHelper sqlHelper;

    @Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKModule());
        }
        return sqlHelper;
    }

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    public ZKModule() {
        super();
    }

    public ZKModule(String pkId) {
        super(pkId);
    }

    /**********************************************************************************/
    /*** 数据库连接信息 */
    /**********************************************************************************/
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 128, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_db_driver", update = @ZKUpdate(true))
    String driver = "com.mysql.jdbc.Driver";

    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 128, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_db_url", update = @ZKUpdate(true))
    String url;

    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_db_username", update = @ZKUpdate(true))
    String username;

    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_db_pwd", update = @ZKUpdate(true))
    String password;

    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 32, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_db_type", update = @ZKUpdate(true))
    String dbType = "mysql"; // mysql; oracle; mssql;

    /**********************************************************************************/
    /*** 模块表信息的一些配置，如表名前缀，字段前缀 */
    /**********************************************************************************/
    /**
     * 表名前缀
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_table_name_prefix", update = @ZKUpdate(true))
    String tableNamePrefix = "";

    /**
     * 字段名前缀
     */
    @Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_col_name_prefix", update = @ZKUpdate(true))
    String colNamePrefix = "";

    /**
     * 是否去掉前缀
     */

    @NotNull(message = "{zk.core.data.validation.notNull}")
    @ZKColumn(name = "c_is_remove_prefix", update = @ZKUpdate(true))
    boolean isRemovePrefix = true;

    /**********************************************************************************/
    /*** 模块代码一些配置 */
    /**********************************************************************************/
    /**
     * java 类包名前缀 包名=【类包名前缀 + 功能子模块名】
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_package_prefix", update = @ZKUpdate(true))
    String packagePrefix;

    /**
     * java 功能模块名; 全表唯一；不做包名，如果要给模块添加包名，直接在包名前缀中添加即可
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_module_name", update = @ZKUpdate(true))
    String moduleName;

    /**********************************************************************************/
    /*** 其他 一些代码生成的辅助信息 */
    /**********************************************************************************/
    /**
     * java 功能模块的标签名称；即显示 名
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length}")
    @ZKColumn(name = "c_label_name", update = @ZKUpdate(true),
            query = @ZKQuery(value = true, queryType = ZKDBOptComparison.LIKE))
    String labelName;

	// @NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 0, max = 64, message = "{zk.core.data.validation.length}")
	@ZKColumn(name = "c_module_prefix", update = @ZKUpdate(true))
	String modulePrefix = "ZK";

//    /**
//     * java 类包名前缀最后一个包名
//     * 
//     * @return lastPackagePrefix sa
//     */
//    public String getLastPackagePrefix() {
//        return StringUtils.substringAfterLast(packagePrefix, ".");
//    }

    /**
     * @return packagePrefix sa
     */
    public String getPackagePrefix() {
        return packagePrefix;
    }

    /**
     * @param packagePrefix
     *            the packagePrefix to set
     */
    public void setPackagePrefix(String packagePrefix) {
        this.packagePrefix = packagePrefix;
    }

    /**
     * @return moduleName sa
     */
    public String getModuleName() {
        return moduleName;
    }

    /**
     * @param moduleName
     *            the moduleName to set
     */
    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    /**
     * @return driver sa
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @param driver
     *            the driver to set
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * @return url sa
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return username sa
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password sa
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return dbType sa
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * @param dbType
     *            the dbType to set
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * @return tableNamePrefix sa
     */
    public String getTableNamePrefix() {
        return tableNamePrefix;
    }

    /**
     * @param tableNamePrefix
     *            the tableNamePrefix to set
     */
    public void setTableNamePrefix(String tableNamePrefix) {
        this.tableNamePrefix = tableNamePrefix;
    }

    /**
     * @return colNamePrefix sa
     */
    public String getColNamePrefix() {
        return colNamePrefix;
    }

    /**
     * @param colNamePrefix
     *            the colNamePrefix to set
     */
    public void setColNamePrefix(String colNamePrefix) {
        this.colNamePrefix = colNamePrefix;
    }

    /**
     * @return isRemovePrefix sa
     */
    public boolean getIsRemovePrefix() {
        return isRemovePrefix;
    }

    /**
     * @param isRemovePrefix
     *            the isRemovePrefix to set
     */
    public void setIsRemovePrefix(boolean isRemovePrefix) {
        this.isRemovePrefix = isRemovePrefix;
    }

    /**
     * @return labelName sa
     */
    public String getLabelName() {
        return labelName;
    }

    /**
     * @param labelName
     *            the labelName to set
     */
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    /**
     * @return modulePrefix sa
     */
	public String getModulePrefix() {
		return modulePrefix == null ? "" : modulePrefix;
	}

	/**
	 * @param modulePrefix
	 *            the modulePrefix to set
	 */
	public void setModulePrefix(String modulePrefix) {
		this.modulePrefix = modulePrefix;
	}

    /**********************************************************************************/
    /*** 计算信息 */
    /**********************************************************************************/

    /**
     * 首字母大写
     * 
     * @return String
     */
    public String getModuleNameCap() {
        if (ZKStringUtils.isEmpty(moduleName)) {
            return "";
        }
        String mn = "";
        String[] mns = moduleName.split("\\.");
        for (String s : mns) {
            mn += ZKStringUtils.capitalize(s);
        }
        return ZKStringUtils.capitalize(mn);
    }

    public String getModuleNameUncap() {
        if (ZKStringUtils.isEmpty(moduleName)) {
            return "";
        }
        String mn = "";
        String[] mns = moduleName.split("\\.");
        for (String s : mns) {
            mn += ZKStringUtils.capitalize(s);
        }
        return ZKStringUtils.uncapitalize(mn);
    }

    public String getModulePrefixLower() {
        if (ZKStringUtils.isEmpty(modulePrefix)) {
            return "";
        }
        return modulePrefix.toLowerCase();
    }

    public String getPackagePrefixPath() {
        return ZKStringUtils.replaceEach(packagePrefix, new String[] { "//", "/", "." },
                new String[] { File.separator, File.separator, File.separator });
    }

    public String getModuleNamePath() {
        if (ZKStringUtils.isEmpty(moduleName)) {
            return "";
        }
        return ZKStringUtils.replaceEach(moduleName, new String[] { "//", "/", "." },
                new String[] { File.separator, File.separator, File.separator });
    }

}
