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
* @Title: ZKSecPrincipalCollection.java 
* @author Vinson 
* @Package com.zk.security.principal.pc
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 7:12:35 PM 
* @version V1.0 
*/
package com.zk.security.principal.pc;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import com.zk.security.principal.ZKSecPrincipal;

/** 
* @ClassName: ZKSecPrincipalCollection 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecPrincipalCollection<ID> extends Serializable, Iterable<ZKSecPrincipal<ID>> {

    ZKSecPrincipal<ID> getPrimaryPrincipal();

    ZKSecPrincipal<ID> getPrimaryPrincipal(String realmName);

    void add(String realmName, ZKSecPrincipal<ID> p);

    void addAll(String realmName, Collection<ZKSecPrincipal<ID>> ps);

    void addAll(ZKSecPrincipalCollection<ID> pc);

    Collection<ZKSecPrincipal<ID>> getByRealmName(String realmName);

    boolean isEmpty();

    Set<ZKSecPrincipal<ID>> asSet();

    int size();

    Set<String> getRealmNames();

    Set<Entry<String, Set<ZKSecPrincipal<ID>>>> asEntrySet();

}

