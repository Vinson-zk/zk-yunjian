package com.zk.demo.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.db.mybatis.dao.ZKDBBaseDao;
import com.zk.demo.entity.ZKJavaDemoEntity;

/**
 * @author bs
 */
@ZKMyBatisDao
public interface ZKJavaDemoDao extends ZKDBBaseDao<ZKJavaDemoEntity> {

    @Select("select ${sCols} from ${tn} ${ta} where ${ta}.c_id = #{pkId} for update")
    public ZKJavaDemoEntity getForUpdate(@Param("tn") String tn, @Param("ta") String ta, @Param("sCols") String sCols,
                                         @Param("pkId") String pkId);
}
