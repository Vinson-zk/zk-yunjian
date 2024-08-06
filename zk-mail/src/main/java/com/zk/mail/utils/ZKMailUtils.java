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
* @Title: ZKMailUtils.java 
* @author Vinson 
* @Package com.zk.mail.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date May 24, 2022 12:05:37 AM 
* @version V1.0 
*/
package com.zk.mail.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.mail.common.ZKMailAuthenticator;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

/**
 * @ClassName: ZKMailUtils
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKMailUtils {

    protected static Logger log = LogManager.getLogger(ZKMailUtils.class);

    /**
     * 发送邮件
     *
     * @Title: sendMail
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 25, 2022 2:06:56 AM
     * @param recipientMailAddr
     *            收件箱
     * @param mailProperties
     *            发送属性
     * @param mailAuthenticator
     *            发送服务器认证配置
     * @param sendAddress
     *            发件邮箱，默认使用 使送账号
     * @param sendName
     *            邮件名称
     * @param subject
     *            发送主题
     * @param content
     *            发送内容
     * @param async
     *            是否异步，true-异步；false-同步
     * @param attachments
     *            邮件附件
     * @return
     * @return boolean
     */
    public static boolean sendMail(String recipientMailAddr, Properties mailProperties,
            ZKMailAuthenticator mailAuthenticator, String sendAddress, String sendName, String subject, String content,
            boolean async, DataSource... attachments) {

        boolean ifSuccess = false;
        try {
            if (ZKStringUtils.isEmpty(sendAddress)) {
                sendAddress = mailAuthenticator.getAccount();
            }
            MimeMessage message = makeMailMsg(recipientMailAddr, mailProperties, mailAuthenticator, sendAddress,
                    sendName, subject, content, attachments);
            if (async) {
                try {
                    ifSuccess = true;
                    // 起一个线程，发送邮件
//                        (new ARSendMailThread(message)).start();
                    mailSendThreadPool.execute(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                // send email
                                log.info("[^_^:20200603-1207-001] send mail ... ... {}",
                                        ZKJsonUtils.toJsonStr(message.getFrom()));
                                Transport.send(message);
                                log.info("[^_^:20200603-1207-002] send mail succsee!}");
                            }
                            catch(Exception e) {
                                log.error("[>_<:20200603-1207-003] send mail fail!, msg:}", e);
                                throw ZKExceptionsUtils.unchecked(e);
                            }
                        }
                    });
                }
                catch(Exception e) {
                    e.printStackTrace();
                    ifSuccess = false;
                }
            }
            else {
                Transport.send(message);
                ifSuccess = true;
            }

        }
        catch(Exception e) {
            log.error("[>_<:20170820-1234-002] send mail fail -> {sendAddress:{}, sendName:{}, recipientMailAddr:{}}",
                    sendAddress, sendName, recipientMailAddr);
//            e.printStackTrace();
            throw ZKExceptionsUtils.unchecked(e);
        }
        return ifSuccess;
    }


    /**
     * 制作邮件最终 message
     *
     * @Title: makeMailMsg
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 25, 2022 1:57:51 AM
     * @param recipientMailAddr
     *            收件箱
     * @param mailProperties
     *            发送属性
     * @param mailAuthenticator
     *            发送服务器认证配置
     * @param sendAddress
     *            发件邮箱
     * @param sendName
     *            邮件名称
     * @param subject
     *            发送主题
     * @param content
     *            发送内容
     * @param attachments
     *            邮件附件
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @return MimeMessage
     */
    protected static MimeMessage makeMailMsg(String recipientMailAddr, Properties mailProperties,
            ZKMailAuthenticator mailAuthenticator, String sendAddress, String sendName, String subject, String content,
            DataSource... attachments) throws MessagingException, UnsupportedEncodingException {

        Session session = Session.getInstance(mailProperties, mailAuthenticator);
        MimeMessage message = new MimeMessage(session);

        message.setSubject(subject);
        // set send date
        message.setSentDate(new Date());

        Address fromAddress = new InternetAddress(sendAddress, sendName);

        // set from address
        message.setFrom(fromAddress);
        // set to address
        Address toAddress = new InternetAddress(recipientMailAddr);
        message.addRecipient(Message.RecipientType.TO, toAddress);

        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setContent(content, "text/html;charset=utf-8");

        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        if (attachments != null && attachments.length > 0) {
            for (DataSource ads : attachments) {
                if (ads == null) {
                    continue;
                }
                BodyPart attachmentBodyPart = new MimeBodyPart();
//                DataSource source = new FileDataSource(f);
                attachmentBodyPart.setDataHandler(new DataHandler(ads));
//                attachmentBodyPart.setFileName(MimeUtility.encodeWord(ads.getName()));
                attachmentBodyPart.setFileName(ads.getName());
                mp.addBodyPart(attachmentBodyPart);
            }
        }

        message.setContent(mp);
        return message;
    }

    // 邮件发送线程池，设置大小为1，因为有些邮件服务器，不支持多线程同时发送邮件
    static ExecutorService mailSendThreadPool = Executors.newFixedThreadPool(1);

    public static ExecutorService getMailSendThreadPool() {
        return mailSendThreadPool;
    }

    /**
     * 等待线程结束
     *
     * @Title: waitFinish
     * @Description: TODO(simple description this method what to do.)
     * @author zhenx
     * @date 2020年6月3日 下午12:26:02
     * @param maxWait
     *            最多等待多少秒
     * @return
     * @throws InterruptedException
     */
    public static boolean waitFinish(int maxWait) throws InterruptedException {

//        arMailSendThreadPool.shutdown();
//        arMailSendThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        mailSendThreadPool.shutdown();
        int i = 0;
        while (!mailSendThreadPool.isTerminated() || i < maxWait) {
            ++i;
            Thread.sleep(1000);
        }

        if (mailSendThreadPool.isTerminated()) {
            mailSendThreadPool = Executors.newFixedThreadPool(1);
            return true;
        }
        else {
            mailSendThreadPool.shutdownNow();
            mailSendThreadPool = Executors.newFixedThreadPool(1);
            return false;
        }
    }

