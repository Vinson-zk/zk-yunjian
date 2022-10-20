/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.org.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zk.base.dao.ZKBaseDao;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.sys.org.entity.ZKSysOrgUser;

/**
 * ZKSysOrgUserDAO接口
 * @author 
 * @version  1.0
 */
@ZKMyBatisDao
public interface ZKSysOrgUserDao extends ZKBaseDao<String, ZKSysOrgUser> {

    // 修改状态

    /**
     * 修改密码
     *
     * @Title: updatePwd
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 11:23:01 AM
     * @param tn
     * @param pkId
     *            主键
     * @param pwd
     *            加密后的密码
     * @param pwdStatus
     *            密码状态
     * @param date
     *            修改日期
     * @return
     * @return int
     */
    @Update(" UPDATE ${tn} SET c_password = #{pwd}, c_pwd_status = #{pwdStatus}, c_pwd_last_update_date = #{updateDate} WHERE c_pk_id = #{pkId} ")
    int updatePwd(@Param("tn") String tn, @Param("pkId") String pkId, @Param("pwd") String pwd,
            @Param("pwdStatus") int pwdStatus, @Param("updateDate") Date date);

    /**
     * 根据账号查询用户；公司下账号唯一；
     *
     * @Title: getByAccount
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 8:28:57 AM
     * @param tn
     * @param alias
     * @param sCols
     * @param companyId
     * @param account
     * @return
     * @return ZKSysOrgUser
     */
    @Select(value = {
            "SELECT ${sCols} FROM ${tn} ${alias} WHERE c_company_id = #{companyId} AND c_account = #{account}" })
    ZKSysOrgUser getByAccount(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("companyId") String companyId, @Param("account") String account);

    /**
     * 根据手机查询用户；公司下手机号唯一；
     *
     * @Title: getByPhoneNum
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 8:29:18 AM
     * @param tn
     * @param alias
     * @param sCols
     * @param companyId
     * @param phoneNum
     * @return
     * @return ZKSysOrgRole
     */
    @Select(value = {
            "SELECT ${sCols} FROM ${tn} ${alias} WHERE c_company_id = #{companyId} AND c_phone_num = #{phoneNum}" })
    ZKSysOrgUser getByPhoneNum(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("companyId") String companyId, @Param("phoneNum") String phoneNum);

    /**
     * 根据邮箱查询用户，公司下邮箱唯一；
     *
     * @Title: getByMail
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 8:29:36 AM
     * @param tn
     * @param alias
     * @param sCols
     * @param companyId
     * @param mail
     * @return
     * @return ZKSysOrgRole
     */
    @Select(value = {
            "SELECT ${sCols} FROM ${tn} ${alias} WHERE c_company_id = #{companyId} AND c_mail = #{mail}" })
    ZKSysOrgUser getByMail(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("companyId") String companyId, @Param("mail") String mail);

    /**
     * 根据工号查询用户；工号公司下唯一，但注意，工号可以为空，所以在参数工号为 null 时，可能返回多个用户
     *
     * @Title: getByJobNum
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 3, 2022 3:40:21 PM
     * @param tn
     * @param alias
     * @param sCols
     * @param companyId
     * @param jobNum
     * @return
     * @return ZKSysOrgUser
     */
    @Select(value = {
            "SELECT ${sCols} FROM ${tn} ${alias} WHERE c_company_id = #{companyId} AND c_job_num = #{jobNum}" })
    ZKSysOrgUser getByJobNum(@Param("tn") String tn, @Param("alias") String alias, @Param("sCols") String sCols,
            @Param("companyId") String companyId, @Param("jobNum") String jobNum);
	
}