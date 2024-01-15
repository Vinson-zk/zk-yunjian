/**
 * 
 */
package com.zk.file.service;
 
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.zk.base.entity.ZKBaseEntity;
import com.zk.base.service.ZKBaseTreeService;
import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKMsgRes;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.commons.data.ZKPage.Max_value;
import com.zk.core.exception.ZKCodeException;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.core.utils.ZKStreamUtils;
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

    @Autowired
    ZKFileDisposeService fileDisposeService;

    // 根据公司代码取公司信息
    public ZKSysOrgCompany getCompanyByCode(String companyCode) {

        ZKMsgRes res = sysOrgCompanyApi.getCompanyByCode(companyCode);
        if (res.isOk()) {
            ZKSysOrgCompany c = res.getDataByClass(ZKSysOrgCompany.class);
            return c;
        }
        log.error("[>_<:20220526-1351-001] 根据公司代码取公司详情失败:{}", res.toString());
        return null;
    }

    // 文件或目录记录处理 ================================================================================================
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

    // 按 name 查询
    public ZKFileInfo getByName(String companyCode, String name) {
        return this.dao.getByName(companyCode, name);
    }

    // 保存目录或文件记录
    @Override
    @Transactional(readOnly = false)
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
                        log.error("[>_<:20230307-0031-001] zk.file.000002=文件代码[{}-{}]已存在", old.getCompanyCode(),
                                old.getCode());
                        errMsg = ZKMsgUtils.getMessage("zk.file.000002", old.getCode());
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
                    log.error("[^_^:20221010-1450-001] zk.file.010002=公司[{}]不存在；", fileInfo.getCompanyCode());
                    Map<String, String> validatorMsg = Maps.newHashMap();
                    validatorMsg.put("companyCode",
                            ZKMsgUtils.getMessage("zk.file.010001", fileInfo.getCompanyCode()));
                    throw ZKCodeException.asDataValidator(validatorMsg);
                }
                else {
                    if (company.getStatus().intValue() != ZKSysOrgCompany.KeyStatus.normal) {
                        log.error("[>_<:20221010-1450-002] zk.file.010002=公司[{}]状态异常，请联系管理员；",
                                fileInfo.getCompanyCode());
                        Map<String, String> validatorMsg = Maps.newHashMap();
                        validatorMsg.put("companyCode",
                                ZKMsgUtils.getMessage("zk.file.010002", fileInfo.getCompanyCode()));
                        throw ZKCodeException.asDataValidator(validatorMsg);
                    }
                    else {
                        fileInfo.setCompanyId(company.getPkId());
                        fileInfo.setGroupCode(company.getGroupCode());
                    }
                }
            }
            // 初始化默认值
            fileInfo.afterAttrSet();
            // sort 排序
            if (fileInfo.getSort() == null) {
                fileInfo.setSort(this.getMaxSort(fileInfo.getCompanyCode(), fileInfo.getParentId()) + 1);
            }
        }
        // 目录新增/修改时都校验名字是否唯一
        if (fileInfo.getType().intValue() == ValueKey.Type.document) {
            ZKFileInfo old = this.getByName(fileInfo.getCompanyCode(), fileInfo.getName());
            if (old != null && old.getType().intValue() == ValueKey.Type.document
                    && old.getPkId().equals(fileInfo.getPkId())) {
                // 目录名已存在

                log.error("[>_<:20230307-0031-001] zk.file.000008=目录{}-{}已存在", fileInfo.getCompanyCode(),
                        fileInfo.getName());
                Map<String, String> validatorMsg = Maps.newHashMap();
                validatorMsg.put("code", ZKMsgUtils.getMessage("zk.file.000008", fileInfo.getName()));
                throw ZKCodeException.asDataValidator(validatorMsg);
            }
        }
        return super.save(fileInfo);
    }

    /**
     * 初始化上级目录和公司信息；
     *
     * @Title: initParentInfo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 26, 2023 2:05:28 PM
     * @param companyCode
     * @param parentCode
     * @param zkFileInfo
     * @return void
     */
    public void initParentAndCompanyInfo(String companyCode, String parentCode, ZKFileInfo zkFileInfo) {
        // 处理文件的上级目录
        if (ZKStringUtils.isEmpty(parentCode)) {
            // 无上级目录
            zkFileInfo.setParentCode(null);
            zkFileInfo.setParentId(null);

            ZKSysOrgCompany c = this.getCompanyByCode(companyCode);

            // 设置公司信息
            zkFileInfo.setGroupCode(c.getGroupCode());
            zkFileInfo.setCompanyCode(c.getCode());
            zkFileInfo.setCompanyId(c.getPkId());

        }
        else {
            ZKFileInfo parentFileInfo = this.getByCode(companyCode, parentCode);
            if (parentFileInfo == null) {
                log.error("[>_<:20230526-0031-001] zk.file.000006=文件[{}-{}]不存在", companyCode, parentCode);
                throw ZKCodeException.as("zk.file.000006", null, null, parentCode);
            }
            zkFileInfo.setParentCode(parentCode);
            zkFileInfo.setParentId(parentFileInfo.getPkId());
            zkFileInfo.setParent(parentFileInfo);

            // 设置公司信息
            zkFileInfo.setGroupCode(parentFileInfo.getGroupCode());
            zkFileInfo.setCompanyCode(parentFileInfo.getCompanyCode());
            zkFileInfo.setCompanyId(parentFileInfo.getCompanyId());
        }
    }

    // 查询指定公司下，指定目录下，最大的排序值
    public int getMaxSort(String companyCode, String parentId) {
        Integer maxSort = null;
        if (!ZKStringUtils.isEmpty(companyCode)) {
            maxSort = this.dao.selectMaxSort(companyCode, parentId);
        }
        return maxSort == null ? 0 : maxSort;

    }

    /**
     * 取文件或目录信息的全路径; 是数据库的记录的目录路径；
     *
     * @Title: getAllPath
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 27, 2023 9:49:19 AM
     * @param zkFileInfo
     * @return
     * @return String
     */
    protected void getAllPath(StringBuffer sb, ZKFileInfo detail) {
//        ZKFileInfo detail = this.getDetail(zkFileInfo);
        if (detail == null) {
            log.error("[>_<:20230327-0950-001] 文件或目录不存在");
        }
        if (detail.getParent() != null) {
            this.getAllPath(sb, detail.getParent());
            sb.append(File.separator);
        }
        sb.append(detail.getCode());
        sb.append(File.separator);
    }

    /**
     * 删除数据
     * 
     * @param entity
     */
    @Transactional(readOnly = false)
    public int del(ZKFileInfo entity) {
        int count = 0;
        entity.setDelFlag(ZKBaseEntity.DEL_FLAG.delete);
        entity.preUpdate();
        count = dao.del(entity);

        // 级联删除
        ZKPage<ZKFileInfo> page = ZKPage.asPage();
        page.setPageNo(-1);
        page.setPageSize(Max_value.size);
        ZKFileInfo params = new ZKFileInfo();
        params.setParentId(entity.getPkId());
        params.setDelFlag(ZKBaseEntity.DEL_FLAG.normal);

        while (page.hasNextPage()) {
            page.setPageNo(page.getPageNo() + 1);
            page = this.findPage(page, params);
            for (ZKFileInfo item : page.getResult()) {
                count += this.del(item);
            }
        }

        return count;
    }

    /**
     * 物理删除数据
     * 
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public int diskDel(ZKFileInfo entity) {
        int count = 0;
        count = dao.diskDel(entity);
        // 级联删除
        ZKPage<ZKFileInfo> page = ZKPage.asPage();
        page.setPageSize(Max_value.size);
        ZKFileInfo params = new ZKFileInfo();
        params.setParentId(entity.getParentId());
        page = this.findPage(page, entity);
        do {
            for (ZKFileInfo item : page.getResult()) {
                count += this.diskDel(item);
            }
        } while (page.hasNextPage());
        return count;
    }

    // 文件处理 ------------------------------------------------------------------------
     
    /**
     * MultipartFile 上传文件
     *
     * @Title: uploadFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 28, 2023 4:21:48 PM
     * @param companyCode:
     *            公司代码
     * @param parentCode:
     *            父目录代码；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     * @param saveGroupCode:
     *            文件分组代码，全表唯一，自动生成，UUID；以便附件分组
     * @param name:
     *            文件名称
     * @param code:
     *            文件代码；公司下唯一;
     * @param stauts:
     *            状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看]
     * @param securityType:
     *            文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]
     * @param actionScope:
     *            文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点
     * @param sort:
     *            排序
     * @param multipartFile
     * @return
     * @return ZKFileInfo
     */
    @Transactional(readOnly = false)
    public ZKFileInfo uploadFile(String companyCode, String parentCode, String saveGroupCode, String name,
            String code, Integer stauts, Integer securityType, Integer actionScope, Integer sort, MultipartFile multipartFile) {
        if (multipartFile == null) {
            log.error("[>_<:20231224-1112-001] zk.file.000009=上传文件失败，未发现上传数据。");
            throw ZKCodeException.as("zk.file.000009", null, null, parentCode);
        }
        ZKFileInfo zkFileInfo = new ZKFileInfo();

//        * @param companyCode: 公司代码  
//        * @param parentCode: 父目录代码；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点； 
//        groupCode: 集团代码    
//        companyId: 公司ID    
//        parentId: 父目录ID；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；  
        this.initParentAndCompanyInfo(companyCode, parentCode, zkFileInfo);
//      * @param saveGroupCode: 文件分组代码，全表唯一，自动生成，UUID；以便附件分组   
        zkFileInfo.setSaveGroupCode(saveGroupCode);
//        * @param name: 文件名称  
        zkFileInfo.setName(name);
//        * @param code: 文件代码；公司下唯一;  
        zkFileInfo.setCode(code);
//        * @param stauts: 状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看]  
        zkFileInfo.setStauts(stauts);
//        * @param securityType: 文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取] 
        zkFileInfo.setSecurityType(securityType);
//        * @param actionScope: 文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点 
        zkFileInfo.setActionScope(actionScope);
//        * @param sort: 排序 
        zkFileInfo.setSort(sort);

        this.transferFile(zkFileInfo, multipartFile);
        return zkFileInfo;
    }

    /**
     * 上传保存一个文件
     *
     * @Title: transferFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 26, 2023 11:42:29 AM
     * @param zkFileInfo
     *            文件信息已设置好目录等相关信息
     * @param multipartFile
     * @return
     * @return File
     */
    protected ZKFileInfo transferFile(ZKFileInfo zkFileInfo, MultipartFile multipartFile) {
//        type: 数据类型；0-文件；1-目录；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点； 
        zkFileInfo.setType(ValueKey.Type.file);
//        saveUuid: 文件保存标识，全表唯一标识，UUID，与ID 作用重复，生成一个保存以备用   
//        originalName: 文件原始名称      
        zkFileInfo.setOriginalName(multipartFile.getOriginalFilename());
//        contentType: 文件类型   
        zkFileInfo.setContentType(multipartFile.getContentType());
//        size: 文件大小；单位 b     
        zkFileInfo.setSize(multipartFile.getSize());
//        uri: 访问文件的相对地址；目录时设置为 

        /*
         * 1. type: 类型为空设置类型默认为文件；
         * 2. name: 类型为文件时文件名称为空设置默认文件名称UUID
         * 3. code: 类型为文件时文件代码为空设置默认文件名称UUID；  
         * 4. stauts: 默认设置为 0-上传； 
         * 5. saveGroupCode: 保存分组代码为空自动填写UUID；
         * 6. saveUuid: 保存标识为空自动填写UUID；  
         * 7. securityType: 文件权限类型：权限类型为空默认填写权限类型为开放；
         * 8. actionScope: 作用域为空默认填写为普通；
         * 9. size: 为空默认 0；
         * 10. uri: 访问文件的相对地址；为空，默认填写 /
         */
        zkFileInfo.afterAttrSet();

        StringBuffer pathSb = new StringBuffer();
        pathSb.append(zkFileInfo.getCompanyCode());
        if (ZKStringUtils.isEmpty(zkFileInfo.getParentCode())) {
            pathSb.append(File.separator);
        }
        else {
            this.getAllPath(pathSb, zkFileInfo.getParent());
        }
        zkFileInfo.setUri(pathSb.toString() + zkFileInfo.getName());
        
        this.save(zkFileInfo);
        String fileAbsolutePath = this.fileDisposeService.doDisposeFile(pathSb.toString(), zkFileInfo.getName(),
                multipartFile);
        log.info("[^_^:20231226-2345-001] 文件上传成功：{}", fileAbsolutePath);
        return zkFileInfo;
    }
    
    // 批量上传
    @Transactional(readOnly = false)
    public List<ZKFileInfo> uploadFileBatch(ZKFileInfo fileInfo, MultipartFile... multipartFiles) {
        List<ZKFileInfo> fis = new ArrayList<>();

        if (multipartFiles != null) {
            ZKFileInfo fi = null;
            for (MultipartFile mf : multipartFiles) {
                fi = this.transferFile(fileInfo, mf);
                fis.add(fi);
            }
        }
        return fis;
    }

    // 批量上传
    @Transactional(readOnly = false)
    public List<ZKFileInfo> uploadFileBatch(String companyCode, String parentCode, String saveGroupCode, String name,
            String code, Integer stauts, Integer securityType, Integer actionScope, Integer sort,
            MultipartFile... multipartFiles) {
        List<ZKFileInfo> fis = new ArrayList<>();

        if (multipartFiles != null) {
            ZKFileInfo fi = null;
            for (MultipartFile mf : multipartFiles) {
                fi = this.uploadFile(companyCode, parentCode, saveGroupCode, name, code, stauts, securityType,
                        actionScope, sort, mf);
                fis.add(fi);
            }
        }
        return fis;
    }

    /**
     * 
     *
     * @Title: downloadFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2023 10:59:27 AM
     * @param isDownload
     * @param pkId
     * @param rep
     * @param userId
     * @param securityType
     * @throws IOException
     * @return void
     */
    public void getFile(boolean isDownload, String pkId, HttpServletResponse rep, String userId, int securityType)
            throws IOException {
        ZKFileInfo zkFileInfo = this.get(pkId);
        if (zkFileInfo == null) {
            log.error("[>_<:20230526-1603-001] zk.file.000006=[{}]不存在", pkId);
            throw ZKCodeException.as("zk.file.000006", null, null, pkId);
        }

        if (zkFileInfo.getType() != ZKFileInfo.ValueKey.Type.file) {
            log.error("[>_<:20230526-1603-002] zk.file.000004=[{}-{}]不是文件", zkFileInfo.getCompanyCode(),
                    zkFileInfo.getCode());
            throw ZKCodeException.as("zk.file.000004", null, null, zkFileInfo.getCode());
        }

        ZKContentType contentType = ZKContentType.parseByType(zkFileInfo.getContentType());

        if (contentType != null) {
            rep.setContentType(contentType.getContentType());
        }
        if (isDownload) {
            // 下载文件
            rep.setHeader("Content-Disposition", "attachment;fileName=" + zkFileInfo.getOriginalName());
        }
        OutputStream os = null;
        try {
            os = rep.getOutputStream();
            this.getFile(zkFileInfo, os, userId, securityType);
        } finally {
            ZKStreamUtils.closeStream(os);
        }
    }

    /**
     * 取文件
     *
     * @Title: getFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2023 10:55:51 AM
     * @param zkFileInfo
     *            文件记录数据
     * @param os
     *            文件输出流
     * @param userId
     *            取文件的用户，当取个人文件，文件创建用户需要与此参数一样，才有权限获取；
     * @param securityType
     *            取文件权限类型；类型为开放的时，不能取受限制的文件；
     * @return void
     */
    public void getFile(ZKFileInfo zkFileInfo, OutputStream os, String userId, int securityType) {
        if (zkFileInfo.getType() != ZKFileInfo.ValueKey.Type.file) {
            log.error("[>_<:20230526-1603-003] zk.file.000004=[{}-{}]不是文件", zkFileInfo.getCompanyCode(),
                    zkFileInfo.getCode());
            throw ZKCodeException.as("zk.file.000004", null, null, zkFileInfo.getCode());
        }

        if (ValueKey.ActionScope.personal == zkFileInfo.getActionScope().intValue()) {
            // 个人文件，只有上传人可以获取
            if (ZKStringUtils.isEmpty(zkFileInfo.getCreateUserId()) || !zkFileInfo.getCreateUserId().equals(userId)) {
                log.error("[>_<:20230527-1036-001] 您没有权限访问文件[{}]", zkFileInfo.getPkId());
                throw ZKCodeException.as("zk.file.000007", "无访问文件权限");
            }
        }
        if (securityType == ValueKey.SecurityType.open) {
            // 只能访问开放的文件
            if (ValueKey.SecurityType.open != zkFileInfo.getSecurityType().intValue()) {
                log.error("[>_<:20230527-1036-002] 您没有权限访问文件[{}]", zkFileInfo.getPkId());
                throw ZKCodeException.as("zk.file.000007", "无访问文件权限");
            }
        }
        this.fileDisposeService.doGetFile(zkFileInfo, os);
    }

}

