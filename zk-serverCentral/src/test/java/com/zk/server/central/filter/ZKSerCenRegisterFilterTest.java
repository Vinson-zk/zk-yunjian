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
* @Title: ZKSerCenRegisterFilterTest.java 
* @author Vinson 
* @Package com.zk.server.central.filter 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 14, 2020 3:51:00 PM 
* @version V1.0 
*/
package com.zk.server.central.filter;

import java.util.regex.Pattern;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSerCenRegisterFilterTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenRegisterFilterTest {

    @Test
    public void testIsDoingFilter() {
        try {

            ZKSerCenRegisterFilter zkSerCenRegisterFilter = new ZKSerCenRegisterFilter(null);
            String url = "";

            url = "/eureka/peerreplication/batch";
            TestCase.assertFalse(zkSerCenRegisterFilter.isDoingFilter(url));

            url = "/s/eureka/apps/batch/s";
            TestCase.assertFalse(zkSerCenRegisterFilter.isDoingFilter(url));

            url = "/eureka/apps/dsfd/s";
            TestCase.assertTrue(zkSerCenRegisterFilter.isDoingFilter(url));

            url = "/eureka/vips/batch/s";
            TestCase.assertTrue(zkSerCenRegisterFilter.isDoingFilter(url));

            url = "/Eureka/vIps/batch/s";
            TestCase.assertTrue(zkSerCenRegisterFilter.isDoingFilter(url));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void test() {
        try {
            // getRequestURI:/eureka/peerreplication/batch/
            // getServletPath:/eureka/apps/delta
            Pattern[] patterns = { Pattern.compile("^/eureka/apps/(.*)", Pattern.CASE_INSENSITIVE),
                    Pattern.compile("^/eureka/vips/(.*)", Pattern.CASE_INSENSITIVE),
                    Pattern.compile("^/eureka/svips/(.*)", Pattern.CASE_INSENSITIVE),
                    Pattern.compile("^/eureka/instances/(.*)", Pattern.CASE_INSENSITIVE) };
            String url = "";
            boolean res = false;

            url = "/eureka/peerreplication/batch";
            res = true;
            for (Pattern pattern : patterns) {
                res = pattern.matcher(url).matches();
                TestCase.assertFalse(res);
            }

            url = "/s/eureka/apps/batch/s";
            res = true;
            for (Pattern pattern : patterns) {
                res = pattern.matcher(url).matches();
                TestCase.assertFalse(res);
            }

            url = "/eureka/apps/dsfd/s";
            res = false;
            for (Pattern pattern : patterns) {
                res = pattern.matcher(url).matches();
                if (res)
                    break;
            }
            TestCase.assertTrue(res);

            url = "/eureka/vips/batch/s";
            res = false;
            for (Pattern pattern : patterns) {
                res = pattern.matcher(url).matches();
                if (res)
                    break;
            }
            TestCase.assertTrue(res);

            String[] patternStrs = { "^/eureka/apps/(.*)", "^/eureka/vips/(.*)", "^/eureka/svips/(.*)",
                    "^/eureka/instances/(.*)" };

            url = "/eureka/peerreplication/batch";
            res = true;
            for (String patternStr : patternStrs) {
                res = Pattern.matches(patternStr, url);
                TestCase.assertFalse(res);
            }

            url = "/s/eureka/apps/batch";
            res = true;
            for (String patternStr : patternStrs) {
                res = Pattern.matches(patternStr, url);
                TestCase.assertFalse(res);
            }

            url = "/eureka/apps/batch";
            res = false;
            for (String patternStr : patternStrs) {
                res = Pattern.matches(patternStr, url);
                if (res)
                    break;
            }
            TestCase.assertTrue(res);

            url = "/eureka/svips/batch";
            res = false;
            for (String patternStr : patternStrs) {
                res = Pattern.matches(patternStr, url);
                if (res)
                    break;
            }
            TestCase.assertTrue(res);

            url = "/Eureka/apps/batch";
            res = false;
            for (String patternStr : patternStrs) {
                res = Pattern.matches(patternStr.toUpperCase(), url.toUpperCase());
                if (res)
                    break;
            }
            TestCase.assertTrue(res);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
