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
 * @Title: ZKValidateCodeUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 4:24:29 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.awt.Font;
import java.io.File;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKValidateCodeUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKValidateCodeUtilsTest {

    @Test
    public void testGetFontByHeight() {
        try {
            String fontStr = null;
            Font font = null;

            fontStr = "Arial";
            font = ZKValidateCodeUtils.ZKImgUtils.getFontByHeight(28, null, new File(""));
            TestCase.assertEquals(fontStr, font.getFamily());

            fontStr = "Arial";
            font = ZKValidateCodeUtils.ZKImgUtils.getFontByHeight(28, null, null);
            TestCase.assertEquals(fontStr, font.getFamily());

            fontStr = "Times New Roman";
            font = ZKValidateCodeUtils.ZKImgUtils.getFontByHeight(28, fontStr, null);
            TestCase.assertEquals(fontStr, font.getFamily());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
