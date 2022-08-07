/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.dao;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
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
     * 树形查询 fetchType = FetchType.EEAGER
     * 不能改为懒加截，不然会报错：com.linde.wms.dto.BinGroupInfoDto_$$_jvst38e_0["handler"])
     * 虽然可以用 @JsonIgnoreProperties(value = "handler") 解决报错，但查出的数据还是错的；
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTree")
    @Results(id = "treeResult", value = {
            @Result(column = "{parentId=pkId}", property = "children", javaType = List.class, many = @Many(select = "com.zk.file.dao.ZKFileDirectoryDao.findTreeChild", fetchType = FetchType.EAGER)) })
    List<ZKFileDirectory> findTree(ZKFileDirectory fileDirectory);

    /**
     * 树形查询的子节点查询
     *
     * @Title: findTreeChild
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jan 6, 2021 10:09:10 PM
     * @param sysMenu
     * @return List<T>
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTree")
    @ResultMap("treeResult")
    List<ZKFileDirectory> findTreeChild(ZKFileDirectory fileDirectory);

	/**
     * 查询详情，包含父节点 fetchType = FetchType.EEAGER
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTreeDetail")
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKFileDirectory.class, one = @One(select = "com.zk.file.dao.ZKFileDirectoryDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKFileDirectory getDetail(ZKFileDirectory fileDirectory);

    @Select("<script>SELECT ${sCols} FROM ${tn} ${ta} WHERE ${ta}.c_company_code = #{companyCode} and ${ta}.c_code = #{code} <if test='delFlag != null'> and ${alias}.c_del_flag = #{delFlag} </if> </script>")
    ZKFileDirectory getByCode(@Param("tn") String tn, @Param("ta") String ta, @Param("sCols") String sCols,
            @Param("companyCode") String companyCode, @Param("code") String code, @Param("delFlag") Integer delFlag);
	
}