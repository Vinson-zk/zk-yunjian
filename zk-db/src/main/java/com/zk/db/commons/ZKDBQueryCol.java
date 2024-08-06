package com.zk.db.commons;


/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 查询 条件字段定义
 * @ClassName ZKDBQueryCol
 * @Package com.zk.db.commons
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-13 01:15:53
 **/
public class ZKDBQueryCol extends ZKDBQuery{

//    private Logger log = LogManager.getLogger(getClass());

    /**
     *
     * @Title: as
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 15, 2022 11:48:38 AM
     * @param queryType
     * @param columnName
     *            表 字段名；
     * @param attrName
     *            java 属性名; 这里会添加 #{}
     * @param javaClassz
     *            查询字段的 java 数据类型
     * @param formats
     *            日期格式化参数；不为 null 时，格式化数据库字段；不为 null 且属性 java 数据类型为 Date 时，同时也用他格式化参数；
     * @param isCaseSensitive
     *            是否区分大小，注，此参数仅在 javaClassz 为 String.class 类型下有效；true-区分大小；false-不区分大小写；
     * @return ZKDBQueryCol
     */
    public static ZKDBQueryCol as(ZKDBOptComparison queryType, String columnName, String attrName, Class<?> javaClassz,
        String[] formats, boolean isCaseSensitive) {
        return new ZKDBQueryCol(queryType, columnName, attrName, javaClassz, formats, isCaseSensitive);
    }

    public static ZKDBQueryCol as(ZKDBOptComparison queryType, String columnName, String attrName, Class<?> javaClassz,
                                  String[] formats, boolean isCaseSensitive, String prefix, String suffix) {
        return new ZKDBQueryCol(queryType, columnName, attrName, javaClassz, formats, isCaseSensitive, prefix, suffix);
    }

    // 查询方式
    private ZKDBOptComparison queryType;

    // 条件字段名
    private String columnName;

    // 条件属性名
    private String attrName;

    // 条件值 javaType
    private Class<?> javaClassz;

    // 格式化参数
    private String[] formats;

    // 是否区分大小写; 字段属性为 String/ZKJson 时，生效；true-区分大小写；false-不区分大小写；
    private boolean isCaseSensitive;

//    protected  ZKDBQueryCol(){}

    protected ZKDBQueryCol(ZKDBOptComparison queryType, String columnName, String attrName, Class<?> javaClassz,
                           String[] formats, boolean isCaseSensitive) {
        super(null, null);
        this.columnName = columnName;
        this.attrName = attrName;
        this.javaClassz = javaClassz;
        this.isCaseSensitive = isCaseSensitive;
        this.queryType = queryType;
        this.formats = formats;
    }

    protected ZKDBQueryCol(ZKDBOptComparison queryType, String columnName, String attrName, Class<?> javaClassz,
                           String[] formats, boolean isCaseSensitive, String prefix, String suffix) {
        super(prefix, suffix);
        this.columnName = columnName;
        this.attrName = attrName;
        this.javaClassz = javaClassz;
        this.isCaseSensitive = isCaseSensitive;
        this.queryType = queryType;
        this.formats = formats;
    }

    /**
     * @return queryType sa
     */
    public ZKDBOptComparison getQueryType() {
        return queryType;
    }

    /**
     * @return columnName sa
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @return attrName sa
     */
    public String getAttrName() {
        return attrName;
    }

    /**
     * @return javaClassz sa
     */
    public Class<?> getJavaClassz() {
        return javaClassz;
    }

    /**
     * @return formats sa
     */
    public String[] getFormats() {
        return formats;
    }

    /**
     * @return isCaseSensitive sa
     */
    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }

    @Override
    protected void convertQueryCondition(ZKSqlConvert convert, StringBuffer sb, String tableAlias,
        ZKDBOptLogic queryLogic) {
        convert.convertQueryCondition(queryLogic, sb, this, tableAlias);
    }
}
