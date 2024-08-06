/** 
* Copyright (c) 2012-2023 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKFileInfoTest.java 
* @author Vinson 
* @Package com.zk.file.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 21, 2023 3:18:07 PM 
* @version V1.0 
*/
package com.zk.file.entity;

import java.util.Locale;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.exception.ZKValidatorException;

import jakarta.validation.Validator;
import junit.framework.TestCase;

/** 
* @ClassName: ZKFileInfoTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileInfoTest {

    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.addBasenames("msg/zkMsg_core");
        messageSource.addBasenames("msg/zkMsg_file");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(Locale.CHINA);
        return messageSource;
    }

    Validator validator = null;

    public Validator validator() {
        if (validator == null) {
            MessageSource messageSource = this.messageSource();
            LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
            ZKValidatorMessageInterpolator zkValidatorMessageInterpolator = new ZKValidatorMessageInterpolator(
                    messageSource);
            localValidatorFactoryBean.setMessageInterpolator(zkValidatorMessageInterpolator);
            // localValidatorFactoryBean.setValidationMessageSource(messageSource);
            localValidatorFactoryBean.afterPropertiesSet();
            validator = localValidatorFactoryBean;
        }
        return validator;
    }

    public ZKFileInfo getInit() {
        ZKFileInfo fi = new ZKFileInfo();
        fi.setCompanyId("test-companyId");
        fi.setGroupCode("test-groupCode");
        fi.setCompanyCode("test-companyCode");
        fi.setSaveGroupCode("test-saveGroupCode");
        fi.setSaveUuid("test-saveUuid");
        fi.setUri("test-uri");
        fi.setSecurityType(1);
        fi.setActionScope(1);
        fi.setSort(0);
        fi.setType(1);
        fi.setStauts(1);
        fi.setName("testname");
        fi.setCode("test-code");
        return fi;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testValidateCode() {
        try {
            ZKValidatorException vErrorException = null;
            Validator validator = this.validator();
            ZKFileInfo fi = this.getInit();
            String errorMsg = null;
            Set vErrorInfos = null;
            
            fi.setCode(null);
            vErrorInfos = validator.validate(fi);
            vErrorException = new ZKValidatorException(vErrorInfos);
            System.out.println("[>_<: 20231221-0100-001] " + vErrorException.getMessage());
            errorMsg = vErrorException.getMessageByPropertyPath("code");
            TestCase.assertEquals("不能为空！", errorMsg);
            
            fi.setCode(".test-Code");
            vErrorInfos = validator.validate(fi);
            vErrorException = new ZKValidatorException(vErrorInfos);
            System.out.println("[>_<: 20231221-0100-001] " + vErrorException.getMessage());
            errorMsg = vErrorException.getMessageByPropertyPath("code");
            TestCase.assertEquals("文件代码为以[字母、数字、中划线、下划线]开头，由[字母、数字、中划线、下划线、点]组成，长度不超过64的字符串。", errorMsg);
            
            fi.setCode("test-Code_123.");
            vErrorInfos = validator.validate(fi);
            vErrorException = new ZKValidatorException(vErrorInfos);
            System.out.println("[>_<: 20231221-0100-001] " + vErrorException.getMessage());
            errorMsg = vErrorException.getMessageByPropertyPath("code");
            TestCase.assertEquals(null, errorMsg);

            fi.setCode("-test-Code_123.");
            vErrorInfos = validator.validate(fi);
            vErrorException = new ZKValidatorException(vErrorInfos);
            System.out.println("[>_<: 20231221-0100-001] " + vErrorException.getMessage());
            errorMsg = vErrorException.getMessageByPropertyPath("code");
            TestCase.assertEquals(null, errorMsg);

            fi.setCode("1test-Code_123.");
            vErrorInfos = validator.validate(fi);
            vErrorException = new ZKValidatorException(vErrorInfos);
            System.out.println("[>_<: 20231221-0100-001] " + vErrorException.getMessage());
            errorMsg = vErrorException.getMessageByPropertyPath("code");
            TestCase.assertEquals(null, errorMsg);
            
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}

