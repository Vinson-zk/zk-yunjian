/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.org.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import com.zk.base.dao.ZKBaseTreeDao;
import com.zk.base.myBaits.provider.ZKMyBatisTreeSqlProvider;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.entity.org.ZKSysOrgCompany;

/**
 * ZKSysOrgCompanyDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysOrgCompanyDao extends ZKBaseTreeDao<String, ZKSysOrgCompany> {

	/**
     * 树形查询 fetchType = FetchType.EEAGER
     * 不能改为懒加截，不然会报错：com.linde.wms.dto.BinGroupInfoDto_$$_jvst38e_0["handler"])
     * 虽然可以用 @JsonIgnoreProperties(value = "handler") 解决报错，但查出的数据还是错的；
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTree")
    @Results(id = "treeResult", value = {
            @Result(column = "{parentId=pkId}", property = "children", javaType = List.class, many = @Many(select = "com.zk.sys.org.dao.ZKSysOrgCompanyDao.findTree", fetchType = FetchType.EAGER)) })
    List<ZKSysOrgCompany> findTree(ZKSysOrgCompany sysOrgCompany);

	/**
     * 查询详情，包含父节点 fetchType = FetchType.EEAGER
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTreeDetail")
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKSysOrgCompany.class, one = @One(select = "com.zk.sys.org.dao.ZKSysOrgCompanyDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKSysOrgCompany getDetail(ZKSysOrgCompany sysOrgCompany);

    /**
     * 根据公司代码取公司
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 11, 2022 9:25:21 AM
     * @param tn
     * @param sCols
     * @param code
     * @return
     * @return ZKSysOrgCompany
     */
    @Select(value = { "SELECT ${sCols} FROM ${tn} ${alias} WHERE c_code = #{code}" })
    ZKSysOrgCompany getByCode(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("code") String code);

    /**
     * 审核公司 c_update_user_id c_update_date
     *
     * @Title: approveCompany
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 12, 2022 3:36:32 PM
     * @param tn
     * @param companyId
     * @param status
     * @param userId
     * @param date
     * @return
     * @return int
     */
    @Update(" UPDATE ${tn} SET c_status = #{status}, c_update_user_id = #{userId}, c_update_date = #{date} WHERE c_pk_id = #{companyId} ")
    int approveCompany(@Param("tn") String tn, @Param("companyId") String companyId, @Param("status") int status,
            @Param("userId") String userId, @Param("date") Date date);
	
}