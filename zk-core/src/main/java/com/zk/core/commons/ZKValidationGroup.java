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
* @Title: ZKValidationGroup.java 
* @author Vinson 
* @Package com.zk.core.commons 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 3, 2023 10:47:41 AM 
* @version V1.0 
*/
package com.zk.core.commons;
/** 
* @ClassName: ZKValidationGroup 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKValidationGroup {

    // 自定义校验模式
    public interface CustomModel {

    }

    /**
     * 新增校验
     * 
     * @ClassName: Insert
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public interface Insert {

    }

    /**
     * 修改校验
     * 
     * @ClassName: Insert
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public interface Update {

    }

    /**
     * 审核内容校验
     * 
     * @ClassName: Audit
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public interface Audit {

    }

}
