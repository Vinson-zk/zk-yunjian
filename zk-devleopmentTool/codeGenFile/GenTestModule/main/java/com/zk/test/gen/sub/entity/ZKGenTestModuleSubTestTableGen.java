/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.test.gen.sub.entity;

import com.zk.db.commons.ZKDBOptComparison;
import java.lang.String;
import com.zk.core.commons.data.ZKJson;

import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotEmpty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlTransient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;

import com.zk.db.annotation.ZKTable;
import com.zk.db.annotation.ZKColumn;
import com.zk.db.annotation.ZKUpdate;
import com.zk.db.annotation.ZKQuery;
import com.zk.db.commons.ZKSqlConvertDelegating;
import com.zk.db.mybatis.commons.ZKDBSqlHelper;

import com.zk.base.entity.ZKBaseEntity;

/**
 * 测试代码生成
 * @author 
 * @version 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ZKTable(name = "t_test_table_gen", alias = "genTestModuleSubTestTableGen", orderBy = " c_create_date ASC ")
public class ZKGenTestModuleSubTestTableGen extends ZKBaseEntity<Date, ZKGenTestModuleSubTestTableGen> {
	
	static ZKDBSqlHelper sqlHelper;

	@Transient
    @XmlTransient
    @JsonIgnore
    @Override
    public ZKDBSqlHelper getSqlHelper() {
        return sqlHelper();
    }

	@Transient
    @XmlTransient
    @JsonIgnore
    public static ZKDBSqlHelper sqlHelper() {
        if (sqlHelper == null) {
            sqlHelper = new ZKDBSqlHelper(new ZKSqlConvertDelegating(), new ZKGenTestModuleSubTestTableGen());
        }
        return sqlHelper;
    }
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * tt
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Length(min = 1, max = 63, message = "{zk.core.data.validation.length.max}")
	@ZKColumn(name = "c_testa", isInsert = true, javaType = String.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	String testa;	
	/**
	 * tt
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_testb", isInsert = true, javaType = Long.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Long testb;	
	/**
	 * tt
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@Range(min = 0, max = 999999999, message = "{zk.core.data.validation.rang.int}")
	@ZKColumn(name = "c_testc", isInsert = true, javaType = Long.class, query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	Long testc;	
	/**
	 * tt
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_test_json", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true)query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson testJson;	
	/**
	 * tt_2
	 */
	@NotNull(message = "{zk.core.data.validation.notNull}")
	@NotEmpty(message = "{zk.core.data.validation.notNull}")
	@ZKColumn(name = "c_test_json_2", isInsert = true, javaType = ZKJson.class, update = @ZKUpdate(true)query = @ZKQuery(queryType = ZKDBOptComparison.EQ))
	ZKJson testJson2;	
	
	public ZKGenTestModuleSubTestTableGen() {
		super();
	}

	public ZKGenTestModuleSubTestTableGen(Date pkId){
		super(pkId);
	}
	
	/**
	 * tt	
	 */	
	public String getTesta() {
		return testa;
	}
	
	/**
	 * tt
	 */	
	public void setTesta(String testa) {
		this.testa = testa;
	}
	/**
	 * tt	
	 */	
	public Long getTestb() {
		return testb;
	}
	
	/**
	 * tt
	 */	
	public void setTestb(Long testb) {
		this.testb = testb;
	}
	/**
	 * tt	
	 */	
	public Long getTestc() {
		return testc;
	}
	
	/**
	 * tt
	 */	
	public void setTestc(Long testc) {
		this.testc = testc;
	}
	/**
	 * tt	
	 */	
	public ZKJson getTestJson() {
		return testJson;
	}
	
	/**
	 * tt
	 */	
	public void setTestJson(ZKJson testJson) {
		this.testJson = testJson;
	}
	/**
	 * tt_2	
	 */	
	public ZKJson getTestJson2() {
		return testJson2;
	}
	
	/**
	 * tt_2
	 */	
	public void setTestJson2(ZKJson testJson2) {
		this.testJson2 = testJson2;
	}
	
	
}