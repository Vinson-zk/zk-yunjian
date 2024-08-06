/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.org.dao;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.FetchType;

import com.zk.base.dao.ZKBaseTreeDao;
import com.zk.base.myBaits.provider.ZKMyBatisTreeSqlProvider;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.org.entity.ZKSysOrgDept;

/**
 * ZKSysOrgDeptDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysOrgDeptDao extends ZKBaseTreeDao<String, ZKSysOrgDept> {

	/**
     * 树形查询 fetchType = FetchType.EEAGER
     * 不能改为懒加截，不然会报错：com.linde.wms.dto.BinGroupInfoDto_$$_jvst38e_0["handler"])
     * 虽然可以用 @JsonIgnoreProperties(value = "handler") 解决报错，但查出的数据还是错的；
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTree")
    @Results(id = "treeResult", value = {
            @Result(column = "{parentId=pkId,delFlag=delFlag}", property = "children", javaType = List.class, many = @Many(select = "com.zk.sys.org.dao.ZKSysOrgDeptDao.findTree", fetchType = FetchType.EAGER)) })
    List<ZKSysOrgDept> findTree(ZKSysOrgDept sysOrgDept);

	/**
     * 查询详情，包含父节点 fetchType = FetchType.EEAGER
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTreeDetail")
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKSysOrgDept.class, one = @One(select = "com.zk.sys.org.dao.ZKSysOrgDeptDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKSysOrgDept getDetail(ZKSysOrgDept sysOrgDept);

    /**
     * 根据代码取部门
     *
     * @Title: getByCode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 20, 2022 8:48:26 AM
     * @param tn
     * @param alias
     * @param sCols
     * @param companyId
     * @param code
     * @return
     * @return ZKSysOrgDept
     */
    @Select(value = { "SELECT ${sCols} FROM ${tn} ${alias} WHERE c_company_id = #{companyId} AND c_code = #{code}" })
    ZKSysOrgDept getByCode(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("companyId") String companyId, @Param("code") String code);
	
}