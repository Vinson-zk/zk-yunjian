/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.res.dao;

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
import com.zk.sys.res.entity.ZKSysResDict;

/**
 * ZKSysResDictDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysResDictDao extends ZKBaseTreeDao<String, ZKSysResDict> {

    /**
     * 树形查询菜单 fetchType = FetchType.EEAGER 不能改为懒加截，不然会报错：com.linde.wms.dto.BinGroupInfoDto_$$_jvst38e_0["handler"]) 虽然可以用 @JsonIgnoreProperties(value =
     * "handler") 解决报错，但查出的数据还是错的；
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTree")
    @Results(id = "treeResult", value = {
            @Result(column = "{parentId=pkId}", property = "children", javaType = List.class, many = @Many(select = "com.zk.sys.res.dao.ZKSysResDictDao.findTree", fetchType = FetchType.EAGER)) })
    List<ZKSysResDict> findTree(ZKSysResDict sysResDic);

    /**
     * 查询字典详情，包含父节点 fetchType = FetchType.EEAGER
     */
    @SelectProvider(type = ZKMyBatisTreeSqlProvider.class, method = "selectTreeDetail")
    @Results(value = {
            @Result(column = "{pkId=parentId}", property = "parent", javaType = ZKSysResDict.class, one = @One(select = "com.zk.sys.res.dao.ZKSysResDictDao.getDetail", fetchType = FetchType.EAGER)) })
    ZKSysResDict getDetail(ZKSysResDict sysResDic);

    @Select("SELECT ${sCols} FROM ${tn} ${tAlias} WHERE c_type_code = #{typeCode} and c_dict_code = #{dictCode} ")
    ZKSysResDict getByTypeCodeAndDictCode(@Param("tn") String tn, @Param("tAlias") String tAlias,
            @Param("sCols") String sCols, @Param("typeCode") String typeCode, @Param("dictCode") String dictCode);
	
}