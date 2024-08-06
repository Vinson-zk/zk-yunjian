/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.file.dao;

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
import com.zk.file.entity.ZKFileInfo;

/**
 * 
 * ZKFileInfoDAO接口
 * 
 * @author
 * @version 1.0
 */
@ZKMyBatisDao
public interface ZKFileInfoDao extends ZKBaseTreeDao<String, ZKFileInfo> {

    /**
     * 树形查询，此方法为递归查询树型节点；1.不支持子节点过虑; 2.仅支持根节点过滤与分页； fetchType = FetchType.EEAGER 不能改为懒加截，不然会报错：com.linde.wms.dto.BinGroupInfoDto_$$_jvst38e_0["handler"])
     * 虽然可以用 @JsonIgnoreProperties(value = "handler") 解决报错，但查出的数据还是错的；
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTree")
    @Results(id = "treeResult", value = {
            @Result(column = "{parentId=pkId}", property = "children", javaType = List.class, many = @Many(select = "com.zk.file.dao.ZKFileInfoDao.findTree", fetchType = FetchType.EAGER)) })
    List<ZKFileInfo> findTree(ZKFileInfo fileInfo);

    /**
     * 查询详情，包含父节点 fetchType = FetchType.EEAGER
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTreeDetail")
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKFileInfo.class, one = @One(select = "com.zk.file.dao.ZKFileInfoDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKFileInfo getDetail(ZKFileInfo fileInfo);

    @Select({ "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} ",
            "WHERE ${_zkSql.ta}.c_company_code = #{companyCode} AND ${_zkSql.ta}.c_code = #{code} " })
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKFileInfo.class, one = @One(select = "com.zk.file.dao.ZKFileInfoDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKFileInfo getByCode(@Param("companyCode") String companyCode, @Param("code") String code);

    @Select({ "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} ",
            "WHERE ${_zkSql.ta}.c_save_uuid = #{saveUuid} " })
    ZKFileInfo getBySaveUuid(@Param("saveUuid") String saveUuid);

    @Select({ "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} ",
            "WHERE ${_zkSql.ta}.c_company_code = #{companyCode} AND ${_zkSql.ta}.c_name = #{name} " })
    ZKFileInfo getByName(@Param("companyCode") String companyCode, @Param("name") String name);

    /**
     * 查询指定公司下，指定目录下，最大的排序值
     */
    @Select({ "<script>SELECT max(${_zkSql.ta}.c_sort) FROM ${_zkSql.tn} ${_zkSql.ta} ",
            "<where> ${_zkSql.ta}.c_company_code = #{companyCode} ",
            "<if test=\"parentId = null or parentId = '' \">AND ${_zkSql.ta}.c_parent_id is null</if>",
            "<if test=\"parentId != null and parentId != '' \">AND ${_zkSql.ta}.c_parent_id = #{parentId}</if>",
            "</where></script>" })
    Integer selectMaxSort(@Param("companyCode") String companyCode, @Param("parentId") String parentId);

}
