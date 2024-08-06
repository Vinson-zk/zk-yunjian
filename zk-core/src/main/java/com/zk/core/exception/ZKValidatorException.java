/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKValidatorException.java 
 * @author Vinson 
 * @Package com.zk.core.exception 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:11:14 PM 
 * @version V1.0   
*/
package com.zk.core.exception;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKValidatorsBeanUtils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

/** 
* @ClassName: ZKValidatorException 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKValidatorException extends ConstraintViolationException {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    // 直接指定验证不通过的字段及信息时使用，不是底层框架验证时抛出异常使用；
    protected Map<String, String> validatorMsg = null;

    public ZKValidatorException(Map<String, String> validatorMsg) {
        super(null);
        this.validatorMsg = validatorMsg;
    }

    public static ZKValidatorException as(Map<String, String> validatorMsg) {
        return new ZKValidatorException(validatorMsg);
    }

    ///// -------------------------------------

    public ZKValidatorException(Set<ConstraintViolation<?>> validatorMsgs) {
        super(validatorMsgs);
    }

    /**
     * 取原始验证异常
     *
     * @Title: getViolationCause
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 1, 2019 12:22:26 AM
     * @return
     * @return ConstraintViolationException
     */
    public ConstraintViolationException getViolationCause() {
        return (ConstraintViolationException) super.getCause();
    }

    // 验证失败 字段名与失败信息分隔符
    public static final String msgSeparator = ":";

    @Override
    public String getMessage() {
        return ZKJsonUtils.toJsonStr(this.getMessagePropertyAndMessageAsMap());
    }

    /**
     * 将验证失败信息转为 List<String>; 失败消息; 无字段名信息
     *
     * @Title: getMessageAsList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:36:50 PM
     * @return
     * @return List<String>
     */
    public List<String> getMessageAsList(){
        return ZKValidatorsBeanUtils.extractMessage(this);
    }

    /**
     * 将验证失败信息转为 List<String>; 字段名 + 分隔符 + 失败消息; 分隔符 默认为 ':'
     *
     * @Title: getMessagePropertyAndMessageAsList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:36:32 PM
     * @return
     * @return List<String>
     */
    public List<String> getMessagePropertyAndMessageAsList() {
        return ZKValidatorsBeanUtils.extractPropertyAndMessageAsList(this, msgSeparator);
    }

    /**
     * 将验证失败信息转为 List<String>; 字段名 + 分隔符 + 失败消息
     *
     * @Title: getMessagePropertyAndMessageAsList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:36:12 PM
     * @param separator
     *            分隔符
     * @return
     * @return List<String>
     */
    public List<String> getMessagePropertyAndMessageAsList(String separator) {
        return ZKValidatorsBeanUtils.extractPropertyAndMessageAsList(this, separator);
    }

    /**
     * 将验证失败消息转为 字段名-失败消息 的键值对 map
     *
     * @Title: getMessagePropertyAndMessageAsMap
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:35:28 PM
     * @return
     * @return Map<String,String>
     */
    public Map<String, String> getMessagePropertyAndMessageAsMap() {
        if (this.validatorMsg != null) {
            return this.validatorMsg;
        }
        else {
            return ZKValidatorsBeanUtils.extractPropertyAndMessage(this);
        }
    }

    // 验证没有通过的字段数量
    public int size() {
        return this.getConstraintViolations().size();
    }

    public String getMessageByPropertyPath(String propertyPath) {
        return this.getMessagePropertyAndMessageAsMap().get(propertyPath);
    }

}
