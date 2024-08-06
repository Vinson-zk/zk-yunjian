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
 * @Title: ZKIdUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:25:52 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** 
* @ClassName: ZKIdUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKIdUtils {

    private static final long twepoch = 1288834974657L;

    private static long sequence = 0;

    private static final long workerIdBits = 4L;

//    private static final long maxMachine = 2L;
//
//    private static final long maxThread = 2L;

    public static final long maxWorkerId = -1L ^ -1L << workerIdBits;

    // private Long maxWorkerId;

    private static final long sequenceBits = 9L;

    private static final long workerIdShift = sequenceBits;

    private static final long timestampLeftShift = sequenceBits + maxWorkerId;

    public static final long sequenceMask = -1L ^ -1L << sequenceBits;

    private static long lastTimestamp = -1L;

    private static List<Long> tidArray = new ArrayList<Long>();

    /***
     * 生成线程的编号,tid 非空,index 空时为生成编号,tid 非空并且index 非空时为归还编号
     *
     * @Title: threadId
     * @Description:
     * @author Wyman liu
     * @date Apr 14, 2017 3:49:50 PM
     * @param tid
     *            线程id
     * @param index
     *            编号
     * @return int
     */
    public synchronized static int threadId(Long tid, Integer index) {
        if (index != null && (index < 0 || index > 99)) {
            throw new RuntimeException("index 应大于等于0 小于等于 99");
        }
        if (tid != null && index == null) {
            int i = 0;
            if (tidArray.indexOf(tid) >= 0) {
                return tidArray.indexOf(tid);
            }
            for (i = 0; i < tidArray.size(); i++) {

                if (tidArray.get(i) == null || tidArray.get(i) == -1) {
                    tidArray.set(i, tid);
                    return i;
                }
            }
            if (tidArray.size() < 99) {
                tidArray.add(tid);
                return tidArray.size() - 1;
            }
            if (i == tidArray.size()) {
                throw new RuntimeException("线程编号满了");
            }
        }
        else if (tid == null) {
            throw new RuntimeException("tid 不能为空");
        }
        else {

            if (tidArray.get(index).longValue() != tid.longValue()) {
                throw new RuntimeException("tid 与 index 不匹配");
            }
            tidArray.set(index, -1L);
        }
        return -1;
    }

    public static synchronized long nextId(long workerId) {

        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(
                    String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
        }

        long timestamp = timeGen();
        if (lastTimestamp == timestamp) {

            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        }
        else {
            sequence = (0);
        }
        if (timestamp < lastTimestamp) {
            try {
                throw new Exception(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds",
                        lastTimestamp - timestamp));
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        lastTimestamp = timestamp;
        long nextId = ((timestamp - twepoch << timestampLeftShift)) | (workerId << workerIdShift) | (sequence);

        return nextId;
    }

    private static long tilNextMillis(final long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private static long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {

        System.out.println(510 & 512);

    }

    /**
     * 生成ID
     *
     * @Title: genId
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Aug 8, 2019 4:10:15 PM
     * @return
     * @return Object
     */
    public static Object genId() {
        return genLongStringId();
    }

    /**
     * 自动取种子生成ID
     * 
     * @return
     */
    public static Long genLongId() {
        return nextId(getSeed());
    }

    public static String genLongStringId() {
        return String.valueOf(genLongId());
    }

    /**
     * 自动取种子生成ID
     * 
     * @return
     */
    public static String genStringId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 取种子
     * 
     * @return
     */
    private static Long getSeed() {
        return 1L;
    }

}
