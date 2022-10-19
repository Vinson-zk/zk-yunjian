/**
 * 
 */
package com.zk.sys.org.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.service.ZKBaseService;
import com.zk.core.encrypt.utils.ZKEncryptUtils;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.*;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.sys.auth.service.ZKSysAuthUserRoleService;
import com.zk.sys.auth.service.ZKSysAuthUserService;
import com.zk.sys.entity.org.ZKSysOrgCompany;
import com.zk.sys.org.dao.ZKSysOrgUserDao;
import com.zk.sys.org.entity.ZKSysOrgDept;
import com.zk.sys.org.entity.ZKSysOrgUser;

/**
 * ZKSysOrgUserService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysOrgUserService extends ZKBaseService<String, ZKSysOrgUser, ZKSysOrgUserDao> {

    @Autowired
    ZKSysOrgCompanyService sysOrgCompanyService;

    @Autowired
    ZKSysOrgDeptService sysOrgDeptService;

    @Autowired
    ZKSysAuthUserRoleService sysAuthUserRoleService;

    @Autowired
    ZKSysAuthUserService sysAuthUserService;

    /**
     * 取用户明细，含公司实体
     *
     * @Title: getDetail
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 26, 2022 4:51:38 PM
     * @param user
     * @return
     * @return ZKSysOrgUser
     */
    public ZKSysOrgUser getDetail(ZKSysOrgUser user) {
        if (user != null) {
            user.setCompany(this.sysOrgCompanyService.get(new ZKSysOrgCompany(user.getCompanyId())));
        }
        return user;
    }

    public ZKSysOrgUser getDetail(String userId) {
        ZKSysOrgUser user = super.get(new ZKSysOrgUser(userId));
        return this.getDetail(user);
    }

    /**
     * 根据用户账号取用户；用户账号公司下唯一
     *
     * @Title: getByAccount
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 9:00:42 AM
     * @param companyId
     * @param account
     * @return ZKSysOrgUser
     */
    public ZKSysOrgUser getByAccount(String companyId, String account) {
        if (ZKStringUtils.isEmpty(account) || ZKStringUtils.isEmpty(companyId)) {
            return null;
        }
        return this.dao.getByAccount(ZKSysOrgUser.sqlHelper().getTableName(), ZKSysOrgUser.sqlHelper().getTableAlias(),
            ZKSysOrgUser.sqlHelper().getBlockSqlCols(), companyId, account);
    }

    /**
     * 根据用户邮箱取用户；公司下邮箱唯一
     *
     * @Title: getByMail
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 9:01:31 AM
     * @param companyId
     * @param mail
     * @return
     * @return ZKSysOrgUser
     */
    public ZKSysOrgUser getByMail(String companyId, String mail) {
        if (ZKStringUtils.isEmpty(mail) || ZKStringUtils.isEmpty(companyId)) {
            return null;
        }
        return this.dao.getByMail(ZKSysOrgUser.sqlHelper().getTableName(), ZKSysOrgUser.sqlHelper().getTableAlias(),
            ZKSysOrgUser.sqlHelper().getBlockSqlCols(), companyId, mail);
    }

    /**
     * 根据手机号码取用户；公司下手机号码唯一
     *
     * @Title: getByPhoneNum
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 9:01:50 AM
     * @param companyId
     * @param phoneNum
     * @return
     * @return ZKSysOrgUser
     */
    public ZKSysOrgUser getByPhoneNum(String companyId, String phoneNum) {
        if (ZKStringUtils.isEmpty(phoneNum) || ZKStringUtils.isEmpty(companyId)) {
            return null;
        }
        return this.dao.getByPhoneNum(ZKSysOrgUser.sqlHelper().getTableName(), ZKSysOrgUser.sqlHelper().getTableAlias(),
            ZKSysOrgUser.sqlHelper().getBlockSqlCols(), companyId, phoneNum);
    }

    public ZKSysOrgUser getByJobNum(String companyId, String jobNum) {
        if (ZKStringUtils.isEmpty(jobNum) || ZKStringUtils.isEmpty(companyId)) {
            return null;
        }
        return this.dao.getByJobNum(ZKSysOrgUser.sqlHelper().getTableName(), ZKSysOrgUser.sqlHelper().getTableAlias(),
            ZKSysOrgUser.sqlHelper().getBlockSqlCols(), companyId, jobNum);
    }

    // 新用户时，使用系统默认密码
    @Transactional(readOnly = false)
    public int saveBySysPwd(ZKSysOrgUser user) {
        if (user.isNewRecord()) {
            user.setPassword(ZKEnvironmentUtils.getString("zk.sys.user.default.sys.pwd", "123456"));
        }
        return this.save(user);
    }

    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysOrgUser user) {
        // 判断公司是否存及公司状态，存在，初始部门公司值
        ZKSysOrgCompany company = this.sysOrgCompanyService.get(new ZKSysOrgCompany(user.getCompanyId()));
        if (company == null) {
            log.error("[^_^:20220425-1014-001] 公司[{}-{}]不存在;", user.getCompanyId(), user.getCompanyCode());
            throw ZKCodeException.as("zk.sys.010003", "公司不存在");
        } else {
            if (company.getStatus() == null || company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                log.error("[^_^:20220425-1014-001] 公司[{}-{}]状态异常，请联系管理员;", company.getPkId(), company.getCode());
                throw ZKCodeException.as("zk.sys.010004", "公司状态异常，请联系管理员");
            } else {
                // 初始化公司值
                user.setGroupCode(company.getGroupCode());
                user.setCompanyCode(company.getCode());
            }
        }
        if (!ZKStringUtils.isEmpty(user.getDeptId())) {
            // 判断部门是否存在及部门状态； zk.sys.010007=部门状态异常，请联系管理员
            ZKSysOrgDept dept = this.sysOrgDeptService.get(new ZKSysOrgDept(user.getDeptId()));
            if (dept == null) {
                log.error("[^_^:20220425-1014-001] 部门[{}-{}]不存在;", user.getDeptId(), user.getDeptCode());
                throw ZKCodeException.as("zk.sys.010006", "部门不存在");
            } else {
                if (dept.getStatus().intValue() != ZKSysOrgDept.KeyStatus.normal) {
                    log.error("[^_^:20220425-1014-002] 部门[{}-{}]状态异常，请联系管理员;", dept.getPkId(), dept.getCode());
                    throw ZKCodeException.as("zk.sys.010007", "部门状态异常，请联系管理员");
                } else {
                    // 初始化部门值
                    user.setDeptId(dept.getPkId());
                    user.setDeptCode(dept.getCode());
                }
            }
        } else {
            user.setDeptId(null);
            user.setDeptCode(null);
        }

        // 判断是否为新增？新增时，判断部门代码是否唯一
        if (user.isNewRecord()) {
            // 根据公司及账号代码查询用户是否存在
            ZKSysOrgUser olduser = this.getByAccount(company.getPkId(), user.getAccount());
            if (olduser != null) {
                log.error("[>_<:20220425-1015-001] 公司[{}]下账号[{}]已存在；", company.getCode(), olduser.getAccount());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.010008", olduser.getAccount()));
                throw ZKCodeException.asDataValidator(validatorMsg);
            }
            // 判断手机号唯一
            olduser = this.getByPhoneNum(company.getPkId(), user.getPhoneNum());
            if (olduser != null) {
                log.error("[>_<:20220425-1015-002] 公司[{}]下手机号[{}]已存在；", company.getCode(), olduser.getPhoneNum());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("phoneNum", ZKMsgUtils.getMessage("zk.sys.010009", olduser.getPhoneNum()));
                throw ZKCodeException.asDataValidator(validatorMsg);
            }
            // 判断邮箱唯一
            olduser = this.getByMail(company.getPkId(), user.getMail());
            if (olduser != null) {
                log.error("[>_<:20220425-1015-003] 公司[{}]下邮箱[{}]已存在；", company.getCode(), olduser.getMail());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("mail", ZKMsgUtils.getMessage("zk.sys.010010", olduser.getMail()));
                throw ZKCodeException.asDataValidator(validatorMsg);
            }
            // 判断工号是否唯一
            olduser = this.getByJobNum(company.getPkId(), user.getJobNum());
            if (olduser != null) {
                log.error("[>_<:20220503-1542-001] 公司[{}]下工号[{}]已存在；", company.getCode(), olduser.getJobNum());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("jobNum", ZKMsgUtils.getMessage("zk.sys.010012", olduser.getJobNum()));
                throw ZKCodeException.asDataValidator(validatorMsg);
            }
            // 密码加密
            user.setPassword(this.encryptionPwd(user.getPassword()));
            user.setPwdStatus(ZKSysOrgUser.KeyPwdStatus.systemPwd);
        }
        ZKUserCacheUtils.cleanUser(user);
        return super.save(user);
    }

    /**
     * 修改密码
     *
     * @Title: updatePwd
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 11:25:21 AM
     * @param userId
     *            修改的目标用户
     * @param pwd
     *            未加密的密码
     * @param pwdStatus
     *            密码状态
     * @return int
     */
    @Transactional(readOnly = false)
    public int updatePwd(String userId, String pwd, int pwdStatus) {
        return this.dao.updatePwd(ZKSysOrgUser.sqlHelper().getTableName(), userId, this.encryptionPwd(pwd), pwdStatus,
            ZKDateUtils.getToday());
    }

    /**
     * Md5 加密
     *
     * @Title: encryptionPwd
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 25, 2022 10:58:46 AM
     * @param pwd
     * @return
     * @return String
     */
    public String encryptionPwd(String pwd) {
        return ZKEncodingUtils.encodeHex(ZKEncryptUtils.md5Encode(pwd.getBytes()));
    }

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysOrgUser user) {
        sysAuthUserRoleService.diskDelByUserId(user.getPkId());
        sysAuthUserService.diskDelByUserId(user.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAuth(user);
        return super.del(user);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysOrgUser user) {
        sysAuthUserRoleService.diskDelByUserId(user.getPkId());
        sysAuthUserService.diskDelByUserId(user.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAuth(user);
        return super.diskDel(user);
    }

}