/**
 * Copyright &copy; All rights reserved.
 */
package com.zk.sys.org.dao;

import java.util.Date;

import org.apache.ibatis.annotations.Delete;
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
    @Update(" UPDATE ${tn} SET c_password = #{pwd}, c_update_date = #{updateDate} WHERE c_pk_id = #{pkId} ")
    int updatePwd(@Param("tn") String tn, @Param("pkId") String pkId, @Param("pwd") String pwd,
            @Param("updateDate") Date date);

    // 修改账号
    @Update(" UPDATE ${tn} SET c_account = #{account}, c_update_date = #{updateDate} WHERE c_pk_id = #{pkId} ")
    int updateAccount(@Param("tn") String tn, @Param("pkId") String pkId, @Param("account") String account,
            @Param("updateDate") Date date);

    // 修改手机号
    @Update(" UPDATE ${tn} SET c_phone_num = #{phoneNum}, c_update_date = #{updateDate} WHERE c_pk_id = #{pkId} ")
    int updatePhoneNum(@Param("tn") String tn, @Param("pkId") String pkId, @Param("phoneNum") String phoneNum,
            @Param("updateDate") Date date);

    // 修改邮箱
    @Update(" UPDATE ${tn} SET c_mail = #{mail}, c_update_date = #{updateDate} WHERE c_pk_id = #{pkId} ")
    int updateMail(@Param("tn") String tn, @Param("pkId") String pkId, @Param("mail") String mail,
            @Param("updateDate") Date date);

    // 修改状态
    @Update(" UPDATE ${tn} SET c_status = #{status}, c_update_date = #{updateDate} WHERE c_pk_id = #{pkId} ")
    int updateStatus(@Param("tn") String tn, @Param("pkId") String pkId, @Param("status") int status,
            @Param("updateDate") Date date);

    // 注销账号
    @Update({ "UPDATE ${tn} SET", //
            "c_account = #{account}, c_mail = null, c_phone_num = null, c_tel_num = null, c_address = null, c_qq = null, c_wechat = null,", //
            "c_family_name = null, c_second_name = null, c_nickname = null, c_head_photo = null, c_head_photo_original = null,", //
            "c_status = #{status}, c_del_flag = #{delFlag}, c_leave_date = #{updateDate}, c_update_date = #{updateDate}", //
            "WHERE c_pk_id = #{pkId} " })
    int closeAccount(@Param("tn") String tn, @Param("pkId") String pkId, @Param("account") String account,
            @Param("status") int status, @Param("delFlag") int delFlag, @Param("updateUserId") String updateUserId,
            @Param("updateDate") Date date);

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

    // 根据公司ID做逻辑删除
    @Delete("UPDATE ${tn} SET c_del_flag = #{delFlag}, c_update_user_id = #{updateUserId}, c_update_date = #{updateDate} WHERE c_company_id = #{companyId} ")
    int delByCompanyId(@Param("tn") String tn, @Param("companyId") String companyId, @Param("delFlag") int delFlag,
            @Param("updateUserId") String updateUserId, @Param("updateDate") Date updateDate);

    // 根据公司ID做物理删除
    @Delete("DELETE FROM ${tn} WHERE c_company_id = #{companyId}")
    int diskDelByCompanyId(@Param("tn") String tn, @Param("companyId") String companyId);
	
}




