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
 * @Title: ZKValidatorMessageInterpolator.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 10:54:48 AM 
 * @version V1.0   
*/
package com.zk.core.commons;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.context.MessageSource;

import com.zk.core.utils.ZKStringUtils;

import jakarta.validation.MessageInterpolator;

/** 
* @ClassName: ZKValidatorMessageInterpolator 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKValidatorMessageInterpolator implements MessageInterpolator {
//  extends ResourceBundleMessageInterpolator {

    private static final Pattern MESSAGE_PARAMETER_PATTERN = Pattern.compile("\\{(\\S+?)\\}");

    private static final String MESSAGE_PARAMETER_PREFIX = "{";

    private static final String MESSAGE_PARAMETER_SUFFIX = "}";

    /**
     * The default locale in the current JVM.
     */
    private final Locale defaultLocale;

    private MessageSource messageSource;

    // MessageSourceResourceBundleLocator messageSourceResourceBundleLocator;

    public ZKValidatorMessageInterpolator(MessageSource messageSource) {
        this(messageSource, null);
    }

    public ZKValidatorMessageInterpolator(MessageSource messageSource, Locale defaultLocale) {
        // super(new MessageSourceResourceBundleLocator(messageSource));
        // this.messageSourceResourceBundleLocator = new
        // MessageSourceResourceBundleLocator(messageSource);
        this.messageSource = messageSource;
        this.defaultLocale = defaultLocale == null ? Locale.getDefault() : defaultLocale;
    }

    protected Map<String, Object> getMsgParams(Context context) {
        if (context == null || context.getConstraintDescriptor() == null) {
            return null;
        }
        return context.getConstraintDescriptor().getAttributes();
    }

    protected String interpolateMessage(String message, Context context, Locale locale) {

//        System.out.println("=== 1 " + ZKJsonUtils.toJsonStr(context));
//        System.out.println("=== 2 " + ZKJsonUtils.toJsonStr(context.getConstraintDescriptor().getAnnotation()));
//        System.out.println("=== 3 " + ZKJsonUtils.toJsonStr(context.getConstraintDescriptor().getAttributes()));
//        System.out.println(
//                "=== 4 " + ZKJsonUtils.toJsonStr(context.getConstraintDescriptor().getValidationAppliesTo()));
//        System.out.println("=== 5 "
//                + ZKJsonUtils.toJsonStr(context.getConstraintDescriptor().getGroups()));

        String interpolateMessage = message;
        Matcher matcher = MESSAGE_PARAMETER_PATTERN.matcher(interpolateMessage);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb,
                    Matcher.quoteReplacement(replaceEscapedLiterals(matcher.group(), getMsgParams(context), locale)));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String replaceEscapedLiterals(String resolvedMessageKey, Map<String, Object> msgParams, Locale locale) {
        String interpolateMessage = resolvedMessageKey;
        // 替换 key 的开头 "{"
        if (interpolateMessage.indexOf(MESSAGE_PARAMETER_PREFIX) == 0) {
            interpolateMessage = interpolateMessage.substring(MESSAGE_PARAMETER_PREFIX.length());
        }
        // 替换 key 的开头 "}"
        if (interpolateMessage.lastIndexOf(MESSAGE_PARAMETER_SUFFIX) == interpolateMessage.length() - 1) {
            interpolateMessage = interpolateMessage.substring(0,
                    interpolateMessage.length() - MESSAGE_PARAMETER_SUFFIX.length());
        }
//        String[] keys = interpolateMessage.split(MESSAGE_PARAMETER_TOKEN);

        try {
            interpolateMessage = this.messageSource.getMessage(interpolateMessage, null, locale);
            interpolateMessage = ZKStringUtils.replaceByName(interpolateMessage, msgParams);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return interpolateMessage;
    }

    @Override
    public String interpolate(String messageTemplate, Context context) {
        return interpolateMessage(messageTemplate, context, this.defaultLocale);
    }

    @Override
    public String interpolate(String messageTemplate, Context context, Locale locale) {
        return interpolateMessage(messageTemplate, context, locale);
    }
    /*******/

}
