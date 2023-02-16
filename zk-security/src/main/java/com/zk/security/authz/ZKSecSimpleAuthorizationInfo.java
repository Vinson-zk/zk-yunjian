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
* @Title: ZKSecSimpleAuthorizationInfo.java 
* @author Vinson 
* @Package com.zk.security.authz 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 13, 2021 2:50:04 PM 
* @version V1.0 
*/
package com.zk.security.authz;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/** 
* @ClassName: ZKSecSimpleAuthorizationInfo 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecSimpleAuthorizationInfo implements ZKSecAuthorizationInfo {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 2649735532521497365L;

    private Set<String> apiCodes;

    private Set<String> authCodes;

    private Collection<String> getApiCodeStore() {
        if (apiCodes == null) {
            apiCodes = new HashSet<String>();
        }
        return apiCodes;
    }

    public void addApiCode(String apiCodes) {
        this.getApiCodeStore().add(apiCodes);
    }

    public void addApiCode(Collection<String> apiCodes) {
        this.getApiCodeStore().addAll(apiCodes);
    }

    @Override
    public Collection<String> getApiCodes() {
        if (apiCodes == null) {
            return Collections.emptySet();
        }
        else {
            return Collections.unmodifiableSet(apiCodes);
        }
    }

    /////////////////////////////////////////////////////////////

    private Collection<String> getAuthCodeStore() {
        if (authCodes == null) {
            authCodes = new HashSet<String>();
        }
        return authCodes;
    }

    public void addAuthCode(String authCode) {
        this.getAuthCodeStore().add(authCode);
    }

    public void addAuthCode(Collection<String> authCodes) {
        this.getAuthCodeStore().addAll(authCodes);
    }

    @Override
    public Collection<String> getAuthCodes() {
        if (authCodes == null) {
            return Collections.emptySet();
        }
        else {
            return Collections.unmodifiableSet(authCodes);
        }
    }


}
