/**
 * 
 */
package com.zk.sys.org.service;

import java.util.Map;

import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseService;
import com.zk.core.commons.ZKValidationGroup;
import com.zk.core.encrypt.utils.ZKEncryptUtils;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.framework.security.utils.ZKUserCacheUtils;
import com.zk.security.utils.ZKSecPrincipalUtils;
import com.zk.sys.auth.service.ZKSysAuthUserRoleService;
import com.zk.sys.auth.service.ZKSysAuthUserService;
import com.zk.sys.common.ZKSysConstants.ValidationPattern;
import com.zk.sys.org.dao.ZKSysOrgUserDao;
import com.zk.sys.org.entity.ZKSysOrgCompany;
import com.zk.sys.org.entity.ZKSysOrgDept;
import com.zk.sys.org.entity.ZKSysOrgUser;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog.ZKUserEditFlag;
import com.zk.sys.org.entity.ZKSysOrgUserEditLog.ZKUserEditType;
import com.zk.sys.utils.ZKSysUtils;

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

    @Autowired
    ZKSysOrgUserEditLogService sysOrgUserEditLogService;

    /********************************************************/
    /** 用户 的一些修改 ****/
    /********************************************************/

    /**
     * 个人操作用户 PersonalUser
     *
     * @Title: editUserSelf
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 23, 2024 3:47:54 PM
     * @param user
     * @param registerType
     *            新增用户时生效，不是新增时传入任意值; ZKUserEditFlag.Base 中的值
     * @param accountEditFlag
     *            新增用户时生效，不是新增时传入任意值; 账号的生成类型，ZKUserEditFlag.Account 中的值
     * @return int
     */
    @Transactional(readOnly = false)
    public int editUserSelf(ZKSysOrgUser user, int registerType, int accountEditFlag) {
        ZKSysOrgCompany company = this.sysOrgCompanyService.get(new ZKSysOrgCompany(user.getCompanyId()));
        return this.editUserSelf(user, registerType, company, accountEditFlag);
    }
    
    @Transactional(readOnly = false)
    public int editUserSelf(ZKSysOrgUser user, int registerType, ZKSysOrgCompany company, int accountEditFlag) {
        // 校验 一下账号、邮箱、手机号
        boolean isNewRecord = user.isNewRecord();
        if (isNewRecord && ZKUserEditFlag.Base.mail == registerType) {
            // 校验邮箱
            this.beanValidator(user, "mail", ZKValidationGroup.CustomModel.class);
        }
        if (isNewRecord && ZKUserEditFlag.Base.phoneNum == registerType) {
            // 校验手机
            this.beanValidator(user, "phoneNum", ZKValidationGroup.CustomModel.class);
        }
        // 判断公司是否存及公司状态、是否存在、初始化用户公司信息
        this.checkCompanyOnSaveUser(user, company);
        int count = this.save(user);
        if (count > 0) {
            if (isNewRecord) {
                // 用户注册方式
                this.sysOrgUserEditLogService.editBase(user.getPkId(), registerType);
                this.updatePwd(user.getPkId(), user.getPassword(), ZKUserEditFlag.Pwd.self);
                this.sysOrgUserEditLogService.editAccount(user.getPkId(), accountEditFlag);
            }
            else {
                // 用户自己修改
                this.sysOrgUserEditLogService.editBase(user.getPkId(), ZKUserEditFlag.Base.self);
            }
        }
        return count;
    }

    /**
     * 公司管理员操作用户
     * 
     * 新增时：会设置系统默认密码
     *
     * @Title: saveUserCompanyOpt
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 23, 2024 3:22:19 PM
     * @param user
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int editUserCompanyOpt(ZKSysOrgUser user) {
        ZKSysOrgCompany company = this.sysOrgCompanyService.get(new ZKSysOrgCompany(user.getCompanyId()));
        return this.editUserCompanyOpt(user, company);
    }

    // 核验公司是否可编辑用户
    @Transactional(readOnly = false)
    public int editUserCompanyOpt(ZKSysOrgUser user, ZKSysOrgCompany company) {
        // 校验 一下账号、邮箱、手机号
        this.beanValidator(user, ZKValidationGroup.CustomModel.class);
        // 判断公司是否存及公司状态、是否存在、初始化用户公司信息
        this.checkCompanyOnSaveUser(user, company);
        boolean isNewRecord = user.isNewRecord();
        int count = this.save(user);
        if (count > 0) {
            this.sysOrgUserEditLogService.editBase(user.getPkId(), ZKUserEditFlag.Base.company);
            if (isNewRecord) {
                // 新用户，设置系统默认密码
                this.updatePwd(user.getPkId(), ZKSysUtils.getUserDefaultPwd(), ZKUserEditFlag.Pwd.system);
            }
        }
        return count;
    }

    // 判断公司是否存及公司状态、是否存在、初始化用户公司信息
    public void checkCompanyOnSaveUser(ZKSysOrgUser user, ZKSysOrgCompany company) {
        if (company == null) {
            log.error("[^_^:20220425-1014-001] 公司[{}-{}]不存在;", user.getCompanyId(), user.getCompanyCode());
            throw ZKBusinessException.as("zk.sys.010003", "公司不存在");
        }
        else {
            if (company.getStatus() == null || company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                log.error("[^_^:20220425-1014-001] 公司[{}-{}]状态异常，请联系管理员;", company.getPkId(), company.getCode());
                throw ZKBusinessException.as("zk.sys.010004", "公司状态异常，请联系管理员");
            }
            else {
                // 初始化公司值
                user.setCompanyId(company.getPkId());
                user.setGroupCode(company.getGroupCode());
                user.setCompanyCode(company.getCode());
            }
        }
    }

    // 保存基础信息
    @Override
    @Transactional(readOnly = false)
    public int save(ZKSysOrgUser user) {
//        // 公司校验放在上一层方法；判断公司是否存及公司状态，存在，初始部门公司值
//        if (company == null) {
//            log.error("[^_^:20220425-1014-001] 公司[{}-{}]不存在;", user.getCompanyId(), user.getCompanyCode());
//            throw ZKBusinessException.as("zk.sys.010003", "公司不存在");
//        }
//        else {
//            if (company.getStatus() == null || company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
//                log.error("[^_^:20220425-1014-001] 公司[{}-{}]状态异常，请联系管理员;", company.getPkId(), company.getCode());
//                throw ZKBusinessException.as("zk.sys.010004", "公司状态异常，请联系管理员");
//            }
//            else {
//                // 初始化公司值
//                user.setCompanyId(company.getPkId());
//                user.setGroupCode(company.getGroupCode());
//                user.setCompanyCode(company.getCode());
//            }
//        }
        if (!ZKStringUtils.isEmpty(user.getDeptId())) {
            // 判断部门是否存在及部门状态； zk.sys.010007=部门状态异常，请联系管理员
            ZKSysOrgDept dept = this.sysOrgDeptService.get(new ZKSysOrgDept(user.getDeptId()));
            if (dept == null) {
                log.error("[^_^:20220425-1014-001] 部门[{}-{}]不存在;", user.getDeptId(), user.getDeptCode());
                throw ZKBusinessException.as("zk.sys.010006", "部门不存在");
            }
            else {
                if (dept.getStatus().intValue() != ZKSysOrgDept.KeyStatus.normal) {
                    log.error("[^_^:20220425-1014-002] 部门[{}-{}]状态异常，请联系管理员;", dept.getPkId(), dept.getCode());
                    throw ZKBusinessException.as("zk.sys.010007", "部门状态异常，请联系管理员");
                }
                else {
                    // 初始化部门值
                    user.setDeptId(dept.getPkId());
                    user.setDeptCode(dept.getCode());
                }
            }
        }
        else {
            user.setDeptId(null);
            user.setDeptCode(null);
        }

        // 判断是否为新增？新增时，判断部门代码是否唯一
        if (user.isNewRecord()) {
            // 根据公司及账号代码查询用户是否存在
            this.checkUniqueByAccount(user.getCompanyId(), user.getAccount(), null);
            // 判断手机号唯一
            this.checkUniqueByPhoneNum(user.getCompanyId(), user.getPhoneNum(), null);
            // 判断邮箱唯一
            this.checkUniqueByMail(user.getCompanyId(), user.getMail(), null);
            // 判断工号是否唯一
            this.checkUniqueByJobNum(user.getCompanyId(), user.getJobNum(), null);
        }
        else {
            // 不是新用户，清理缓存
            ZKUserCacheUtils.cleanUser(user);
        }
        return super.save(user);
    }

    // 修改账号
    @Transactional(readOnly = false)
    public int updateAccount(ZKSysOrgUser user, String account, int editFlag) {
        // 仅个人用户可以修改账号
        if (!ZKSysUtils.getPersonalUserTypeCode().equals(user.getUserTypeCode())) {
            // zk.sys.010028=只允许个人用户可修改账号
            log.error("[^_^:20220425-1015-002]  zk.sys.010028=只允许个人用户可修改账号, userId:{};", user.getPkId());
            throw ZKBusinessException.as("zk.sys.010028");
        }
        // 用户只能修改掉系统生成的账号，然后不能再修改
        // 取出账号的修改日志
        ZKSysOrgUserEditLog accountEditLog = this.sysOrgUserEditLogService.getLatestEditLog(user.getPkId(),
                ZKUserEditType.account);
        if (accountEditLog == null || accountEditLog.getEditFlag().intValue() != ZKUserEditFlag.Account.system) {
            // 账号上次修改不是系统修改，不允许修改账号
            log.error("[^_^:20220425-1015-003] zk.sys.010029=用户账号非系统生成，不允许修改, userId:{};", user.getPkId());
            throw ZKBusinessException.as("zk.sys.010029");
        }
        // 判断账号唯一
        this.checkUniqueByAccount(user.getCompanyId(), account, user.getPkId());
        int count = this.dao.updateAccount(ZKSysOrgUser.sqlHelper().getTableName(), user.getPkId(), account,
                ZKDateUtils.getToday());
        if (count > 0) {
            this.sysOrgUserEditLogService.editAccount(user.getPkId(), editFlag);
        }
        return count;
    }

    // 修改手机号
    @Transactional(readOnly = false)
    public int updatePhoneNum(ZKSysOrgUser user, String phoneNum, int editFlag) {
        // 判断手机号唯一
        this.checkUniqueByPhoneNum(user.getCompanyId(), phoneNum, user.getPkId());
        int count = this.dao.updatePhoneNum(ZKSysOrgUser.sqlHelper().getTableName(), user.getPkId(), phoneNum,
                ZKDateUtils.getToday());
        if (count > 0) {
            this.sysOrgUserEditLogService.editPhone(user.getPkId(), editFlag);
        }
        return count;
    }

    // 修改邮箱
    @Transactional(readOnly = false)
    public int updateMail(ZKSysOrgUser user, String mail, int editFlag) {
        // 判断邮箱唯一
        this.checkUniqueByMail(user.getCompanyId(), mail, user.getPkId());
        int count = this.dao.updateMail(ZKSysOrgUser.sqlHelper().getTableName(), user.getPkId(), mail,
                ZKDateUtils.getToday());
        if (count > 0) {
            this.sysOrgUserEditLogService.editMail(user.getPkId(), editFlag);
        }
        return count;
    }

    // 修改状态
    @Update(" UPDATE ${tn} SET c_status = #{status}, c_update_date = #{updateDate} WHERE c_pk_id = #{pkId} ")
    public int updateStatus(String pkId, int status, int editFlag) {
        int count = this.dao.updateStatus(ZKSysOrgUser.sqlHelper().getTableName(), pkId, status, ZKDateUtils.getToday());
        if(count > 0) {
            sysOrgUserEditLogService.editStatus(pkId, ZKUserEditFlag.Status.company);
        }
        return count;
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
    public int updatePwd(String userId, String pwd, int editFlag) {
        int count = this.dao.updatePwd(ZKSysOrgUser.sqlHelper().getTableName(), userId, this.encryptionPwd(pwd),
                ZKDateUtils.getToday());
        if (count > 0) {
            sysOrgUserEditLogService.editPwd(userId, editFlag);
        }
        return count;
    }

    /********************************************************/
    /** 用户 的一些查询 ****/
    /********************************************************/

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
            user.setCompany(this.sysOrgCompanyService.getDetail(new ZKSysOrgCompany(user.getCompanyId())));
        }
        return user;
    }

    public ZKSysOrgUser getDetail(String userId) {
        ZKSysOrgUser user = super.get(new ZKSysOrgUser(userId));
        return this.getDetail(user);
    }

    // 根据公司代码，输入内容，从账号、手机、邮箱 查找用户
    public ZKSysOrgUser getUserSmartByCompanyCode(String companyCode, String inputValue) {
        if (ZKStringUtils.isEmpty(companyCode) || ZKStringUtils.isEmpty(inputValue)) {
            log.error("[>_<:20240717-0114-001] 输入用户信息为空，无法查找用户；companyId：{}，inputValue：{}", companyCode, inputValue);
            return null;
        }
        ZKSysOrgCompany company = this.sysOrgCompanyService.getByCode(companyCode);
        if (company == null) {
            log.error("[>_<:20240717-0114-001] 公司[{}]不存在;", companyCode);
            throw ZKBusinessException.as("zk.sys.010003", "公司不存在");
        }
        return this.getUserSmart(company.getPkId(), inputValue);
    }

    public ZKSysOrgUser getUserSmartByCompanyId(String companyId, String inputValue) {
        if (ZKStringUtils.isEmpty(companyId) || ZKStringUtils.isEmpty(inputValue)) {
            log.error("[>_<:20240717-0113-001] 输入用户信息为空，无法查找用户；companyId：{}，inputValue：{}", companyId, inputValue);
            return null;
        }
        return this.getUserSmart(companyId, inputValue);
    }

    protected ZKSysOrgUser getUserSmart(String companyId, String inputValue) {
        if (ValidationPattern.patternAccount.matcher(inputValue).matches()) {
            // 根据用户账号取用户
            log.info("[^_^:20240717-0113-002] 输入用户信息为账号，根据账号查找用户");
            return this.dao.getByAccount(ZKSysOrgUser.sqlHelper().getTableName(),
                    ZKSysOrgUser.sqlHelper().getTableAlias(), ZKSysOrgUser.sqlHelper().getBlockSqlCols(), companyId,
                    inputValue);
        }
        else if (ValidationPattern.patternPhoneNum.matcher(inputValue).matches()) {
            // 根据手机号取用户
            log.info("[^_^:20240717-0113-003] 输入用户信息为手机号，根据手机号查找用户");
            return this.dao.getByPhoneNum(ZKSysOrgUser.sqlHelper().getTableName(),
                    ZKSysOrgUser.sqlHelper().getTableAlias(), ZKSysOrgUser.sqlHelper().getBlockSqlCols(), companyId,
                    inputValue);
        }
        else if (ValidationPattern.patternMail.matcher(inputValue).matches()) {
            // 根据邮箱取用户
            log.info("[^_^:20240717-0113-003] 输入用户信息为邮箱，根据邮箱查找用户");
            return this.dao.getByMail(ZKSysOrgUser.sqlHelper().getTableName(), ZKSysOrgUser.sqlHelper().getTableAlias(),
                    ZKSysOrgUser.sqlHelper().getBlockSqlCols(), companyId, inputValue);
        }
        return null;
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

    /**
     * 根据工号取用户；公司下工号唯一
     *
     * @Title: getByJobNum
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 20, 2024 8:55:57 PM
     * @param companyId
     * @param jobNum
     * @return
     * @return ZKSysOrgUser
     */
    public ZKSysOrgUser getByJobNum(String companyId, String jobNum) {
        if (ZKStringUtils.isEmpty(jobNum) || ZKStringUtils.isEmpty(companyId)) {
            return null;
        }
        return this.dao.getByJobNum(ZKSysOrgUser.sqlHelper().getTableName(), ZKSysOrgUser.sqlHelper().getTableAlias(),
            ZKSysOrgUser.sqlHelper().getBlockSqlCols(), companyId, jobNum);
    }

    /**
     * 验证公司下，账号是否唯一；
     *
     * @Title: checkUniqueByAccount
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 20, 2024 9:01:04 PM
     * @param companyId
     * @param account
     * @param filterId
     *            过虑的用户ID，即找到用户的ID为些ID时，不认为用户已存在；
     * @return
     * @return boolean
     */
    public boolean checkUniqueByAccount(String companyId, String account, String filterId) {
        ZKSysOrgUser user = this.getByAccount(companyId, account);
        if (user != null && !user.getPkId().equals(filterId)) {
            log.error("[>_<:20220425-1015-001] 公司[{}]下账号[{}]已存在；", user.getCompanyCode(), user.getAccount());
            Map<String, String> validatorMsg = Maps.newHashMap();
            validatorMsg.put("code", ZKMsgUtils.getMessage("zk.sys.010008", user.getAccount()));
            throw ZKValidatorException.as(validatorMsg);
        }
        return true;
    }

    /**
     * 验证公司下，手机号是否唯一；
     *
     * @Title: checkUniqueByPhoneNum
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 20, 2024 9:02:09 PM
     * @param companyId
     * @param phoneNum
     * @param filterId
     *            过虑的用户ID，即找到用户的ID为些ID时，不认为用户已存在；
     * @return
     * @return boolean
     */
    public boolean checkUniqueByPhoneNum(String companyId, String phoneNum, String filterId) {
        ZKSysOrgUser user = this.getByPhoneNum(companyId, phoneNum);
        if (user != null && !user.getPkId().equals(filterId)) {
            log.error("[>_<:20220425-1015-002] 公司[{}]下手机号[{}]已存在；", user.getCompanyCode(), user.getPhoneNum());
            Map<String, String> validatorMsg = Maps.newHashMap();
            validatorMsg.put("phoneNum", ZKMsgUtils.getMessage("zk.sys.010009", user.getPhoneNum()));
            throw ZKValidatorException.as(validatorMsg);
        }
        return true;
    }

    /**
     * 验证公司下，邮箱是否唯一；
     *
     * @Title: checkUniqueByMail
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 20, 2024 9:02:12 PM
     * @param companyId
     * @param mail
     * @param filterId
     *            过虑的用户ID，即找到用户的ID为些ID时，不认为用户已存在；
     * @return
     * @return boolean
     */
    public boolean checkUniqueByMail(String companyId, String mail, String filterId) {
        ZKSysOrgUser user = this.getByMail(companyId, mail);
        if (user != null && !user.getPkId().equals(filterId)) {
            log.error("[>_<:20220425-1015-003] 公司[{}]下邮箱[{}]已存在；", user.getCompanyCode(), user.getMail());
            Map<String, String> validatorMsg = Maps.newHashMap();
            validatorMsg.put("mail", ZKMsgUtils.getMessage("zk.sys.010010", user.getMail()));
            throw ZKValidatorException.as(validatorMsg);
        }
        return true;
    }

    /**
     * 验证公司下，工号是否唯一；
     *
     * @Title: checkUniqueByJobNum
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 20, 2024 9:02:15 PM
     * @param companyId
     * @param jobNum
     * @param filterId
     *            过虑的用户ID，即找到用户的ID为些ID时，不认为用户已存在；
     * @return
     * @return boolean
     */
    public boolean checkUniqueByJobNum(String companyId, String jobNum, String filterId) {
        ZKSysOrgUser user = this.getByJobNum(companyId, jobNum);
        if (user != null && !user.getPkId().equals(filterId)) {
            log.error("[>_<:20220503-1542-001] 公司[{}]下工号[{}]已存在；", user.getCompanyCode(), user.getJobNum());
            Map<String, String> validatorMsg = Maps.newHashMap();
            validatorMsg.put("jobNum", ZKMsgUtils.getMessage("zk.sys.010012", user.getJobNum()));
            throw ZKValidatorException.as(validatorMsg);
        }
        return true;
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

    // 验证用户密码是否正确，true-正确；false-不正确
    public boolean checkUserPassword(ZKSysOrgUser loginUser, String pwd) {
        pwd = this.encryptionPwd(pwd);
        if (pwd.equals(loginUser.getPassword())) {
            return true;
        }
        return false;
    }

    /********************************************************/
    /** 用户 的删除 ****/
    /********************************************************/

    @Override
    @Transactional(readOnly = false)
    public int del(ZKSysOrgUser user) {
        sysAuthUserRoleService.diskDelByUserId(user.getPkId());
        sysAuthUserService.diskDelByUserId(user.getPkId());
        sysOrgUserEditLogService.delByUserId(user.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAuth(user);
        return super.del(user);
    }

    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKSysOrgUser user) {
        sysAuthUserRoleService.diskDelByUserId(user.getPkId());
        sysAuthUserService.diskDelByUserId(user.getPkId());
        sysOrgUserEditLogService.diskDelByUserId(user.getPkId());
        // 清空权限缓存
        ZKUserCacheUtils.cleanAuth(user);
        return super.diskDel(user);
    }

    // 根据公司ID做逻辑删除
    public int delByCompanyId(String companyId) {
        return this.dao.delByCompanyId(ZKSysOrgUser.sqlHelper().getTableName(), companyId,
                ZKBaseEntity.DEL_FLAG.delete, ZKSecPrincipalUtils.getSecPrincipalService().getUserId(),
                ZKDateUtils.getToday());
    }

    // 根据公司ID做物理删除
    public int diskDelByCompanyId(String companyId) {
        return this.dao.diskDelByCompanyId(ZKSysOrgUser.sqlHelper().getTableName(), companyId);
    }

}