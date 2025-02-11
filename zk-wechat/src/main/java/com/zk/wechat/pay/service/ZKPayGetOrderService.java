/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKPayGetOrderService.java 
* @author Vinson 
* @Package com.zk.wechat.pay.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 19, 2021 11:52:58 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.service;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.core.exception.ZKBusinessException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.wechat.pay.dao.ZKPayGetOrderDao;
import com.zk.wechat.pay.entity.ZKPayGetAmount;
import com.zk.wechat.pay.entity.ZKPayGetBusinessType;
import com.zk.wechat.pay.entity.ZKPayGetOrder;
import com.zk.wechat.pay.entity.ZKPayGetPayer;
import com.zk.wechat.pay.entity.ZKPayGroup;
import com.zk.wechat.pay.enumType.ZKPayGetChannel;
import com.zk.wechat.pay.enumType.ZKPayStatus;

/** 
* @ClassName: ZKPayGetOrderService 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Service
@Transactional(readOnly = true)
public class ZKPayGetOrderService extends ZKBaseService<String, ZKPayGetOrder, ZKPayGetOrderDao> {

    /**
     * 日志对象
     */
    protected Logger log = LogManager.getLogger(getClass());

    @Autowired
    ZKPayGetOrderHistoryService payGetOrderHistoryService;

    @Autowired
    ZKPayGetAmountService payGetAmountService;

    @Autowired
    ZKPayGetPayerService payGetPayerService;

    @Autowired
    ZKPayGetBusinessTypeService payGetBusinessTypeService;
    
    @Autowired
    ZKPayGroupService payGroupService;

    // 校验 业务渠道类型 是否可用
    private void checkBusinessType(String businessCode, ZKPayGetBusinessType businessType) {
        if (businessType == null) {
            log.error("[^_^:20210220-0906-001] 暂不支持此业务类型[{}]", businessCode);
            throw ZKBusinessException.as("zk.wechat.000007", null, businessCode);
        }

        if (ZKPayGetBusinessType.Status.enabled != businessType.getStatus().intValue()) {
            log.error("[^_^:20210220-0906-002] 业务类型[{}]被禁止使用", businessCode);
            throw ZKBusinessException.as("zk.wechat.000008", null, businessCode);
        }

        if (businessType.getStartDate() != null) {
            if (businessType.getStartDate().getTime() > (new Date()).getTime()) {
                log.error("[^_^:20210220-0906-003] 业务类型[{}]于[{}]才开始启用", businessCode,
                        ZKDateUtils.formatDate(businessType.getStartDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
                throw ZKBusinessException.as("zk.wechat.000009", null, businessCode,
                        ZKDateUtils.formatDate(businessType.getStartDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            }
        }

        if (businessType.getEndDate() != null) {
            if (businessType.getEndDate().getTime() < (new Date()).getTime()) {
                log.error("[^_^:20210220-0906-004] 业务类型[{}]于[{}]过期", businessCode,
                        ZKDateUtils.formatDate(businessType.getEndDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
                throw ZKBusinessException.as("zk.wechat.000010", null, businessCode,
                        ZKDateUtils.formatDate(businessType.getEndDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            }
        }
    }

    // 校验 后台系统的支付渠道 是否可用
    private void checkPayment(String payGroupCode, ZKPayGroup payGroup) {
        if (payGroup == null) {
            log.error("[^_^:20210221-0906-001] 系统支付关系组[{}]不存在", payGroupCode);
            throw ZKBusinessException.as("zk.wechat.000012", null, payGroupCode);
        }

        if (ZKPayGroup.Status.enabled != payGroup.getStatus().intValue()) {
            log.error("[^_^:20210221-0906-002] 系统支付关系组[{}]被禁止使用", payGroupCode);
            throw ZKBusinessException.as("zk.wechat.000013", null, payGroupCode);
        }

        if (payGroup.getStartDate() != null) {
            if (payGroup.getStartDate().getTime() > (new Date()).getTime()) {
                log.error("[^_^:20210221-0906-003] 系统支付关系组[{}]于[{}]才开始启用", payGroupCode,
                        ZKDateUtils.formatDate(payGroup.getStartDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
                throw ZKBusinessException.as("zk.wechat.000014", null, payGroupCode,
                        ZKDateUtils.formatDate(payGroup.getStartDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            }
        }

        if (payGroup.getEndDate() != null) {
            if (payGroup.getEndDate().getTime() < (new Date()).getTime()) {
                log.error("[^_^:20210221-0906-004] 系统支付关系组[{}]于[{}]过期", payGroupCode,
                        ZKDateUtils.formatDate(payGroup.getEndDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
                throw ZKBusinessException.as("zk.wechat.000015", null, payGroupCode,
                        ZKDateUtils.formatDate(payGroup.getEndDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            }

        }
    }

    // 创建支付订单
    @Transactional(readOnly = false)
    public ZKPayGetOrder create(ZKPayGetChannel wxChannel, String payGroupCode, String businessCode,
            String businessNo, String description, Date timeExpire, String notifyUrl, ZKPayGetOrder payGetOrder,
            ZKPayGetAmount payGetAmount, ZKPayGetPayer payGetPayer) {

        ZKPayGetOrder oldPayGetOrder = this.getByBusiness(businessCode, businessNo);
        if (oldPayGetOrder != null) {
            // 支付订单已存在，不能重复支付
            log.error("[>_<:20210221-1049-001] 支付订单[{}-{}]已存在，请务重复支付", businessCode, businessNo);
            throw ZKBusinessException.as("zk.wechat.000016", null, businessNo);
        }

        ZKPayGroup payGroup = payGroupService.getByCode(payGroupCode);
        this.checkPayment(payGroupCode, payGroup);

        ZKPayGetBusinessType businessType = payGetBusinessTypeService.getByCode(businessCode);
        this.checkBusinessType(businessCode, businessType);

        payGetOrder.setTimeExpire(timeExpire);
        payGetOrder.setPayStatus(ZKPayStatus.CREATE);
        payGetOrder.setWxChannel(wxChannel);
        payGetOrder.setPayGroupCode(payGroup.getCode());
        payGetOrder.setMchid(payGroup.getWxMchid());
        payGetOrder.setAppid(payGroup.getWxAppId());
        payGetOrder.setDescriptionRename(description);
        payGetOrder.setBusinessCode(businessType.getCode());
        payGetOrder.setBusinessNo(businessNo);
        payGetOrder.setNewRecord(true);
//        payGetOrder.preInsert();
        this.preInsert(payGetOrder);

        payGetOrder.setNotifyUrl(ZKStringUtils.replaceByPoint(notifyUrl, 0, payGetOrder.getPkId()));

        // 非 JSAPI/小程序 支付时，可能没支付人这一项数据；
        if (payGetPayer != null) {
            payGetPayer.setPayOrderPkId(payGetOrder.getPkId());
            payGetPayerService.save(payGetPayer);
        }

        payGetAmount.setPayOrderPkId(payGetOrder.getPkId());
        payGetAmountService.save(payGetAmount);

        this.save(payGetOrder);
        payGetOrder.setPayGetAmount(payGetAmount);
        payGetOrder.setPayGetPayer(payGetPayer);

        return payGetOrder;
    }

    public ZKPayGetOrder getDetail(String pkId) {
        ZKPayGetOrder payGetOrder = this.get(new ZKPayGetOrder(pkId));
        if (payGetOrder != null) {
            payGetOrder.setPayGetAmount(this.payGetAmountService.getByPayOrderPkId(pkId));
            payGetOrder.setPayGetPayer(this.payGetPayerService.getByPayOrderPkId(pkId));
        }
        return payGetOrder;
    }

    /**
     * 修改统一下单后生成的预支付信息
     *
     * @Title: updatePrepay
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 20, 2021 10:05:09 PM
     * @param pkId
     * @param payStatus
     * @param wxResStatusCode
     * @param prepayId
     * @return int
     */
    @Transactional(readOnly = false)
    public int updatePrepay(String pkId, ZKPayStatus payStatus, String wxResStatusCode,
            String prepayId) {
        return this.dao.updatePrepay(pkId, payStatus, wxResStatusCode, prepayId, new Date());
    }

    @Transactional(readOnly = false)
    public int updateTimeExpire(String pkId, Date timeExpire) {
        return this.dao.updateTimeExpire(pkId, timeExpire);
    }

    /**
     * 根据业务类型获取支付订单
     *
     * @Title: getByBusiness
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 21, 2021 9:18:49 PM
     * @param businessCode
     *            业务代码
     * @param businessNo
     *            业务单号
     * @return
     * @return ZKPayGetOrder
     */
    public ZKPayGetOrder getByBusiness(String businessCode, String businessNo) {
        return this.dao.getByBusiness(ZKPayGetOrder.sqlHelper().getBlockSqlCols(""),
                ZKPayGetOrder.sqlHelper().getTableName(), businessCode, businessNo);
    }

    /**
     * 物理删除数据
     * 
     * @param entity
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public int diskDel(ZKPayGetOrder entity) {
        // 保存历史
        this.payGetOrderHistoryService.save(entity);
        return dao.diskDel(entity);
    }

    /**
     * 更新调起支付签名时间
     *
     * @Title: updatePaySignDate
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 22, 2021 12:09:56 PM
     * @param pkId
     * @param paySignDate
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int updatePaySignDate(String pkId, Date paySignDate) {
        return this.dao.updatePaySignDate(pkId, paySignDate);
    }

    /**
     * 修改支付订单的状态
     *
     * @Title: updatePayStatus
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 22, 2021 3:13:57 PM
     * @param pkId
     * @param payStatus
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int updatePayStatus(String pkId, ZKPayStatus payStatus) {
        return this.dao.updatePayStatus(pkId, payStatus, new Date());
    }

    /**
     * 修改支付订单渠道，同步修改订单是状态；
     *
     * @Title: updatePayChannel
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 22, 2021 4:02:14 PM
     * @param pkId
     * @param payStatus
     * @param wxChannel
     * @return
     * @return int
     */
    @Transactional(readOnly = false)
    public int updatePayChannel(String pkId, ZKPayStatus payStatus, ZKPayGetChannel wxChannel) {
        return this.dao.updatePayChannel(pkId, payStatus, wxChannel, new Date());
    }

}
