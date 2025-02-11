/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.iot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.FetchType;

import com.zk.base.dao.ZKBaseTreeDao;
import com.zk.base.myBaits.provider.ZKMyBatisTreeSqlProvider;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.iot.entity.ZKIotProdCategores;

/**
 * ZKIotProdCategoresDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKIotProdCategoresDao extends ZKBaseTreeDao<String, ZKIotProdCategores> {

	/**
     * 树形查询，此方法为递归查询树型节点；1.不支持子节点过虑; 2.仅支持根节点过滤与分页；
     * fetchType = FetchType.EEAGER 不能改为懒加截，不然会报错：com.linde.wms.dto.BinGroupInfoDto_$$_jvst38e_0["handler"])
     * 虽然可以用 @JsonIgnoreProperties(value = "handler") 解决报错，但查出的数据还是错的；
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTree")
    @Results(id = "treeResult", value = {
            @Result(column = "{parentId=pkId}", property = "children", javaType = List.class, many = @Many(select = "com.zk.iot.dao.ZKIotProdCategoresDao.findTree", fetchType = FetchType.EAGER)) })
    List<ZKIotProdCategores> findTree(ZKIotProdCategores iotProdCategores);

	/**
     * 查询详情，包含父节点 fetchType = FetchType.EEAGER
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTreeDetail")
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKIotProdCategores.class, one = @One(select = "com.zk.iot.dao.ZKIotProdCategoresDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKIotProdCategores getDetail(ZKIotProdCategores iotProdCategores);

    // 根据代码取实体及明细
    @Select({
            "SELECT ${_zkSql.cols} FROM ${_zkSql.tn} ${_zkSql.ta} WHERE ${_zkSql.ta}.c_prod_categores_code = #{prodCategoresCode} " })
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKIotProdCategores.class, one = @One(select = "com.zk.iot.dao.ZKIotProdCategoresDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKIotProdCategores getByCode(@Param("prodCategoresCode") String prodCategoresCode);

    // 修改状态
    @Update(" UPDATE ${_tn} SET c_status = #{status} WHERE c_pk_id = #{pkId} ")
    int updateStatus(@Param("_tn") String tn, @Param("pkId") String pkId, @Param("status") int status);

    // 取指定父节点下的子节点的最大排序号
    @Select({
            "<script>SELECT max(c_sort) FROM ${_zkSql.tn} WHERE ",
            "<choose><when test=\"parentId == null or parentId == '' \">c_parent_id is null</when><otherwise>c_parent_id = #{parentId}</otherwise></choose>",
            "</script>" })
    Integer getMaxSort(@Param("parentId") String parentId);
	
}
