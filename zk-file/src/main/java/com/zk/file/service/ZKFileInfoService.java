/**
 * 
 */
package com.zk.file.service;
 
import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseTreeService;
import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.file.dao.ZKFileInfoDao;
import com.zk.file.entity.ZKFileInfo;
import com.zk.file.entity.ZKFileInfo.ValueKey;
import com.zk.sys.api.org.ZKSysOrgCompanyApi;
import com.zk.sys.entity.org.ZKSysOrgCompany;

/**
 * ZKFileInfoService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKFileInfoService extends ZKBaseTreeService<String, ZKFileInfo, ZKFileInfoDao> {

    @Autowired
    private ZKSysOrgCompanyApi sysOrgCompanyApi;

    /**
     * 查询详情，包含父节点
     */
    public ZKFileInfo getDetail(ZKFileInfo fileInfo) {
        return this.dao.getDetail(fileInfo);
    }

    // 按 code 查询
    public ZKFileInfo getByCode(String companyCode, String code) {
        return this.dao.getByCode(companyCode, code);
    }

    @Override
    public int save(ZKFileInfo fileInfo) {
        // 新增时，判断目录代码在公司下是否唯一
        if (fileInfo.isNewRecord()) {
            ZKFileInfo old = this.getByCode(fileInfo.getCompanyCode(), fileInfo.getCode());
            if (old != null) {
                if (old.getDelFlag().intValue() == ZKBaseEntity.DEL_FLAG.normal) {
                    String errMsg = "";
                    if (old.getType().intValue() == ValueKey.Type.document) {
                        log.error("[>_<:20220524-1510-002] zk.file.000001=目录代码[{}-{}]已存在；", old.getCompanyCode(),
                                old.getCode());
                        errMsg = ZKMsgUtils.getMessage("zk.file.000001", old.getCode());
                    }
                    else {
                        log.error("[>_<:20230307-0031-001] zk.file.000004=文件代码[{}-{}]已存在", old.getCompanyCode(),
                                old.getCode());
                        errMsg = ZKMsgUtils.getMessage("zk.file.000004", old.getCode());
                    }
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("code", errMsg);
                    throw ZKCodeException.asDataValidator(validatorMsg);
                }
                else {
                    // 如果数据只是做了逻辑删除，恢复原数据
                    fileInfo.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);
                    fileInfo.setPkId(old.getPkId());
                }
            }
            if (!ZKStringUtils.isEmpty(fileInfo.getCompanyCode())) {
                // 公司代码不为空，填充公司信息
                ZKSysOrgCompany company = this.getCompanyByCode(fileInfo.getCompanyCode());
                if (company == null) {
                    // 公司不存在
                    log.error("[^_^:20221010-1450-001] zk.file.000002=公司[{}]不存在；", fileInfo.getCompanyCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("companyCode",
                            ZKMsgUtils.getMessage("zk.file.000002", fileInfo.getCompanyCode()));
                    throw ZKCodeException.asDataValidator(validatorMsg);
                }
                else {
                    if (company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                        log.error("[>_<:20221010-1450-002] zk.file.000003=公司[{}]状态异常，请联系管理员；",
                                fileInfo.getCompanyCode());
                        Map<String, String> validatorMsg = Maps.newHashMap();
                        validatorMsg.put("companyCode",
                                ZKMsgUtils.getMessage("zk.file.000003", fileInfo.getCompanyCode()));
                        throw ZKCodeException.asDataValidator(validatorMsg);
                    }
                    else {
                        fileInfo.setCompanyId(company.getPkId());
                        fileInfo.setGroupCode(company.getGroupCode());
                    }
                }
            }

            fileInfo.initAttr();
            // sort 排序
            fileInfo.setSort(this.getMaxSort(fileInfo.getCompanyCode(), fileInfo.getParentId()) + 1);
        }
        return super.save(fileInfo);
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

    // 查询指定公司下，指定目录下，最大的排序值
    public int getMaxSort(String companyCode, String parentId) {
        Integer maxSort = null;
        if (!ZKStringUtils.isEmpty(companyCode)) {
            maxSort = this.dao.selectMaxSort(companyCode, parentId);
        }
        return maxSort == null ? 0 : maxSort;

    }

    // 文件处理 ------------------------------------------------------------------------
    public static final String rootPath = "/Users/bs/bs_temp/zk-yunjian/fileUpload";

    @Autowired
    ZKFileTransfer zkFileTransfer;

    public File getFile(ZKFileInfo zkFileInfo) {
        return null;
    }

    public File transferFile(ZKFileInfo zkFileInfo, MultipartFile multipartFile) {
        zkFileTransfer.transferFile(path, isNewName, isOverride, multipartFile);
        return null;
    }
	
	
}