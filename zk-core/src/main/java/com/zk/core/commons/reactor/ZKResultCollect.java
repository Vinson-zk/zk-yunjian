/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKResultCollect.java 
* @author Vinson 
* @Package com.zk.core.commons.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 13, 2024 3:52:50 PM 
* @version V1.0 
*/
package com.zk.core.commons.reactor;

import java.util.ArrayList;
import java.util.List;

import reactor.core.publisher.FluxSink;
import reactor.core.publisher.MonoSink;

/**
 * @ClassName: ZKResultCollect
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKResultCollect<T> {

    final List<ZKResultConllectSubscribe<T>> rcSubscribeList = new ArrayList<ZKResultConllectSubscribe<T>>();

    public ZKResultCollect(int resultCount, MonoSink<List<T>> monoSink) {
        this.rcSubscribeList.add(new ZKResultConllectSubscribeMono<T>(monoSink));
        this.resultCount = resultCount;
    }

    public <R> ZKResultCollect(int resultCount, MonoSink<R> monoSink, ZKResultFormat<R, T> format) {
        this.rcSubscribeList.add(new ZKResultConllectSubscribeMonoFormat<R, T>(monoSink, format));
        this.resultCount = resultCount;
    }

    public ZKResultCollect(int resultCount, FluxSink<List<T>> fluxSink) {
        this.rcSubscribeList.add(new ZKResultConllectSubscribeFlux<T>(fluxSink));
        this.resultCount = resultCount;
    }

    public <R> ZKResultCollect(int resultCount, FluxSink<R> fluxSink, ZKResultFormat<R, T> format) {
        this.rcSubscribeList.add(new ZKResultConllectSubscribeFluxFormat<R, T>(fluxSink, format));
        this.resultCount = resultCount;
    }

    // --------------------
    public void add(MonoSink<List<T>> monoSink) {
        this.rcSubscribeList.add(new ZKResultConllectSubscribeMono<T>(monoSink));
    }

    public <R> void add(MonoSink<R> monoSink, ZKResultFormat<R, T> format) {
        this.rcSubscribeList.add(new ZKResultConllectSubscribeMonoFormat<R, T>(monoSink, format));
    }

    public void add(FluxSink<List<T>> fluxSink) {
        this.rcSubscribeList.add(new ZKResultConllectSubscribeFlux<T>(fluxSink));
    }

    public <R> void add(FluxSink<R> fluxSink, ZKResultFormat<R, T> format) {
        this.rcSubscribeList.add(new ZKResultConllectSubscribeFluxFormat<R, T>(fluxSink, format));
    }

    // ===============================================
    private final List<ZKReactorRelational<T>> reactorRelationalList = new ArrayList<ZKReactorRelational<T>>();

    final List<T> res = new ArrayList<T>();

    // 设置收集结果的个数
    int resultCount = Integer.MAX_VALUE;

    public List<T> getResult() {
        return this.res;
    }

    /**
     * @return resultCount sa
     */
    public int getResultCount() {
        return resultCount;
    }

    /**
     * @param resultCount
     *            the resultCount to set
     */
    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

//  public void add(Disposable disposable) {
//      this.disposableList.add(disposable);
//  }

    public ZKReactorRelational<T> createRelational() {
        ZKReactorRelational<T> rr = new ZKReactorRelational<T>(this);
        this.reactorRelationalList.add(rr);
        return rr;
    }

    public void accept(T value) {
        this.res.add(value);
    }

    public void accept(List<T> values) {
        this.res.addAll(values);
    }

    public boolean isFinsh() {
//        for (ZKReactorRelational<T> item : reactorRelationalList) {
//            if (item.getDisposable() == null) {
//                System.out.println("--- 11 null ");
//            }
//            else {
//                System.out.println("---  22 " + item.getDisposable().isDisposed());
//            }
//        }
        if (reactorRelationalList.size() < this.resultCount) {
            return false;
        }
        for (ZKReactorRelational<T> item : reactorRelationalList) {
            if (!item.isFinsh()) {
                return false;
            }
        }
        return true;
    }

    public void doSubscribe() {
        if (this.isFinsh()) {
            this.rcSubscribeList.forEach(action -> {
                action.doSubscribe(this.res);
            });
        }
    }

//    public void doSubscribe(MonoSink<List<T>> monoSink) {
//        if (this.isFinsh()) {
//            monoSink.success(this.res);
//        }
//    }
//
//    public <R> void doSubscribe(MonoSink<R> monoSink, ZKResultFormat<R, T> format) {
//        if (this.isFinsh()) {
//            monoSink.success(format.format(this.res));
//        }
//    }
//
//    public void doSubscribe(FluxSink<List<T>> fluxSink) {
//        if (this.isFinsh()) {
//            fluxSink.next(this.getResult());
//        }
//    }
//
//    public <R> void doSubscribe(FluxSink<R> fluxSink, ZKResultFormat<R, T> format) {
//        if (this.isFinsh()) {
//            fluxSink.next(format.format(this.res));
//        }
//    }

}
