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
* @Title: MyAmqpConsumer.java 
* @author Vinson 
* @Package com.zk.iot.rabbitmq 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 10, 2025 3:59:41 PM 
* @version V1.0 
*/
package com.zk.iot.rabbitmq;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

/** 
* @ClassName: MyAmqpConsumer 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class MyAmqpConsumer extends DefaultConsumer {

    public MyAmqpConsumer(Channel channel) {
        super(channel); // TODO Auto-generated constructor stub
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        System.out.println(
                "[^_^:20250110-1147-001] MyAmqpConsumer.handleDelivery -----------------------------------------------");
        System.out.println(
                "[^_^:20250110-1147-001] MyAmqpConsumer.handleDelivery -----------------------------------------------");
        System.out.println("[^_^:20250110-1147-001] consumerTag: " + consumerTag);

        String exchange = envelope.getExchange();
        String routingKey = envelope.getRoutingKey();
        long deliveryTag = envelope.getDeliveryTag();
        String contentType = properties.getContentType();
        String contentEncoding = properties.getContentEncoding();

        System.out.println("[^_^:20250110-1147-001] exchange: " + exchange);
        System.out.println("[^_^:20250110-1147-001] routingKey: " + routingKey);
        System.out.println("[^_^:20250110-1147-001] deliveryTag: " + deliveryTag);
        System.out.println("[^_^:20250110-1147-001] contentType: " + contentType);
        System.out.println("[^_^:20250110-1147-001] contentEncoding: " + contentEncoding);

        String msg = new String(body, "UTF-8");
        System.out.println("[^_^:20250110-1152-001] msg: " + msg);
    }
}




