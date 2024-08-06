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
* @Title: ZKSecRedisTicketManager.java 
* @author Vinson 
* @Package com.zk.security.ticket.support.redis 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 8:16:08 AM 
* @version V1.0 
*/
package com.zk.security.ticket.support.redis;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.collect.Lists;
import com.zk.core.exception.ZKSystemException;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.security.principal.ZKSecPrincipal;
import com.zk.security.principal.pc.ZKSecPrincipalCollection;
import com.zk.security.ticket.ZKSecAbstractTicketManager;
import com.zk.security.ticket.ZKSecDelegatingTicker;
import com.zk.security.ticket.ZKSecProxyTickerManager;
import com.zk.security.ticket.ZKSecTicket;

/** 
* @ClassName: ZKSecRedisTicketManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecRedisTicketManager extends ZKSecAbstractTicketManager implements ZKSecProxyTickerManager {

    protected Logger logger = LogManager.getLogger(this.getClass());

    public static interface ZKRedisKey {
        public static final String Ticket_Mapping = "zk_sec_Ticket_mapping";
    }

    public static interface AttrKeyName {
        /**
         * 令牌 put 信息时 key 的前缀，防止与固定属性 key 重复
         */
        public static final String valueKey_prefix = "tk_v_keyPerfix";

        public static final String principalCollection = "principalCollection";

        public static final String baseInfo = "tk_baseInfo";

        /**
         * 身份集合放入 mongo 中时做一些处理，把主要的属性抽出来做个数组，以便查询。 其他属性还是以二进制存放与读取。
         */
        public static final String pcKey_PC = "pcKeyPC";

        public static final String pcKey_PS = "pcKeyPS";

        public static final String pKey_pkID = "pKeypkID";

        public static final String pKey_Type = "pKeyType";

        public static final String pKey_GroupCode = "pKeyGroupCode";

        public static final String pKey_AuthorizationInfo = "pKeyAuthorizationInfo";
    }
    
    // 默认有效时长，900000 毫秒；单位：毫秒
    @Value("${zk.sec.ticket.default.valid.time:900000}")
    private long defaultValidTime;

    public ZKSecRedisTicketManager() {
        
    }
    public ZKSecRedisTicketManager(ZKJedisOperatorStringKey jedisOperator) {
        this.jedisOperator = jedisOperator;
    }

    ZKJedisOperatorStringKey jedisOperator;

    /**
     * @return jedisOperator sa
     */
    public ZKJedisOperatorStringKey getJedisOperator() {
        return jedisOperator;
    }

    /**
     * @param jedisOperator
     *            the jedisOperator to set
     */
    public void setJedisOperator(ZKJedisOperatorStringKey jedisOperator) {
        this.jedisOperator = jedisOperator;
    }

    /**
     * 
     *
     * @Title: createTicket
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 16, 2021 2:44:07 PM
     * @param identification
     * @param validTime
     *            有效时长 毫秒
     * @param type
     * @param status
     * @return
     * @return ZKSecTicket
     */
    private ZKSecTicket createTicket(Serializable identification, long validTime, int type, int securityType,
            int status) {
        String tkId = identification.toString();
        if (validTime < 1) {
            validTime = defaultValidTime;
        }

        Long res = 0l;
        ZKSecRedisTicketBaseInfo ticketBaseInfo = new ZKSecRedisTicketBaseInfo(tkId, type, securityType, status,
                validTime);

        this.getJedisOperator().hset(ZKRedisKey.Ticket_Mapping, tkId, ticketBaseInfo);
        res = this.getJedisOperator().hset(tkId, AttrKeyName.baseInfo, ticketBaseInfo);
        this.getJedisOperator().expire(tkId, (int) (validTime / 1000));

        if (res != null) {
            logger.info("[^_^:20210816-1048-001] ZKCreate ZKSecTicket success -> {}", tkId);
        }
        else {
            logger.error("[>_<:20210816-1048-002] ZKCreate ZKSecTicket fail -> {}; validTime:{}, type:{}, status:{} ",
                    tkId, validTime, type, status);
            throw ZKSystemException.as("zk.sec.000019", "ZKCreate ZKSecTicket fail");
        }
        return new ZKSecDelegatingTicker(this, tkId);
    }

    public Map<String, Object> getTicketMapping() {
        return this.getJedisOperator().hget(ZKRedisKey.Ticket_Mapping);
    }

    /*******************************************************************************/
    /*******************************************************************************/
    /*******************************************************************************/

    /**
     * (not Javadoc)
     * <p>
     * Title: createTicket
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     * @see com.zk.security.ticket.ZKSecTicketManager#createTicket(java.lang.String)
     */
    @Override
    public ZKSecTicket createTicket(Serializable identification) {
        return createTicket(identification, defaultValidTime);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: createTicket
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @param validTime
     * @return
     */
    @Override
    public ZKSecTicket createTicket(Serializable identification, long validTime) {
        return this.createTicket(identification, validTime, ZKSecTicket.KeyType.General, 0,
                ZKSecTicket.KeyStatus.Start);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: createSecTicket
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     */
    @Override
    public ZKSecTicket createSecTicket(Serializable identification) {
        return createSecTicket(identification, ZKSecTicket.KeySecurityType.User, defaultValidTime);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: createSecTicket
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @param validTime
     * @return
     * @see com.zk.security.ticket.ZKSecTicketManager#createSecTicket(java.lang.String,
     *      long)
     */
    @Override
    public ZKSecTicket createSecTicket(Serializable identification, int securityType) {
        return createSecTicket(identification, securityType, defaultValidTime);
    }

    @Override
    public ZKSecTicket createSecTicket(Serializable identification, int securityType, long validTime) {
        return this.createTicket(identification, validTime, ZKSecTicket.KeyType.Security,
                securityType, ZKSecTicket.KeyStatus.Start);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: dropTicketByType
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param type
     * @return
     * @see com.zk.security.ticket.ZKSecTicketManager#dropTicketByType(int)
     */
    @Override
    public int dropTicketByType(int type) {
        List<ZKSecRedisTicketBaseInfo> ts = this.getJedisOperator().hValues(ZKRedisKey.Ticket_Mapping);
        int count = 0;
        for (ZKSecRedisTicketBaseInfo t : ts) {
            if(t.getType() == type) {
                this.drop(t.getTkId());
                ++count;
            }
        }
        return count;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: dropAll
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @return
     * @see com.zk.security.ticket.ZKSecTicketManager#dropAll()
     */
    @Override
    public int dropAll() {
        Set<String> tkIds = this.getJedisOperator().hkeys(ZKRedisKey.Ticket_Mapping);
        int count = 0;
        for (String tkId : tkIds) {
            this.drop(tkId);
            ++count;
        }
        return count;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: validateTickets
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @see com.zk.security.ticket.ZKSecTicketManager#validateTickets()
     */
    @Override
    public void validateTickets() {
        List<ZKSecRedisTicketBaseInfo> ts = this.getJedisOperator().hValues(ZKRedisKey.Ticket_Mapping);
        for (ZKSecRedisTicketBaseInfo t : ts) {
            if (!t.isValid()) {
                this.drop(t.getTkId());
            }
        }
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getTicket
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     * @see com.zk.security.ticket.ZKSecTicketManager#getTicket(java.lang.String)
     */
    @Override
    public ZKSecTicket getTicket(Serializable identification) {
        if (this.isValid(identification)) {
            return new ZKSecDelegatingTicker(this, identification);
        }
        return null;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: dropTicket
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param ticket
     * @return
     * @see com.zk.security.ticket.ZKSecTicketManager#dropTicket(com.zk.security.ticket.ZKSecTicket)
     */
    @Override
    public int dropTicket(ZKSecTicket ticket) {
        return this.drop(ticket.getTkId());
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: dropTicket
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     * @see com.zk.security.ticket.ZKSecTicketManager#dropTicket(java.lang.String)
     */
    @Override
    public int dropTicket(Serializable identification) {
        Long res = this.getJedisOperator().del(identification.toString());
        this.getJedisOperator().hdel(ZKRedisKey.Ticket_Mapping, identification.toString());
        return res == null ? 0 : res.intValue();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: findTickeByPrincipal
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param principal
     * @return
     * @see com.zk.security.ticket.ZKSecTicketManager#findTickeByPrincipal(com.zk.security.principal.ZKSecPrincipal)
     */
    @Override
    public <ID> List<ZKSecTicket> findTickeByPrincipal(ZKSecPrincipal<ID> principal) {
       return this.findTickeByPrincipal(principal, null);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: findTickeByPrincipal
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param principal
     * @param filterTickets
     * @return
     * @see com.zk.security.ticket.ZKSecTicketManager#findTickeByPrincipal(com.zk.security.principal.ZKSecPrincipal,
     *      java.util.List)
     */
    @Override
    public <ID> List<ZKSecTicket> findTickeByPrincipal(ZKSecPrincipal<ID> principal, List<ZKSecTicket> filterTickets) {
        List<ZKSecRedisTicketBaseInfo> tks = this.getJedisOperator().hValues(ZKRedisKey.Ticket_Mapping);
        List<ZKSecRedisTicketBaseInfo> resTks = Lists.newArrayList();
        for (ZKSecRedisTicketBaseInfo tk : tks) {
            if (tk.isValid()) {
                if (tk.getType() == ZKSecTicket.KeyType.Security && tk.getPrincipalCollection() != null) {
                    ZKSecPrincipalCollection<ID> pc = tk.getPrincipalCollection();
                    for (ZKSecPrincipal<ID> p : pc) {
                        if (p.getPkId().equals(principal.getPkId()) && (p.getType() == principal.getType())) {
                            resTks.add(tk);
                            break;
                        }
                    }
                }
            }
            else {
                this.drop(tk.getTkId());
            }
        }

        List<ZKSecTicket> res = Lists.newArrayList();
        if (filterTickets != null && !filterTickets.isEmpty()) {
            ZKSecRedisTicketBaseInfo ti = null;
            for (ZKSecRedisTicketBaseInfo tk : resTks) {
                ti = tk;
                for (ZKSecTicket ft : filterTickets) {
                    if (ft.getTkId().equals(tk.getTkId())) {
                        ti = null;
                        break;
                    }
                }
                if (ti != null) {
                    res.add(new ZKSecDelegatingTicker(this, ti.getTkId()));
                }
            }
        }
        else {
            for (ZKSecRedisTicketBaseInfo tk : resTks) {
                res.add(new ZKSecDelegatingTicker(this, tk.getTkId()));
            }
        }

        return res;
    }

    @Override
    public int getType(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        return tkBaseInfo.getType();
    }

    @Override
    public int getSecurityType(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        return tkBaseInfo.getSecurityType();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getPrincipalCollection
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#getPrincipalCollection(java.lang.String)
     */
    @Override
    public <ID> ZKSecPrincipalCollection<ID> getPrincipalCollection(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        return tkBaseInfo.getPrincipalCollection();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: setPrincipalCollection
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @param principalCollection
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#setPrincipalCollection(java.lang.String,
     *      com.zk.security.principal.pc.ZKSecPrincipalCollection)
     */
    @Override
    public <ID> void setPrincipalCollection(Serializable identification,
            ZKSecPrincipalCollection<ID> principalCollection) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        tkBaseInfo.setPrincipalCollection(principalCollection);
        this.updateLastTime(tkBaseInfo);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getStatus
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#getStatus(java.lang.String)
     */
    @Override
    public int getStatus(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        return tkBaseInfo.getStatus();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: start
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#start(java.lang.String)
     */
    @Override
    public void start(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        tkBaseInfo.setStatus(ZKSecTicket.KeyStatus.Start);
        this.updateLastTime(tkBaseInfo);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: stop
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#stop(java.lang.String)
     */
    @Override
    public void stop(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        if (tkBaseInfo != null) {
            tkBaseInfo.setStatus(ZKSecTicket.KeyStatus.Stop);
            this.updateLastTime(tkBaseInfo);
        }
        else {
            logger.info("[^_^:20220512-0954-001] 令牌[{}]不存在！", identification);
        }

    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getLastTime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#getLastTime(java.lang.String)
     */
    @Override
    public Date getLastTime(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        return tkBaseInfo.getLastTime();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: updateLastTime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#updateLastTime(java.lang.String)
     */
    @Override
    public void updateLastTime(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        this.updateLastTime(tkBaseInfo);
    }

    private void updateLastTime(ZKSecRedisTicketBaseInfo tkBaseInfo) {
        tkBaseInfo.setLastTime(new Date());
        this.getJedisOperator().hset(ZKRedisKey.Ticket_Mapping, tkBaseInfo.getTkId(), tkBaseInfo);
        this.getJedisOperator().hset(tkBaseInfo.getTkId(), AttrKeyName.baseInfo, tkBaseInfo);
        this.getJedisOperator().expire(tkBaseInfo.getTkId(), (int) (tkBaseInfo.getValidTime() / 1000));
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getValidTime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#getValidTime(java.lang.String)
     */
    @Override
    public long getValidTime(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        return tkBaseInfo.getValidTime();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: setValidTime
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @param validTime
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#setValidTime(java.lang.String,
     *      long)
     */
    @Override
    public void setValidTime(Serializable identification, long validTime) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        tkBaseInfo.setValidTime(validTime);
        this.updateLastTime(tkBaseInfo);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: isValid
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#isValid(java.lang.String)
     */
    @Override
    public boolean isValid(Serializable identification) {
        ZKSecRedisTicketBaseInfo tkBaseInfo = this.getJedisOperator().hget(identification.toString(),
                AttrKeyName.baseInfo);
        if (tkBaseInfo != null && tkBaseInfo.isValid()) {
            return true;
        }
        // 令牌过期
        logger.info("[^_^:20210816-1057-001] ZKSecTicket is expired ");
        this.drop(identification);
        return false;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: drop
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @return
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#drop(java.lang.String)
     */
    @Override
    public int drop(Serializable identification) {
        Long res = this.getJedisOperator().del(identification.toString());
        this.getJedisOperator().hdel(ZKRedisKey.Ticket_Mapping, identification.toString());
        return res.intValue();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: put
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param <V>
     * @param identification
     * @param key
     * @param value
     * @return
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#put(java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    @Override
    public <V> boolean put(Serializable identification, String key, V value) {
        return this.putValue(identification, AttrKeyName.valueKey_prefix + key, value);
    }

    public <V> boolean putValue(Serializable identification, String key, V value) {
        boolean res = this.getJedisOperator().hset(identification.toString(), key, value) != null;
        if (res) {
            this.updateLastTime(identification);
        }
        else {
            logger.error("[>_<:20220517-1130-001] Redis 令牌管理，put value 时，失败！identification:{}, key:{}, value:{} ",
                    identification, key, value);
        }
        return res;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: get
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param <V>
     * @param identification
     * @param key
     * @return
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#get(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public <V> V get(Serializable identification, String key) {
        return this.getValue(identification, AttrKeyName.valueKey_prefix + key);
    }

    private <V> V getValue(Serializable identification, String key) {
        return this.getJedisOperator().hget(identification.toString(), key);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: remove
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param identification
     * @param key
     * @return
     * @see com.zk.security.ticket.ZKSecProxyTickerManager#remove(java.lang.String,
     *      java.lang.String)
     */
    @Override
    public boolean remove(Serializable identification, String key) {
        return this.getJedisOperator().hdel(identification.toString(), key) != null;
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: destroy
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @see com.zk.core.spring.lifecycle.ZKDestroyable#destroy()
     */
    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

}