//    /**
//     * 发送邮件线程
//     */
//    public static class ARSendMailThread extends Thread {
//
//        public final static List<ARSendMailThread> tPool = new ArrayList<>();
//
//        private MimeMessage message;
//
//        public ARSendMailThread(MimeMessage message) {
//            super(ARSendMailThread.class.getSimpleName());
//            this.message = message;
//            tPool.add(this);
//        }
//
//        @Override
//        public void run() {
//            try {
//                // send email
//                log.info("[^_^:20170911-0911-000] send mail ... ... {}", JsonUtils.writeObjectJson(message.getFrom()));
//                Transport.send(message);
//                log.info("[^_^:20170911-0911-001] send mail succsee!}");
//            }
//            catch(Exception e) {
//                log.error("[>_<:20170911-0911-002] send mail fail!, msg:}", e);
//                Exceptions.unchecked(e);
//            } finally {
//                tPool.remove(this);
//            }
//        }
//    }
    /*****************************************************/
    /*** 辅助创建一些发送的相关对象方法 **/
    /*****************************************************/
    // 发送邮件时的认证对象
    public static ZKMailAuthenticator getMailAuthenticator(String account, String password) {
        return new ZKMailAuthenticator(account, password);
    }

    // 发送邮件时，向服务器指定的一些发送属性配置
    public static Properties getProperties(String host, String port, String type, boolean validate) {

        Properties properties = new Properties();

        // 网络代理
        // properties.put("mail.smtp.proxy.host", host);
        // properties.put("mail.smtp.proxy.port", port);
        // properties.put("mail.smtp.proxy.user", host);
        // properties.put("mail.smtp.proxy.password", port);
        // socke 代理
        // properties.put("mail.smtp.socks.host", host);
        // properties.put("mail.smtp.socks.port", port);

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", validate ? "true" : "false");

        if ("ssl".equalsIgnoreCase(type.toLowerCase())) {
            properties.put("mail.smtp.socketFactory.port", port);
            properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.put("mail.smtp.ssl.enable", "true");

            if (host.indexOf("gmail") != -1) {
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.EnableSSL.enable", "true");
                properties.setProperty("mail.smtp.socketFactory.fallback", "false");
            }
        }
        else if ("tls".equalsIgnoreCase(type.toLowerCase())) {
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.checkserveridentity", "false");
            properties.put("mail.smtp.ssl.trust", host);
            properties.put("mail.smtp.port", port);
        }
        else {

        }

        return properties;
    }

}
