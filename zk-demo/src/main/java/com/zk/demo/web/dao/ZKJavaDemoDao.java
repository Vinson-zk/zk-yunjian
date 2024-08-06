package com.zk.demo.web.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.db.mybatis.dao.ZKDBDao;
import com.zk.demo.web.entity.ZKJavaDemoEntity;

/**
 * @author bs
 */
@ZKMyBatisDao
public interface ZKJavaDemoDao extends ZKDBDao<ZKJavaDemoEntity> {

    @Select("select ${sCols} from ${tn} ${ta} where ${ta}.c_id = #{pkId} for update")
    public ZKJavaDemoEntity getForUpdate(@Param("tn") String tn, @Param("ta") String ta, @Param("sCols") String sCols,
                                         @Param("pkId") String pkId);
}
