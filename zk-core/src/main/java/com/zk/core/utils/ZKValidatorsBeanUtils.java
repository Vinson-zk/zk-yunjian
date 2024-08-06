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
 * @Title: ZKValidatorsBeanUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:31:28 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zk.core.exception.ZKValidatorException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

/** 
* @ClassName: ZKValidatorsBeanUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKValidatorsBeanUtils {

    /**
     * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
     * 
     * @throws ZKValidatorException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void validateWithException(Validator validator, Object object, Class<?>... groups)
            throws ZKValidatorException {
        Set constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ZKValidatorException(constraintViolations);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void validateWithException(Validator validator, Object object, String propertyName,
            Class<?>... groups) throws ZKValidatorException {
        Set constraintViolations = validator.validateProperty(object, propertyName, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ZKValidatorException(constraintViolations);
        }
    }

    /**
     * 辅助方法，将验证失败信息转为 List<String>; 失败消息；无字段名信息
     *
     * @Title: extractMessage
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:20:45 PM
     * @param e
     * @return
     * @return List<String>
     */
    public static List<String> extractMessage(ConstraintViolationException e) {
        return extractMessage(e.getConstraintViolations());
    }

    /**
     * 辅助方法，将验证失败信息转为 List<String>; 失败消息
     *
     * @Title: extractMessage
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:20:51 PM
     * @param constraintViolations
     * @return
     * @return List<String>
     */
    @SuppressWarnings("rawtypes")
    public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
        List<String> errorMessages = new ArrayList<String>();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.add(violation.getMessage());
        }
        return errorMessages;
    }

    /**
     * 辅助方法，将验证失败信息转为 Map<String, String>; 字段名 - 失败消息
     *
     * @Title: extractPropertyAndMessage
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:20:27 PM
     * @param e
     * @return
     * @return Map<String,String>
     */
    public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e) {
        return extractPropertyAndMessage(e.getConstraintViolations());
    }

    /**
     * 辅助方法，将验证失败信息转为 Map<String, String>; 字段名 - 失败消息
     *
     * @Title: extractPropertyAndMessage
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:20:00 PM
     * @param constraintViolations
     * @return
     * @return Map<String,String>
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> extractPropertyAndMessage(
            Set<? extends ConstraintViolation> constraintViolations) {
        Map<String, String> errorMessages = new HashMap<String, String>();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errorMessages;
    }

    /**
     * 辅助方法，将验证失败信息转为 List<String>; 字段名 + 分隔符 + 失败消息
     *
     * @Title: extractPropertyAndMessageAsList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:19:01 PM
     * @param e
     * @param separator
     *            分隔符
     * @return List<String>
     */
    public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator) {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
    }

    /**
     * 辅助方法，将验证失败信息转为 List<String>; 字段名 + 分隔符 + 失败消息
     *
     * @Title: extractPropertyAndMessageAsList
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 5, 2020 2:18:03 PM
     * @param constraintViolations
     * @param separator
     *            分隔符
     * @return List<String>
     */
    @SuppressWarnings("rawtypes")
    public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations,
            String separator) {
        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
        }
        return errorMessages;
    }


}
