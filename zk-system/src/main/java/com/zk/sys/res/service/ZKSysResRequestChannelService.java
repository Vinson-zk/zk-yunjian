/**
 * 
 */
package com.zk.sys.res.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseService;
import com.zk.sys.res.entity.ZKSysResRequestChannel;
import com.zk.sys.res.dao.ZKSysResRequestChannelDao;

/**
 * ZKSysResRequestChannelService
 * 
 * @author
 * @version
 */
@Service
@Transactional(readOnly = true)
public class ZKSysResRequestChannelService
    extends ZKBaseService<String, ZKSysResRequestChannel, ZKSysResRequestChannelDao> {

}