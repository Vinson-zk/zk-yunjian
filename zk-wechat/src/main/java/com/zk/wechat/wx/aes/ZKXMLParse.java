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
* @Title: ZKXMLParse.java 
* @author Vinson 
* @Package com.zk.wechat.wx.aes 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 18, 2021 2:19:21 PM 
* @version V1.0 
*/
package com.zk.wechat.wx.aes;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.qq.weixin.mp.aes.AesException;
import com.zk.core.utils.ZKXmlUtils;

/**
 * 重写 com.qq.weixin.mp.aes.XMLParse
 * 
 * 解决第三方平台解密时因没有 ToUserName 属性报空指针异常的原因；
 * 
 * NodeList nodelist2 = root.getElementsByTagName("ToUserName");
 * 
 * result[2] = nodelist2.item(0).getTextContent();
 * 
 * @ClassName: ZKXMLParse
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKXMLParse {

    /**
     * 提取出xml数据包中的加密消息
     * 
     * @param xmltext
     *            待提取的xml字符串
     * @return 提取出的加密消息字符串
     * @throws AesException
     */
    public static Object[] extract(String xmltext) throws ZKAesException {
        Object[] result = new Object[3];
        try {
            Document document = ZKXmlUtils.getDocument(xmltext);
            Element root = document.getRootElement();
            List<Element> nodelist1 = root.elements("Encrypt");
            List<Element> nodelist2 = root.elements("ToUserName");
            result[0] = 0;
            result[1] = nodelist1.get(0).getTextTrim();
//            result[2] = nodelist2.item(0).getTextContent();
            result[2] = (nodelist2 == null || nodelist2.size() < 1) ? "" : nodelist2.get(0).getTextTrim();
            return result;

//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            DocumentBuilder db = dbf.newDocumentBuilder();
//            StringReader sr = new StringReader(xmltext);
//            InputSource is = new InputSource(sr);
//            Document document = db.parse(is);
//
//            Element root = document.getDocumentElement();
//            NodeList nodelist1 = root.getElementsByTagName("Encrypt");
//            NodeList nodelist2 = root.getElementsByTagName("ToUserName");
//            result[0] = 0;
//            result[1] = nodelist1.item(0).getTextContent();
////            result[2] = nodelist2.item(0).getTextContent();
//            result[2] = (nodelist2 == null || nodelist2.getLength() < 1) ? "" : nodelist2.item(0).getTextContent();
//            return result;
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new ZKAesException(AesException.ParseXmlError);
        }
    }

    /**
     * 生成xml消息
     * 
     * @param encrypt
     *            加密后的消息密文
     * @param signature
     *            安全签名
     * @param timestamp
     *            时间戳
     * @param nonce
     *            随机字符串
     * @return 生成的xml字符串
     */
    public static String generate(String encrypt, String signature, String timestamp, String nonce) {
        String format = "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
                + "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n" + "<TimeStamp>%3$s</TimeStamp>\n"
                + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
        return String.format(format, encrypt, signature, timestamp, nonce);

    }

}
