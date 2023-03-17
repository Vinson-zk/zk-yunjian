/**
 * 
 */
package com.zk.file.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseTreeService;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.file.dao.ZKFileDirectoryDao;
import com.zk.file.entity.ZKFileDirectory;
import com.zk.sys.api.org.ZKSysOrgCompanyApi;
import com.zk.sys.entity.org.ZKSysOrgCompany;

/**
 * ZKFileDirectoryService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class  ZKFileDirectoryService extends ZKBaseTreeService<String, ZKFileDirectory, ZKFileDirectoryDao> {

    @Autowired
    private ZKSysOrgCompanyApi sysOrgCompanyApi;

	/**
	 * 查询详情，包含父节点
	 */
    public ZKFileDirectory getDetail(ZKFileDirectory fileDirectory) {
        return this.dao.getDetail(fileDirectory);
    }

    // 按 code 查询
    public ZKFileDirectory getByCode(String companyCode, String code){
        return this.dao.getByCode(companyCode, code);
    }

    @Override
    public int save(ZKFileDirectory fileDirectory){
        // 新增时，判断目录代码在公司下是否唯一
        if(fileDirectory.isNewRecord()){
            ZKFileDirectory old = this.getByCode(fileDirectory.getCompanyCode(), fileDirectory.getCode());
            if(old != null){
                if (old.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    log.error("[>_<:20220524-1510-002] zk.file.000001=目录代码[{}-{}]已存在；",
                            fileDirectory.getCompanyCode(), fileDirectory.getCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("code", ZKMsgUtils.getMessage("zk.file.000001", fileDirectory.getCode()));
                    throw ZKCodeException.asDataValidator(validatorMsg);
                }else{
                    // 如果数据只是做了逻辑删除，恢复原数据
                    fileDirectory.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                    fileDirectory.setPkId(old.getPkId());
                }
            }
            if(!ZKStringUtils.isEmpty(fileDirectory.getCompanyCode())) {
                // 公司代码不为空，填充公司信息
                ZKSysOrgCompany company = this.getCompanyByCode(fileDirectory.getCompanyCode());
                if(company == null) {
                    // 公司不存在
                    log.error("[^_^:20221010-1450-001] zk.file.000002=公司[{}]不存在；", fileDirectory.getCompanyCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("companyCode",
                            ZKMsgUtils.getMessage("zk.file.000002", fileDirectory.getCompanyCode()));
                    throw ZKCodeException.asDataValidator(validatorMsg);
                }else {
                    if (company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                        log.error("[>_<:20221010-1450-002] zk.file.000003=公司[{}]状态异常，请联系管理员；",
                                fileDirectory.getCompanyCode());
                        Map<String, String> validatorMsg = Maps.newHashMap();
                        validatorMsg.put("companyCode",
                                ZKMsgUtils.getMessage("zk.file.000003", fileDirectory.getCompanyCode()));
                        throw ZKCodeException.asDataValidator(validatorMsg);
                    }
                    else {
                        fileDirectory.setCompanyId(company.getPkId());
                        fileDirectory.setGroupCode(company.getGroupCode());
                    }
                }
            }
        }
        return super.save(fileDirectory);
    }

    public ZKSysOrgCompany getCompanyByCode(String companyCode) {
        ZKMsgRes res = sysOrgCompanyApi.getCompanyByCode(companyCode);
        if (res.isOk()) {
            ZKSysOrgCompany c = res.getDataByClass(ZKSysOrgCompany.class);
            return c;
        }
        log.error("[>_<:20220526-1351-001] 根据公司代码取公司详情失败:{}", res.toString());
        return null;
    }
}