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
* @Title: ZKTestDoMain.java 
* @author Vinson 
* @Package com.zk.test 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 9, 2021 8:10:51 AM 
* @version V1.0 
*/
package com.zk.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** 
* @ClassName: ZKTestDoMain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKTestDoMain {

    public static void main(String[] args) {
        try {

//            String s = "0000013701234567FFFFFF111102000111091C0A0F0168";
//            byte[] sb = null;
//            sb = ZKEncodingUtils.decodeHex(s);
//            System.out.println("--- 1 " + ZKEncodingUtils.encodeHex(sb));
//            byte[] seb = ZKEncryptUtils.md5Encode(sb);
//            // C4 7D 33 4F A4 D4 E2 5E BA EB 0A D4 0F 7E DC B5
//            System.out.println("--- 2 " + ZKEncodingUtils.encodeHex(seb));
//
//            byte[] reb = new byte[8];
//            for (int i = 0; i < seb.length;) {
//                reb[i / 2] = seb[i];
//                ++i;
//                ++i;
//            }
//            System.out.println("--- 3 " + ZKEncodingUtils.encodeHex(reb));
//            byte[] rb = new byte[reb.length + sb.length];
//            int index = 0;
//            for (byte b : reb) {
//                rb[index] = (byte) ~b;
//                ++index;
//            }
//            for (byte b : sb) {
//                rb[index] = b;
//                ++index;
//            }
//            System.out.println("--- 4 " + ZKEncodingUtils.encodeHex(rb));
//
//            System.out.println("---------------------------------------");
//
//            String key = "0123456789012345";
//            String saes = "3BCC5B1D45F5F0230000013701234567FFFFFF111102000111091C0A0F0168";
//            byte[] sbaes = ZKEncodingUtils.decodeHex(saes);
//            System.out.println("--- 2-1 " + ZKEncodingUtils.encodeHex(sbaes));
//            byte[] ebaes = ZKEncryptAesUtils.encrypt(sbaes, key.getBytes());
//            System.out.println("--- 2-2 " + ZKEncodingUtils.encodeHex(ebaes));

            testStream();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    public static void testStream() {
        List<String> strs = new ArrayList<>();
        strs.add("1");
        strs.add("2");
        strs.add("3");

        Optional<String> s = strs.stream().filter(item->item.equals("2")).findFirst();
        System.out.println("[^_^: 20220915-1621-001] " + s.get());
    }

}
