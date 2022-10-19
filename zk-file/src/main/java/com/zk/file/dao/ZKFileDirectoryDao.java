/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.dao;

import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import com.zk.base.dao.ZKBaseTreeDao;
import com.zk.base.myBaits.provider.ZKMyBatisTreeSqlProvider;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.file.entity.ZKFileDirectory;

/**
 * ZKFileDirectoryDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKFileDirectoryDao extends ZKBaseTreeDao<String, ZKFileDirectory> {

	/**
     * 树形查询，此方法为递归查询树型节点；1.不支持子节点过虑; 2.仅支持根节点过滤与分页；
     * fetchType = FetchType.EEAGER 不能改为懒加截，不然会报错：com.linde.wms.dto.BinGroupInfoDto_$$_jvst38e_0["handler"])
     * 虽然可以用 @JsonIgnoreProperties(value = "handler") 解决报错，但查出的数据还是错的；
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTree")
    @Results(id = "treeResult", value = {
            @Result(column = "{parentId=pkId}", property = "children", javaType = List.class, many = @Many(select = "com.zk.file.dao.ZKFileDirectoryDao.findTree", fetchType = FetchType.EAGER)) })
    List<ZKFileDirectory> findTree(ZKFileDirectory fileDirectory);

	/**
     * 查询详情，包含父节点 fetchType = FetchType.EEAGER
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTreeDetail")
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKFileDirectory.class, one = @One(select = "com.zk.file.dao.ZKFileDirectoryDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKFileDirectory getDetail(ZKFileDirectory fileDirectory);


    @Select({"SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} ",
            "WHERE ${_zkSql.ta}.c_company_code = #{companyCode} AND ${_zkSql.ta}.c_code = #{code} "})
    ZKFileDirectory getByCode(@Param("companyCode") String companyCode, @Param("code")String code);
	
}