/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.res.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Transient;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKJsonArray;
import com.zk.core.utils.ZKIdUtils;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKTable;
import com.zk.db.commons.ZKDBQueryType;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.commons.ZKSqlProvider;

/**
 * 功能 API
 * @author 
 * @version 
 */
@ZKTable(name = "t_sys_res_func_api", alias = "sysResFuncApi", orderBy = " c_create_date ASC ")
public class ZKSysResFuncApi extends ZKBaseEntity<String, ZKSysResFuncApi> {
	
	static ZKSqlProvider sqlProvider;
	
    @Override 
    public ZKSqlProvider getSqlProvider() {
        return initSqlProvider();
    }
    
    public static ZKSqlProvider initSqlProvider() {
        if(sqlProvider == null) {
            sqlProvider = new ZKSqlProvider(new ZKSqlConvertDelegating(), new ZKSysResFuncApi());
        }
        return sqlProvider;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 功能API 名称
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_name", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	ZKJson name;	

    /**
     * 应用系统代码
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_system_code", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.EQ)
    String systemCode;

	/**
	 * api 标识代码，同代码中权限注解代码；项目下唯一
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 64, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_code", isInsert = true, isUpdate = false, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
    String code;

	/**
	 * 应用场景说明
	 */
	@ZKColumn(name = "c_use_context", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = false)
	ZKJson useContext;	
	
    /**
     * 请求内容类型;
     * 
     * ENUM('application/x-www-form-urlencoded', 'application/json', 'application/octet-stream', 'text/html', 'multipart/form-data', 'text/plain')
     */
	@NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 128, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_req_content_type", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
    String reqContentType;

	/**
	 * 请求说明
	 */
	@ZKColumn(name = "c_req_desc", isInsert = true, isUpdate = true, javaType = ZKJson.class, isQuery = false)
	ZKJson reqDesc;	
	/**
	 * 请求参数说明
	 */
    @ZKColumn(name = "c_req_params_desc", isInsert = true, isUpdate = true, javaType = ZKJsonArray.class, isQuery = false)
    ZKJsonArray reqParamsDesc;
	
    /**
     * 响应内容类型;
     * 
     * ENUM('application/x-www-form-urlencoded', 'application/json', 'application/octet-stream', 'text/html', 'multipart/form-data', 'text/plain')
     */
    @NotNull(message = "{zk.core.data.validation.notNull}")
    @Length(min = 1, max = 128, message = "{zk.core.data.validation.length.max}")
    @ZKColumn(name = "c_res_content_type", isInsert = true, isUpdate = true, javaType = String.class, isQuery = false)
    String resContentType;

	/**
	 * 响应说明
	 */
    @ZKColumn(name = "c_res_data_desc", isInsert = true, isUpdate = true, javaType = ZKJsonArray.class, isQuery = false)
    ZKJsonArray resDataDesc;

	/**
	 * 功能访问源地址
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_original_uri", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String originalUri;	
	/**
	 * 功能访问代理地址
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 512, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_agent_uri", isInsert = true, isUpdate = true, javaType = String.class, isQuery = true, queryType = ZKDBQueryType.LIKE)
	String agentUri;	
	
    /**
     * 支持的请求方法；位与表示，0-支持、1-不支持：1位-GET、2位-POST、3位-DELETE、4位-PUT、5位-CONNECT、6位-HEAD、7位-OPTIONS、8位、TRACE；
     */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_req_methods", isInsert = true, isUpdate = true, javaType = Long.class, isQuery = false)
    Integer reqMethods;

    /******************************************************************/
    /*** 辅助属性 */
    /******************************************************************/

    /**
     * 应用系统项目实体；查询明细 get 时，会级联查询出来
     */
    @Transient
    ZKSysResApplicationSystem sysResApplicationSystem;

    /**
     * 支持的请求方法
     * 
     * 1-GET、2-POST、3-DELETE、4-PUT、5-CONNECT、6-HEAD、7-OPTIONS、8、TRACE；
     * 
     * @ClassName: ReqMethod
     * @author Vinson
     * @version 1.0
     */
    public static interface ReqMethod {
        public static final int GET = 1;

        public static final int POST = 2;

        public static final int DELETE = 4;

        public static final int PUT = 8;

        public static final int CONNECT = 16;

        public static final int HEAD = 32;

        public static final int OPTIONS = 64;

        public static final int TRACE = 128;
    }
	
	public ZKSysResFuncApi() {
		super();
	}

	public ZKSysResFuncApi(String pkId){
		super(pkId);
	}
	
	/**
	 * 功能API 名称	
	 */	
	public ZKJson getName() {
		return name;
	}
	
	/**
	 * 功能API 名称
	 */	
	public void setName(ZKJson name) {
		this.name = name;
	}
	/**
	 * api 标识代码，同代码中权限注解代码；项目下唯一	
	 */	
	public String getCode() {
		return code;
	}
	
	/**
	 * api 标识代码，同代码中权限注解代码；项目下唯一
	 */	
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * 应用系统代码	
	 */	
	public String getSystemCode() {
		return systemCode;
	}
	
	/**
	 * 应用系统代码
	 */	
	public void setSystemCode(String systemCode) {
		this.systemCode = systemCode;
	}
	/**
	 * 应用场景说明	
	 */	
	public ZKJson getUseContext() {
		return useContext;
	}
	
	/**
	 * 应用场景说明
	 */	
	public void setUseContext(ZKJson useContext) {
		this.useContext = useContext;
	}
	/**
	 * 请求内容类型	
	 */	
    public String getReqContentType() {
		return reqContentType;
	}
	
	/**
	 * 请求内容类型
	 */	
    public void setReqContentType(String reqContentType) {
		this.reqContentType = reqContentType;
	}
	/**
	 * 请求说明	
	 */	
	public ZKJson getReqDesc() {
		return reqDesc;
	}
	
	/**
	 * 请求说明
	 */	
	public void setReqDesc(ZKJson reqDesc) {
		this.reqDesc = reqDesc;
	}
	/**
	 * 请求参数说明	
	 */	
    public ZKJsonArray getReqParamsDesc() {
		return reqParamsDesc;
	}
	
	/**
	 * 请求参数说明
	 */	
    public void setReqParamsDesc(ZKJsonArray reqParamsDesc) {
		this.reqParamsDesc = reqParamsDesc;
	}
	/**
	 * 响应内容类型	
	 */	
    public String getResContentType() {
		return resContentType;
	}
	
	/**
	 * 响应内容类型
	 */	
    public void setResContentType(String resContentType) {
		this.resContentType = resContentType;
	}
	/**
	 * 响应说明	
	 */	
    public ZKJsonArray getResDataDesc() {
        return resDataDesc;
	}
	
	/**
	 * 响应说明
	 */	
    public void setResDataDesc(ZKJsonArray resDataDesc) {
        this.resDataDesc = resDataDesc;
	}
	/**
	 * 功能访问源地址	
	 */	
	public String getOriginalUri() {
		return originalUri;
	}
	
	/**
	 * 功能访问源地址
	 */	
	public void setOriginalUri(String originalUri) {
		this.originalUri = originalUri;
	}
	/**
	 * 功能访问代理地址	
	 */	
	public String getAgentUri() {
		return agentUri;
	}
	
	/**
	 * 功能访问代理地址
	 */	
	public void setAgentUri(String agentUri) {
		this.agentUri = agentUri;
	}
	/**
	 * 支持的请求方法；位与表示，0-支持、1-不支持：1-GET、2-POST、3-DELETE、4-PUT、5-CONNECT、6-HEAD、7-OPTIONS、8、TRACE；	
	 */	
    public Integer getReqMethods() {
		return reqMethods;
	}
	
	/**
	 * 支持的请求方法；位与表示，0-支持、1-不支持：1-GET、2-POST、3-DELETE、4-PUT、5-CONNECT、6-HEAD、7-OPTIONS、8、TRACE；
	 */	
    public void setReqMethods(Integer reqMethods) {
		this.reqMethods = reqMethods;
	}
	
	/**
	 * 根据主键类型，重写主键生成；
	 */
	@Override
	protected String genId() {
        return ZKIdUtils.genLongStringId();
    }

    /**
     * @return sysResApplicationSystem sa
     */
    public ZKSysResApplicationSystem getSysResApplicationSystem() {
        return sysResApplicationSystem;
    }

    /**
     * @param sysResApplicationSystem
     *            the sysResApplicationSystem to set
     */
    public void setSysResApplicationSystem(ZKSysResApplicationSystem sysResApplicationSystem) {
        this.sysResApplicationSystem = sysResApplicationSystem;
    }
	
}