package com.zk.demo.saToken;

import com.zk.demo.saToken.commons.ZKSaTokenContextForThreadLocal;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.stp.StpUtil;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 学习
 * @ClassName ZKSaTokenDemo
 * @Package com.zk.demo.saToken
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-05 17:18:35
 **/
public class ZKSaTokenDemo {

    public static void main(String[] args){
        try{
            SaManager.setSaTokenContext(new ZKSaTokenContextForThreadLocal());
            // 设置不读 cookie
            SaManager.getConfig().setIsReadCookie(false);
            StpUtil.login(9527);

            System.out.println("[^_^: 20220905-1901-001] ==== " + StpUtil.isLogin());
            System.out.println("[^_^: 20220905-1901-001] ==== " + StpUtil.getTokenValue());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
