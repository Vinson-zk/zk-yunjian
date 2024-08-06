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
* @Title: ZKSecDefaultPrincipalCollection.java 
* @author Vinson 
* @Package com.zk.security.principal.pc
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 14, 2021 7:13:18 PM 
* @version V1.0 
*/
package com.zk.security.principal.pc;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.zk.security.principal.ZKSecPrincipal;

/** 
* @ClassName: ZKSecDefaultPrincipalCollection 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultPrincipalCollection<ID> implements ZKSecPrincipalCollection<ID> {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Map<String, Set<ZKSecPrincipal<ID>>> realmPrincipals;

    @Override
    public Set<ZKSecPrincipal<ID>> asSet() {
        if (realmPrincipals == null || realmPrincipals.isEmpty()) {
            return Collections.emptySet();
        }
        Set<ZKSecPrincipal<ID>> ps = new LinkedHashSet<>();
        for (Set<ZKSecPrincipal<ID>> ss : realmPrincipals.values()) {
            ps.addAll(ss);
        }
        if (ps.isEmpty()) {
            return Collections.emptySet();
        }
        return ps;
    }

    @Override
    public Set<Entry<String, Set<ZKSecPrincipal<ID>>>> asEntrySet() {
        if (realmPrincipals == null || realmPrincipals.isEmpty()) {
            return Collections.emptySet();
        }
        return this.realmPrincipals.entrySet();
    }

    @Override
    public boolean isEmpty() {
        return realmPrincipals == null || realmPrincipals.isEmpty();
    }

    @Override
    public Iterator<ZKSecPrincipal<ID>> iterator() {
        return asSet().iterator();
    }

    @Override
    public ZKSecPrincipal<ID> getPrimaryPrincipal() {
        if (isEmpty()) {
            return null;
        }
        Iterator<ZKSecPrincipal<ID>> iterator = iterator();
        ZKSecPrincipal<ID> p = null;
        while (iterator.hasNext()) {
            p = iterator.next();
            if (p.isPrimary()) {
                break;
            }
        }
        return p;
    }

    @Override
    public ZKSecPrincipal<ID> getPrimaryPrincipal(String realmName) {
        if (isEmpty()) {
            return null;
        }
        Set<ZKSecPrincipal<ID>> sets = this.getPrincipalsLazy(realmName);
        ZKSecPrincipal<ID> p = null;
        if (sets != null && !sets.isEmpty()) {
            for (ZKSecPrincipal<ID> t : sets) {
                p = t;
                if (p.isPrimary()) {
                    break;
                }
            }
        }
        return p;
    }

    @Override
    public void add(String realmName, ZKSecPrincipal<ID> principal) {
        if (realmName == null) {
            throw new IllegalArgumentException("realmName argument cannot be null.");
        }
        if (principal == null) {
            throw new IllegalArgumentException("principal argument cannot be null.");
        }
        getPrincipalsLazy(realmName).add(principal);
    }

    @Override
    public void addAll(String realmName, Collection<ZKSecPrincipal<ID>> principals) {
        if (realmName == null) {
            throw new IllegalArgumentException("realmName argument cannot be null.");
        }
        if (principals == null) {
            throw new IllegalArgumentException("principals argument cannot be null.");
        }
        if (principals.isEmpty()) {
            throw new IllegalArgumentException("principals argument cannot be an empty collection.");
        }
        getPrincipalsLazy(realmName).addAll(principals);
    }

    protected Set<ZKSecPrincipal<ID>> getPrincipalsLazy(String realmName) {
        if (realmPrincipals == null) {
            realmPrincipals = new LinkedHashMap<String, Set<ZKSecPrincipal<ID>>>();
        }
        Set<ZKSecPrincipal<ID>> principals = realmPrincipals.get(realmName);
        if (principals == null) {
            principals = new LinkedHashSet<>();
            realmPrincipals.put(realmName, principals);
        }
        return principals;
    }

    @Override
    public int size() {
        if (realmPrincipals != null) {
            return realmPrincipals.size();
        }
        return 0;
    }

    @Override
    public Collection<ZKSecPrincipal<ID>> getByRealmName(String realmName) {
        if (!isEmpty()) {
            return this.realmPrincipals.get(realmName);
        }
        return null;
    }

    @Override
    public Set<String> getRealmNames() {
        if (!isEmpty()) {
            return this.realmPrincipals.keySet();
        }
        return null;
    }

    @Override
    public void addAll(ZKSecPrincipalCollection<ID> pc) {
        if (realmPrincipals == null) {
            realmPrincipals = new LinkedHashMap<String, Set<ZKSecPrincipal<ID>>>();
        }
        if (pc != null && !pc.isEmpty()) {
            for (Entry<String, Set<ZKSecPrincipal<ID>>> o : pc.asEntrySet()) {
                if (o.getValue() != null && !o.getValue().isEmpty()) {
                    this.addAll(o.getKey(), o.getValue());
                }
            }
        }
    }

}
