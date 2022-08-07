/**
 * 
 */
package com.zk.file.service;
 
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zk.base.service.ZKBaseTreeService;
import com.zk.file.dao.ZKFileDirectoryDao;
import com.zk.file.entity.ZKFileDirectory;

/**
 * ZKFileDirectoryService
 * @author 
 * @version 
 */
@Service
@Transactional(readOnly = true)
public class ZKFileDirectoryService extends ZKBaseTreeService<String, ZKFileDirectory, ZKFileDirectoryDao> {

	/**
     * 执行树形查询；
     */
    public List<ZKFileDirectory> doFindTree(ZKFileDirectory fileDirectory) {
        return this.dao.findTree(fileDirectory);
    }

	/**
	 * 查询详情，包含父节点
	 */
    public ZKFileDirectory getDetail(ZKFileDirectory fileDirectory) {
        return this.dao.getDetail(fileDirectory);
    }
	
}