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
 * @Title: ZKValidateCodeUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 4:16:12 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/** 
* @ClassName: ZKValidateCodeUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKValidateCodeUtils {

    protected static Logger logger = LogManager.getLogger(ZKValidateCodeUtils.class);

    /**
     * 生成一个指定长度为6位的 数字验证码字符串
     * 
     * @Title: genVerifyNumCode
     * @Description: TODO(simple description this method what to do.)
     * @author zhenx
     * @date Sep 15, 2017 8:22:55 AM
     * @return
     * @return String
     */
    public static String genVerifyNumCode() {
        return genVerifyNumCode(6);
    }

    /**
     * 生成一个指定长度的 数字验证码字符串
     * 
     * @Title: genVerifyCode
     * @Description: TODO(simple description this method what to do.)
     * @author zhenx
     * @date Sep 14, 2017 11:44:22 PM
     * @param length
     * @return
     * @return String
     */
    public static String genVerifyNumCode(int length) {
        StringBuffer code = new StringBuffer("");
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int a = random.nextInt(10);
            code.append(a);
        }
        return code.toString();
    }

    // 生成验证字符源
    private static final char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    /**
     * 按验证字符源数组，生成一个指定长度的验证字符串
     * 
     * @Title: genVerifyCode
     * @Description: TODO(simple description this method what to do.)
     * @author zhenx
     * @date Sep 15, 2017 8:23:41 AM
     * @param length
     * @return
     * @return String
     */
    public static String genVerifyCode(int length) {
        return genVerifyCode(codeSequence, length);
    }

    /**
     * 按验证字符源数组，生成一个指定长度的验证字符串
     * 
     * @Title: genVerifyCode
     * @Description: TODO(simple description this method what to do.)
     * @author zhenx
     * @date Sep 15, 2017 8:24:41 AM
     * @param codeSequence
     * @param length
     * @return
     * @return String
     */
    public static String genVerifyCode(char[] codeSequence, int length) {
        StringBuffer code = new StringBuffer("");
        for (int i = 0; i < length; i++) {
            Random random = new Random();
            int a = random.nextInt(codeSequence.length);
            code.append(codeSequence[a]);
        }
        return code.toString();
    }

    /**
     * 
     *
     * @Title: verifyCode
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2018年4月2日 上午11:08:22
     * @param c1
     * @param c2
     * @param type
     *            0 区分大小写；1 不区分大小写
     * @return boolean true-相同；false-不同
     * @return
     */
    public static boolean checkVerifyCode(Object c1, Object c2, int type) {
        if (type == 0) {
            // 区分大小写
            if (c1 != null && c2 != null) {
                if (c1.toString().equals(c2.toString())) {
                    return true;
                }
            }
        }
        else {
            // 不区分大小写
            if (c1 != null && c2 != null) {
                if (c1.toString().toLowerCase().equals(c2.toString().toLowerCase())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证码图片
     * 
     * @ClassName: ZKValidateCodeImgUtils
     * @Description: TODO(simple description this class what to do.)
     * @author Vinson
     * @version 1.0
     */
    public static class ZKImgUtils {

        protected static Logger logger = LogManager.getLogger(ZKImgUtils.class);

        public static final int width = 120;

        public static final int height = 30;

        public static final int lineCount = 10;

        public static BufferedImage genValidateCodeImg() {
            return genValidateCodeImg(width, height, lineCount, ZKValidateCodeUtils.genVerifyCode(5));
        }

        public static BufferedImage genValidateCodeImg(String code) {
            return genValidateCodeImg(width, height, lineCount, code, null, null);
        }

        public static BufferedImage genValidateCodeImg(String code, String defaultFontStr, File fontFile) {
            return genValidateCodeImg(width, height, lineCount, code, defaultFontStr, fontFile);
        }

        public static BufferedImage genValidateCodeImg(int width, int height, String code) {
            return genValidateCodeImg(width, height, lineCount, code, null, null);
        }

        public static BufferedImage genValidateCodeImg(int width, int height, String code, String defaultFontStr,
                File fontFile) {
            return genValidateCodeImg(width, height, lineCount, code, defaultFontStr, fontFile);
        }

        public static BufferedImage genValidateCodeImg(int width, int height, int lineCount, String code) {
            return genValidateCodeImg(width, height, lineCount, code, null, null);
        }

        public static BufferedImage genValidateCodeImg(int width, int height, int lineCount, String code,
                String defaultFontStr, File fontFile) {
            return genValidateCodeImg(width, height, lineCount, code.toCharArray(), defaultFontStr, fontFile);
        }

        /**
         * 生成一张图片 BufferedImage
         *
         * @Title: genValidateCodeImg
         * @Description: TODO(simple description this method what to do.)
         * @author Vinson
         * @date Oct 15, 2019 5:06:33 PM
         * @param width
         * @param height
         * @param lineCount
         * @param code
         * @param defaultFontStr
         *            默认字体，当 fontFile 不存在时，会生效；为 null 或 空 时，取 Arial 为字体
         * @param fontFile
         *            指定字体文件；不存在是根据
         * @return
         * @return BufferedImage
         */
        public static BufferedImage genValidateCodeImg(int width, int height, int lineCount, char[] code,
                String defaultFontStr, File fontFile) {
            int x = 0, fontHeight = 0, codeY = 0;
            int red = 0, green = 0, blue = 0;

            x = width / (code.length + 2);
            fontHeight = height - 2;
            codeY = height - 4;

            BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = buffImg.createGraphics();

            Random random = new Random();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            // 可配置 验证码在图片中字体内容，配置 key 为 zk.validate.code.font
            Font font = getFontByHeight(fontHeight, defaultFontStr, fontFile);
            g.setFont(font);

            for (int i = 0; i < lineCount; i++) {
                int xs = random.nextInt(width);
                int ys = random.nextInt(height);
                int xe = xs + random.nextInt(width / 8);
                int ye = ys + random.nextInt(height / 8);
                red = random.nextInt(255);
                green = random.nextInt(255);
                blue = random.nextInt(255);
                g.setColor(new Color(red, green, blue));
                g.drawLine(xs, ys, xe, ye);
            }

            for (int i = 0; i < code.length; i++) {
                String strRand = String.valueOf(code[i]);
                red = random.nextInt(255);
                green = random.nextInt(255);
                blue = random.nextInt(255);
                g.setColor(new Color(red, green, blue));
                g.drawString(strRand, (i + 1) * x, codeY);
            }
            return buffImg;
        }

        /**
         * 输出，打印一张图片
         *
         * @Title: write
         * @Description: TODO(simple description this method what to do.)
         * @author Vinson
         * @date Oct 15, 2019 3:45:15 PM
         * @param img
         * @param os
         *            输出打印流，方法中不会关闭输出方法流，谁传入，谁去管理；
         * @throws IOException
         * @return void
         */
        public static void write(BufferedImage img, OutputStream os) throws IOException {
            ImageIO.write(img, "png", os);
            os.flush();
        }

        /**
         * 验证码在图片中字体
         *
         * @Title: getFontByHeight
         * @Description: TODO(simple description this method what to do.)
         * @author Vinson
         * @date Oct 15, 2019 4:07:08 PM
         * @param fontHeight
         * @return
         * @return Font
         */
        public static Font getFontByHeight(int fontHeight, String defaultFontStr, File fontFile) {
            try {
                if (fontFile == null) {
                    defaultFontStr = ZKStringUtils.isEmpty(defaultFontStr) ? "Arial" : defaultFontStr;
                    return new Font(defaultFontStr, Font.PLAIN, fontHeight);
                }
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(fontFile);
                    Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fis);
                    return baseFont.deriveFont(Font.PLAIN, fontHeight);
                } finally {
                    ZKStreamUtils.closeStream(fis);
                }
            }
            catch(Exception e) {
                logger.error("[>_<:20191015-1549-001] 生成字体: {fontHeight:{}, fontStr:{}, fontFile:{}} 失败；返回字体：Arial。",
                        fontHeight, defaultFontStr, (fontFile == null ? fontFile : fontFile.getAbsolutePath()));
                e.printStackTrace();
                return new Font("Arial", Font.PLAIN, fontHeight);
            }
        }

    }

}
