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
* @Title: ZKConvertUtils.java 
* @author Vinson 
* @Package com.zk.code.generate.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 5:06:44 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.utils.ZKStringUtils;
import com.zk.devleopment.tool.gen.entity.ZKColInfo;
import com.zk.devleopment.tool.gen.entity.ZKModule;
import com.zk.devleopment.tool.gen.entity.ZKTableInfo;

/**
 * 表信息转为 java 代码信息的一些公共方法
 * 
 * @ClassName: ZKConvertUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKConvertUtils {

    /**
     * 日志对象
     */
    protected static Logger log = LogManager.getLogger(ZKConvertUtils.class);

    /**
     * 将做好的表信息，包含字段信息，转换为 java 属性信息；
     *
     * @Title: convertTableInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 25, 2021 6:44:36 PM
     * @param zkModule
     * @param zkTableInfo
     * @return void
     */
    public static void convertTableInfo(ZKModule zkModule, ZKTableInfo zkTableInfo) {
        if (zkModule.getIsRemovePrefix()) {
            convertTableInfo(zkModule, zkModule.getTableNamePrefix(), zkTableInfo);
        }
        else {
            convertTableInfo(zkModule, "", zkTableInfo);
        }

        if (zkTableInfo.getCols() != null) {
            convertAttrInfo(zkModule, zkTableInfo, zkTableInfo.getCols());
        }
    }

    /**
     * 设置好表信息后，转换为 java 类信息; 不包括转换字段信息
     * 
     * 类名：className = 模块前缀 + 首字线大写的模块名 + 首字母大写的子模块名 + 去掉表中的：表名前缀、子模块名；按驼峰命名法命名的类名；
     * 
     * 功能名：funcName = 不要模块前缀的 类名
     * 
     * 导航代码：navCode = 模块名
     * 
     * 表标签：label = 表的说明
     *
     * @Title: convertTableInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jan 9, 2022 5:01:48 PM
     * @param zkModule
     * @param tableNamePrefix
     * @param zkTableInfo
     * @return void
     */
    private static void convertTableInfo(ZKModule zkModule, String tableNamePrefix, ZKTableInfo zkTableInfo) {
        String name = zkModule.getModuleNameCap();
        name += zkTableInfo.getSubModuleName();
        name += convertTableName(tableNamePrefix, zkTableInfo.getSubModuleName(), zkTableInfo.getTableName());

        zkTableInfo.setFuncName(name);
        name = ZKStringUtils.capitalize(zkModule.getModulePrefix()) + name;
        zkTableInfo.setClassName(name);

        zkTableInfo.setLabel(zkTableInfo.getTableComments());
        // 设置模块ID
        zkTableInfo.setModuleId(zkModule.getPkId());
        // 表信息导航代码，默认设置为模块名
        zkTableInfo.setNavCode(zkModule.getModuleName());
    }

    /**
     * 转换字段信息
     *
     * @Title: convertAttrInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 1, 2021 12:34:39 AM
     * @param zkModule
     * @param zkTableInfo
     * @param cols
     * @return void
     */
    public static void convertAttrInfo(ZKModule zkModule, ZKTableInfo zkTableInfo, Collection<ZKColInfo> cols) {
        for (ZKColInfo col : cols) {
            convertAttrInfo(zkModule, zkTableInfo, col);
        }
    }

    public static void convertAttrInfo(ZKModule zkModule, ZKTableInfo zkTableInfo, ZKColInfo col) {
        if (zkModule.getIsRemovePrefix()) {
            convertAttrInfo(zkModule.getColNamePrefix(), col, zkTableInfo.getIsTree());
        }
        else {
            convertAttrInfo(null, col, zkTableInfo.getIsTree());
        }
        // 设置表ID
        col.setTableId(zkTableInfo.getPkId());
        // 转换时，设置字段描述为标签
        col.setLabel(col.getColComments());
        if (col.getLabel() != null && col.getLabel().length() > 64) {
            col.setLabel(col.getLabel().substring(0, 64));
        }
    }

    // 设置好 表字段信息后，转换字段信息为 java 属性信息；
    private static void convertAttrInfo(String colNamePrefix, ZKColInfo col, boolean isTree) {
        convertColClass(col.getColJdbcType(), col);
        col.setAttrName(convertColName(colNamePrefix, col.getColName()));
        // 判断是不是父类的字段
        col.setAttrIsBaseField(isBaseField(col.getAttrName(), isTree));
        // 转换字段编辑策略
        col.setEditStrategy(convertColEditStrategy(col.getAttrName(), isTree, col.getAttrType()));
        // 转换查询方式；父字段不查询
        if (col.isNewRecord()) {
            // 字段查询方式仅在新增时，进行转换设置
            if (isBaseField(col.getAttrName(), isTree)) {
                col.setAttrQueryType("");
            }
            else {
                col.setAttrQueryType("EQ");
            }
        }

    }

    /**
     * 根据表字段的类型制作 java 类中属性的类型
     *
     * @Title: convertColClass
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 23, 2021 8:38:12 AM
     * @param jdbcType
     * @param col
     *            输出参数
     */
    public static void convertColClass(String jdbcType, ZKColInfo col) {
        String pType = convertJdbcTypeToClassName(jdbcType);
        col.setAttrType(pType);
    }

    /**
     * 转换表名为 原始类名，其中首字母大写；真实类型还会与模块代码、了模块名结合；
     *
     * @Title: convertTableName
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 24, 2021 4:59:29 PM
     * @param tableNamePrefix
     *            表名前缀
     * @param subModuleName
     *            子模块名
     * @param tableName
     *            传入的表名
     * @return String 返回 java 类名；首字母大写；
     */
    public static String convertTableName(String tableNamePrefix, String subModuleName, String tableName) {

        // 将表名转为小写
        tableName = tableName.toLowerCase();
        // 正则表达式匹配需要移除的开头
        if (!ZKStringUtils.isEmpty(tableNamePrefix)) {
            tableNamePrefix = tableNamePrefix.toLowerCase();
            String regularStr = "^" + tableNamePrefix + ".*";
            if (tableName.matches(regularStr) == true) {
                tableName = tableName.substring(tableNamePrefix.length());
            }
        }

        // 正则表达式匹配需要移除的开头了子模块名
        if (!ZKStringUtils.isEmpty(subModuleName)) {
            subModuleName = subModuleName.toLowerCase();
            // regularStr = "^((" + subModuleName + ")|(_" + subModuleName + ")).*";
            String regularStr = "^_.*";
            if (tableName.matches(regularStr) == true) {
                tableName = tableName.substring(1);
            }

            regularStr = "^" + subModuleName + ".*";
            if (tableName.matches(regularStr) == true) {
                tableName = tableName.substring(subModuleName.length());
            }
        }

        // 把 _[第一个字母] 换成 [第一个字母]大写
        Pattern pattern = Pattern.compile("[_]{1}[a-zA-Z]{1}");
        Matcher matcher = pattern.matcher(tableName);
        String tempStr = "";
        while (matcher.find()) {
            tempStr = matcher.group(0);
            tempStr = tempStr.substring(1);
            tableName = matcher.replaceFirst(tempStr.toUpperCase());
            matcher = pattern.matcher(tableName);
            tempStr = "";
        }

        // 去掉所有的下划线
        pattern = Pattern.compile("[_]{1}");
        matcher = pattern.matcher(tableName);
        tableName = matcher.replaceAll("");

        // 第一个字母转为大写
        return ZKStringUtils.capitalize(tableName);
    }

    /**
     * 转换表字段名称属性名
     *
     * @Title: convertColName
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 24, 2021 4:56:14 PM
     * @param columnNamePrefix
     *            表字段前缀
     * @param colName
     *            传入的字段名
     * @return String 返回 java 属性名；首字母小写；
     */
    public static String convertColName(String colNamePrefix, String colName) {

        // 字段先转为小写
        colName = colName.toLowerCase();
        // 正则表达式匹配需要移除的开头
        if (colNamePrefix != null && !"".equals(colNamePrefix)) {
            colNamePrefix = colNamePrefix.toLowerCase();
            String regularStr = "^" + colNamePrefix + ".*";
            if (colName.matches(regularStr) == true) {
                colName = colName.substring(colNamePrefix.length());
            }
        }

        // 把 _[第一个字母] 换成 [第一个字母]大写
        Pattern pattern = Pattern.compile("[_]{1}[a-zA-Z]{1}");
        Matcher matcher = pattern.matcher(colName);
        String tempStr = "";
        while (matcher.find()) {
            tempStr = matcher.group(0);
            tempStr = tempStr.substring(1);
            colName = matcher.replaceFirst(tempStr.toUpperCase());
            matcher = pattern.matcher(colName);
            tempStr = "";
        }
        // 去掉所有的下划线
        pattern = Pattern.compile("[_]{1}");
        matcher = pattern.matcher(colName);
        colName = matcher.replaceAll("");

        // 首字线小写
        return ZKStringUtils.uncapitalize(colName);
    }

    /**
     * 判断是不是父类字段
     *
     * @Title: isBaseField
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 25, 2021 2:55:23 PM
     * @param filedName
     * @param isTree
     *            是不是树形结构；true-是；false-不是；
     * @return boolean true-是父类字段；false-不是父类字段；
     */
    public static boolean isBaseField(String filedName, boolean isTree) {
        String[] baseFields = new String[] { "pkId", "createUserId", "updateUserId", "createDate", "updateDate",
                "remarks", "pDesc", "spare1", "spare2", "spare3", "spareJson", "delFlag", "version" };
        
        String[] treeBaseFields = new String[] { "parentId" };

        for (String s : baseFields) {
            if (s.equals(filedName)) {
                return true;
            }
        }
        if (isTree) {
            for (String s : treeBaseFields) {
                if (s.equals(filedName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 根据 字段情况 转换 UI 编辑策略
    public static String convertColEditStrategy(String filedName, boolean isTree, String attrType) {
        if (isBaseField(filedName, isTree)) {
            return ZKColInfo.KeyEditStrategy.noEdit;
        }
        else if ("String".equals(attrType)) {
            return ZKColInfo.KeyEditStrategy.EditString;
        }
        else if ("Date".equals(attrType)) {
            return ZKColInfo.KeyEditStrategy.EditDate;
        }
        else if ("Boolean".equals(attrType)) {
            return ZKColInfo.KeyEditStrategy.EditRadio;
        }
        else if (attrType.startsWith("ZKJson")) {
            return ZKColInfo.KeyEditStrategy.EditJson;
        }
        else if (isInt(attrType)) {
            return ZKColInfo.KeyEditStrategy.EditInt;
        }
        else if (isFloat(attrType)) {
            return ZKColInfo.KeyEditStrategy.EditFloat;
        }
        return ZKColInfo.KeyEditStrategy.EditString;
    }

    // 判断 java 类型是否为整型
    public static boolean isInt(String attrType) {
        if ("Long".equals(attrType) || "Integer".equals(attrType) || "long".equals(attrType)
                || "int".equals(attrType)) {
            return true;
        }
        return false;
    }

    // 判断 java 类型是否为符点型
    public static boolean isFloat(String attrType) {
        if ("Double".equals(attrType) || "Float".equals(attrType) || "double".equals(attrType)
                || "float".equals(attrType)) {
            return true;
        }
        return false;
    }

    /**
     * 
     *
     * @Title: convertJdbcTypeToClassName
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 8, 2023 4:03:28 PM
     * @param sourceJdbcType
     * @return
     * @return String
     */
    public static String convertJdbcTypeToClassName(String sourceJdbcType) {
        String javaClassSimpleName = null;
        String jdbcType = sourceJdbcType.toUpperCase();

        int[] lengths = convertJdbcLength(jdbcType);

        if (jdbcType.indexOf("(") != -1 && jdbcType.indexOf(")") != -1) {
            jdbcType = jdbcType.substring(0, jdbcType.indexOf("("));
        }

        // 设置java类型
        switch (jdbcType) {
            case "TINYINT":
                javaClassSimpleName = "Integer";
                break;
            case "JSON":
                javaClassSimpleName = "ZKJson";
                break;
            case "DATETIME":
            case "DATE":
            case "TIMESTAMP":
                javaClassSimpleName = "Date";
                break;
            case "NUMBER":
            case "DOUBLE":
            case "FLOAT":
            case "INT":
                // 数字类型如果长度大于了 (19, ~) 使用 BigInteger/BigDecimal; (9-19] 使用 Long/Double; (~, 9] 使用 Integer/Float
                if (lengths == null) {
                    javaClassSimpleName = "BigInteger";
                }
                else {
                    if (lengths[0] > 19) {
                        if (lengths[1] > 0) {
                            javaClassSimpleName = "BigDecimal";
                        }
                        else {
                            javaClassSimpleName = "BigInteger";
                        }
                    }
                    else if (lengths[0] > 9) {
                        if (lengths[1] > 0) {
                            javaClassSimpleName = "Double";
                        }
                        else {
                            javaClassSimpleName = "Long";
                        }
                    }
                    else {
                        if (lengths[1] > 0) {
                            javaClassSimpleName = "Float";
                        }
                        else {
                            javaClassSimpleName = "Integer";
                        }
                    }
                }
                break;
            case "BIGINT":
                javaClassSimpleName = "BigInteger";
                break;
            case "DECIMAL":
                javaClassSimpleName = "BigDecimal";
                break;
            default:
                javaClassSimpleName = "String";
        }
        log.info("[^_^:20230308-1940-001] jdbcType: [{}] 对应 java 类型: [{}] ", sourceJdbcType, javaClassSimpleName);
        return javaClassSimpleName;
    }

    /**
     * 根据 jdbc 类型取长度；没长度时，返回 null;
     *
     * @Title: convertJdbcLength
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 1, 2021 12:46:50 PM
     * @param jdbcType
     * @return
     * @return int[]
     */
    public static int[] convertJdbcLength(String sourceJdbcType) {
        try {
            String jdbcType = sourceJdbcType.toUpperCase();
            if (jdbcType.startsWith("ENUM")) {
                // ENUM 枚举类型
                return new int[] { 2, 0 };
            }
            if (jdbcType.indexOf("(") != -1 && jdbcType.indexOf(")") != -1) {
                String length = jdbcType.substring(jdbcType.indexOf("(") + 1, jdbcType.indexOf(")"));
                jdbcType = jdbcType.substring(0, jdbcType.indexOf("("));

                String[] str = length.split(",");
                int[] lengths = new int[2];
                lengths[0] = Integer.parseInt(str[0]);
                if (str.length == 2 && Integer.parseInt(str[1]) > 0) {
                    lengths[1] = Integer.parseInt(str[1]);
                }
                else {
                    lengths[1] = 0;
                }
                return lengths;
            }
            return null;
        }
        catch(Exception e) {
            log.error("[>_<:20211126-1209-001] 取 jdbcType 长度失败；jdbcType:{}", sourceJdbcType);
            throw e;
        }
    }

}
