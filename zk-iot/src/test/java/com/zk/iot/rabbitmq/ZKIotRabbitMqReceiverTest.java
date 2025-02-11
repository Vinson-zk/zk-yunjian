/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKIotRabbitMqReceiverTest.java 
* @author Vinson 
* @Package com.zk.iot.rabbitmq 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 10, 2025 1:32:12 PM 
* @version V1.0 
*/
package com.zk.iot.rabbitmq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import junit.framework.TestCase;

/** 
* @ClassName: ZKIotRabbitMqReceiverTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKIotRabbitMqReceiverTest {

    public static interface ExchangeName {
        public static final String devReceiver = "devReceiver";
        public static final String devSender = "devSender";
    }

    public static final BuiltinExchangeType ExchangeType = BuiltinExchangeType.DIRECT;

    static final List<Connection> connectionList = new ArrayList<>();

    static final List<Channel> channelList = new ArrayList<>();

    static final List<String> routeKeys = Arrays.asList(
            "hf9527".toLowerCase()
//            "YLWX22440019".toLowerCase(), 
//            "000000000002".toLowerCase() //
    );

    public static void receiverMsg(ConnectionFactory factory, String... ExchangeNames) {
        try {
            Connection connection = factory.newConnection("Vinson9527-Connection");
            connectionList.add(connection);
            for (String ExchangeName : ExchangeNames) {
                receiverMsgByExchangeName(connection, ExchangeName);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    public static void receiverMsgByExchangeName(Connection connection, String ExchangeName) {
        try {
            Channel channel = connection.createChannel();
            channelList.add(channel);

            // // 声明 Exchange
            channel.exchangeDeclare(ExchangeName, ExchangeType, true);

            for (String routeKey : routeKeys) {
                String QUEUE_NAME = "Vinson9527[" + ExchangeName + "][" + routeKey + "]";
                // 声明（创建）队列
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);

                // 队列绑定 Exchange
                channel.queueBind(QUEUE_NAME, ExchangeName, routeKey);

                // 监听消息
                channel.basicConsume(QUEUE_NAME, true, new MyAmqpConsumer(channel));
                System.out.println("[^_^:20250110-1352-001] 开始监听消息[QUEUE_NAME: " + QUEUE_NAME + "][routeKey: "
                        + routeKey + "]... ...");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        for (Connection item : connectionList) {
            item.close();
        }
        for (Channel item : channelList) {
            item.close();
        }
    }

}
