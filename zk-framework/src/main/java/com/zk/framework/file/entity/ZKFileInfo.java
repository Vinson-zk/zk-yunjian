/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKFileInfo.java 
* @author Vinson 
* @Package com.zk.framework.file.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 23, 2024 6:22:11 PM 
* @version V1.0 
*/
package com.zk.framework.file.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.zk.core.commons.data.ZKJson;

/** 
* @ClassName: ZKFileInfo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileInfo implements Serializable {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    // 实体编号（唯一标识）
    protected String pkId;

    // 创建者, ID
    protected String createUserId;

    // 更新者
    protected String updateUserId;

    // 创建日期
    protected Date createDate;

    // 更新日期
    protected Date updateDate;

    // 备注
    protected String remarks;

    // 国际化说明 description 加个前缀，防关键字冲突
    protected ZKJson pDesc;

    // 备用字段 1
    protected String spare1;

    // 备用字段 2
    protected String spare2;

    // 备用字段 3
    protected String spare3;

    // 备用字段 ZKJson
    protected ZKJson spareJson;

    // 删除标记（0：正常；1：删除;）
    protected Integer delFlag;

    // 数据版本
    protected Long version;

    protected ZKFileInfo parent;

    /**
     * 子菜单(子路由)数组；
     */
    protected List<ZKFileInfo> children;

    protected Integer depth;

    /**
     * 集团代码
     */
    String groupCode;

    /**
     * 公司ID
     */
    String companyId;

    /**
     * 公司代码
     */
    String companyCode;

    /**
     * 父目录ID；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     */
    String parentId;

    /**
     * 父目录代码；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     */
    String parentCode;

    /**
     * 文件名称
     */
    String name;

    /**
     * 文件代码；公司下唯一;
     */
    String code;

    /**
     * 文件原始名称
     */
    String originalName;

    /**
     * 文件类型
     */
    String contentType;

    /**
     * 文件大小；单位 b
     */
    Long size;

    /**
     * 状态：0-上传、1-正常、2-失效[上传后在指定时间来，没修改为正常时，会自动失效，会删除实际文件]、3-禁用[不能下载，但能查看]
     */
    Integer stauts;

    /**
     * 文件保存标识，全表唯一标识，UUID，与ID 作用重复，生成一个保存以备用
     */
    String saveUuid;

    /**
     * 文件分组代码，全表唯一，自动生成，UUID；以便附件分组
     */
    String saveGroupCode;

    /**
     * 文件权限类型：0-私有[需要有身份才获取]，1-开放[可以通过开放的接口获取]
     */
    Integer securityType;

    /**
     * 文件作用域：0-普通；1-个人；可以扩展其他作用域，作用域尽量广义一点
     */
    Integer actionScope;

    /**
     * 排序
     */
    Integer sort;

    /**
     * 数据类型；0-文件；1-目录；文件不能做父节点；即 c_type = 0 的数据，不能做为父节点；
     */
    Integer type;

    /**
     * 访问文件的相对地址；目录设置为 /
     */
    String uri;

    /**
     * @return pkId sa
     */
    public String getPkId() {
        return pkId;
    }

    /**
     * @param pkId
     *            the pkId to set
     */
    public void setPkId(String pkId) {
        this.pkId = pkId;
    }

    /**
     * @return createUserId sa
     */
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     * @param createUserId
     *            the createUserId to set
     */
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * @return updateUserId sa
     */
    public String getUpdateUserId() {
        return updateUserId;
    }

    /**
     * @param updateUserId
     *            the updateUserId to set
     */
    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * @return createDate sa
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     *            the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return updateDate sa
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     *            the updateDate to set
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return remarks sa
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @param remarks
     *            the remarks to set
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * @return pDesc sa
     */
    public ZKJson getpDesc() {
        return pDesc;
    }

    /**
     * @param pDesc
     *            the pDesc to set
     */
    public void setpDesc(ZKJson pDesc) {
        this.pDesc = pDesc;
    }

    /**
     * @return spare1 sa
     */
    public String getSpare1() {
        return spare1;
    }

    /**
     * @param spare1
     *            the spare1 to set
     */
    public void setSpare1(String spare1) {
        this.spare1 = spare1;
    }

    /**
     * @return spare2 sa
     */
    public String getSpare2() {
        return spare2;
    }

    /**
     * @param spare2
     *            the spare2 to set
     */
    public void setSpare2(String spare2) {
        this.spare2 = spare2;
    }

    /**
     * @return spare3 sa
     */
    public String getSpare3() {
        return spare3;
    }

    /**
     * @param spare3
     *            the spare3 to set
     */
    public void setSpare3(String spare3) {
        this.spare3 = spare3;
    }

    /**
     * @return spareJson sa
     */
    public ZKJson getSpareJson() {
        return spareJson;
    }

    /**
     * @param spareJson
     *            the spareJson to set
     */
    public void setSpareJson(ZKJson spareJson) {
        this.spareJson = spareJson;
    }

    /**
     * @return delFlag sa
     */
    public Integer getDelFlag() {
        return delFlag;
    }

    /**
     * @param delFlag
     *            the delFlag to set
     */
    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * @return version sa
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version
     *            the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * @return parent sa
     */
    public ZKFileInfo getParent() {
        return parent;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(ZKFileInfo parent) {
        this.parent = parent;
    }

    /**
     * @return children sa
     */
    public List<ZKFileInfo> getChildren() {
        return children;
    }

    /**
     * @param children
     *            the children to set
     */
    public void setChildren(List<ZKFileInfo> children) {
        this.children = children;
    }

    /**
     * @return depth sa
     */
    public Integer getDepth() {
        return depth;
    }

    /**
     * @param depth
     *            the depth to set
     */
    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    /**
     * @return groupCode sa
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * @param groupCode
     *            the groupCode to set
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * @return companyId sa
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId
     *            the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return companyCode sa
     */
    public String getCompanyCode() {
        return companyCode;
    }

    /**
     * @param companyCode
     *            the companyCode to set
     */
    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    /**
     * @return parentId sa
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId
     *            the parentId to set
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return parentCode sa
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * @param parentCode
     *            the parentCode to set
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * @return name sa
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return code sa
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return originalName sa
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * @param originalName
     *            the originalName to set
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    /**
     * @return contentType sa
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType
     *            the contentType to set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return size sa
     */
    public Long getSize() {
        return size;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * @return stauts sa
     */
    public Integer getStauts() {
        return stauts;
    }

    /**
     * @param stauts
     *            the stauts to set
     */
    public void setStauts(Integer stauts) {
        this.stauts = stauts;
    }

    /**
     * @return saveUuid sa
     */
    public String getSaveUuid() {
        return saveUuid;
    }

    /**
     * @param saveUuid
     *            the saveUuid to set
     */
    public void setSaveUuid(String saveUuid) {
        this.saveUuid = saveUuid;
    }

    /**
     * @return saveGroupCode sa
     */
    public String getSaveGroupCode() {
        return saveGroupCode;
    }

    /**
     * @param saveGroupCode
     *            the saveGroupCode to set
     */
    public void setSaveGroupCode(String saveGroupCode) {
        this.saveGroupCode = saveGroupCode;
    }

    /**
     * @return securityType sa
     */
    public Integer getSecurityType() {
        return securityType;
    }

    /**
     * @param securityType
     *            the securityType to set
     */
    public void setSecurityType(Integer securityType) {
        this.securityType = securityType;
    }

    /**
     * @return actionScope sa
     */
    public Integer getActionScope() {
        return actionScope;
    }

    /**
     * @param actionScope
     *            the actionScope to set
     */
    public void setActionScope(Integer actionScope) {
        this.actionScope = actionScope;
    }

    /**
     * @return sort sa
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * @param sort
     *            the sort to set
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * @return type sa
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return uri sa
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param uri
     *            the uri to set
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        return "ZKFileInfo [pkId=" + pkId + ", createUserId=" + createUserId + ", updateUserId=" + updateUserId
                + ", createDate=" + createDate + ", updateDate=" + updateDate + ", remarks=" + remarks + ", pDesc="
                + pDesc + ", spare1=" + spare1 + ", spare2=" + spare2 + ", spare3=" + spare3 + ", spareJson="
                + spareJson + ", delFlag=" + delFlag + ", version=" + version + ", parent=" + parent + ", children="
                + children + ", depth=" + depth + ", groupCode=" + groupCode + ", companyId=" + companyId
                + ", companyCode=" + companyCode + ", parentId=" + parentId + ", parentCode=" + parentCode + ", name="
                + name + ", code=" + code + ", originalName=" + originalName + ", contentType=" + contentType
                + ", size=" + size + ", stauts=" + stauts + ", saveUuid=" + saveUuid + ", saveGroupCode="
                + saveGroupCode + ", securityType=" + securityType + ", actionScope=" + actionScope + ", sort=" + sort
                + ", type=" + type + ", uri=" + uri + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + ", toString()=" + super.toString() + "]";
    }
}
