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
 * @Title: ZKFileUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 4:27:28 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipOutputStream;

import org.junit.Test;

import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKFileType;

import junit.framework.TestCase;

/** 
* @ClassName: ZKFileUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileUtilsTest {

    private static final String writeFileRootPath = "target/testFileUtilsDir" + File.separator;

    private static final String sourceFileRootPath = "src/test/resources/testFile" + File.separator;

    @Test
    public void test() {
        try {

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testNewFileName() {
        ZKContentType ct = null;
        String fileName = null, newFileName = null;

        Pattern pattern = null;
        Matcher matcher = null;

        fileName = "test.txt";
        ct = ZKContentType.DOCX;

        newFileName = ZKFileUtils.newFileName(ct, fileName);
        System.out.println("[^_^:20240223-2357-001-1] newFileName: " + newFileName);
        pattern = Pattern.compile("^(.*)\\.docx$");
        matcher = pattern.matcher(newFileName);
        TestCase.assertEquals(true, matcher.matches());
        newFileName = ZKFileUtils.newFileName(null, fileName);
        System.out.println("[^_^:20240223-2357-001-2] newFileName: " + newFileName);
        pattern = Pattern.compile("^(.*)\\.txt$");
        matcher = pattern.matcher(newFileName);
        TestCase.assertEquals(true, matcher.matches());

    }

    // 测试 创建文件
    @Test
    public void testCreateFile() {
        try {
            String path = ZKFileUtilsTest.writeFileRootPath + UUID.randomUUID().toString() + File.separator
                    + UUID.randomUUID().toString();
            String fileName;
            File file;
            byte[] zk;
            String content = "测试";

            // 目录不存 文件不存在
            fileName = "fileTest";
            file = ZKFileUtils.createFile(path, fileName, false);
            TestCase.assertNotNull(file);
            ZKFileUtils.writeFile(content.getBytes(), file, false);
            // 目录存在 文件不存在
            fileName = "testttt";
            file = ZKFileUtils.createFile(path, fileName, false);
            TestCase.assertNotNull(file);

            // 目录存在 文件存在，覆盖
            fileName = "fileTest";
            file = ZKFileUtils.createFile(path, fileName, true);
            System.out.println("===== " + file.getCanonicalPath());
            System.out.println("===== " + file.getAbsolutePath());
            System.out.println("===== " + file.getName());
            System.out.println("===== " + file.getPath());
            zk = ZKFileUtils.readFile(new File(path + File.separator + fileName));
            TestCase.assertEquals(0, zk.length);
            ZKFileUtils.writeFile(content.getBytes(), file, false);
            file = ZKFileUtils.createFile(path, fileName, false);
            ZKFileUtils.writeFile(content.getBytes(), file, false);
            zk = ZKFileUtils.readFile(new File(path + File.separator + fileName));
            TestCase.assertEquals(content, new String(zk));
            zk = ZKFileUtils.readFile(new File(path + File.separator + fileName + "(1)"));
            TestCase.assertEquals(content, new String(zk));

            // 测试加后缀名
            fileName = "fileTest.test";
            file = ZKFileUtils.createFile(path, fileName, false);
            ZKFileUtils.writeFile(content.getBytes(), file, false);
            file = ZKFileUtils.createFile(path, fileName, false);
            ZKFileUtils.writeFile(content.getBytes(), file, false);
            zk = ZKFileUtils.readFile(new File(path + File.separator + fileName));
            TestCase.assertEquals(content, new String(zk));
            zk = ZKFileUtils.readFile(new File(path + File.separator + "fileTest(1).test"));
            TestCase.assertEquals(content, new String(zk));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 测试读文件
    @Test
    public void testReadFile() {
        try {
            String path = ZKFileUtilsTest.writeFileRootPath + "readTest";
            String fileName;
            File file;
            byte[] zk;
            String content = "测试";

            // 先写一个文件，再测试读
            fileName = "readTest";
            file = ZKFileUtils.createFile(path, fileName, true);
            ZKFileUtils.writeFile(content.getBytes(), file, false);
            // 读
            zk = ZKFileUtils.readFile(new File(path + File.separator + fileName));
            TestCase.assertEquals(content, new String(zk));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 测试写文件
    @Test
    public void testWriteFile() {
        try {
            String path = ZKFileUtilsTest.writeFileRootPath + "writeTest";
            String fileName;
            File file;
            byte[] zk;
            String content = "test write \r\n 测试";

            // 先写一个文件，再测试读
            fileName = "writeTest";
            file = ZKFileUtils.createFile(path, fileName, true);
            ZKFileUtils.writeFile(content.getBytes(), file, false);
            // 读
            zk = ZKFileUtils.readFile(new File(path + File.separator + fileName));
            TestCase.assertEquals(content, new String(zk));
            // 追加内容
            file = new File(path + File.separator + fileName);
            ZKFileUtils.writeFile(content.getBytes(), file, true);
            // 读
            zk = ZKFileUtils.readFile(new File(path + File.separator + fileName));
            TestCase.assertEquals(content + content, new String(zk));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 测试 取文件头
    @Test
    public void testGetFileHead() {
        try {
            String path = ZKFileUtilsTest.writeFileRootPath;
            String fileName;
            File file;
            byte[] zk;
            String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \ntestGetFileHeadByPath.xml content";

            // 先写一个文件，再测试读
            fileName = "getFileHead";
            file = ZKFileUtils.createFile(path, fileName, true);
            zk = ZKFileUtils.getFileHead(file);
            System.out.println("[^_^:20231225-2303-001]    刚写的文件，file head:" + ZKEncodingUtils.encodeHex(zk));
            TestCase.assertEquals(0, zk.length);
            ZKFileUtils.writeFile(content.getBytes(), file, false);
            zk = ZKFileUtils.getFileHead(file);
            System.out.println("[^_^:20231225-2303-002]    刚写的文件，file head:" + ZKEncodingUtils.encodeHex(zk));
            TestCase.assertTrue(0 < zk.length);
            
            path = ZKFileUtilsTest.sourceFileRootPath;
            fileName = "sort.c";
            file = new File(path + File.separator + fileName);
            zk = ZKFileUtils.getFileHead(file);
            System.out.println("[^_^:20231225-2303-003]       .c 文件 file head:" + ZKEncodingUtils.encodeHex(zk));

//            fileName = "write 文件1.doc";
//            file = new File(path + File.separator + fileName);
//            zk = ZKFileUtils.getFileHead(file);
//            System.out.println("[^_^:20231225-2303-004] 手写 .doc 文件 file head:" + ZKEncodingUtils.encodeHex(zk));
//
//            fileName = "write 文件1.txt";
//            file = new File(path + File.separator + fileName);
//            zk = ZKFileUtils.getFileHead(file);
//            System.out.println("[^_^:20231225-2303-005] 手写 .txt 文件 file head:" + ZKEncodingUtils.encodeHex(zk));

            fileName = "test 文件1.doc";
            file = new File(path + File.separator + fileName);
            zk = ZKFileUtils.getFileHead(file);
            System.out.println("[^_^:20231225-2303-004] 手写 .doc 文件 file head:" + ZKEncodingUtils.encodeHex(zk));

            fileName = "test 文件2.docx";
            file = new File(path + File.separator + fileName);
            zk = ZKFileUtils.getFileHead(file);
            System.out.println("[^_^:20231225-2303-006]    .docx 文件 file head:" + ZKEncodingUtils.encodeHex(zk));

            fileName = "test 文件4.txt";
            file = new File(path + File.separator + fileName);
            zk = ZKFileUtils.getFileHead(file);
            System.out.println("[^_^:20231225-2303-007]     .txt 文件 file head:" + ZKEncodingUtils.encodeHex(zk));

            fileName = "test 文件3.jpg";
            file = new File(path + File.separator + fileName);
            zk = ZKFileUtils.getFileHead(file);
            System.out.println("[^_^:20231225-2303-008]     .jpg 文件 file head:" + ZKEncodingUtils.encodeHex(zk));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 测试 取文件头
    @Test
    public void testGetFileType() {
        try {
            String path = ZKFileUtilsTest.writeFileRootPath + "getFileType";
            String fileName;
            File file;
            ZKFileType ft;
            String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \ntestGetFileHeadByPath.xml content";

            // 先写一个文件，再测试读
            fileName = "getFileHead";
            file = ZKFileUtils.createFile(path, fileName, true);
            ft = ZKFileUtils.getFileType(file);
            TestCase.assertNull(ft);
            ZKFileUtils.writeFile(content.getBytes(), file, false);
            ft = ZKFileUtils.getFileType(file);
            TestCase.assertEquals(ZKFileType.XML, ft);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

//    // 测试文件类型检验
//    @Test
//    public void testVaildateFileType() {
//        ZKFileType[] extType = { ZKFileType.PNG };
//        try {
//            TestCase.assertTrue(0 == ZKFileUtils.vaildateFileType(ZKFileType.PNG,
//                    extType, null));
//            TestCase.assertTrue(0 == ZKFileUtils.vaildateFileType(ZKFileType.JPG,
//                    null, extType));
//            TestCase.assertTrue(1 == ZKFileUtils.vaildateFileType(ZKFileType.JPG,
//                    extType, null));
//            TestCase.assertTrue(2 == ZKFileUtils.vaildateFileType(ZKFileType.PNG,
//                    null, extType));
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//            TestCase.assertTrue(false);
//        }
//    }

    // 压缩文件
    @Test
    public void testCompressFile() {
        try {
            String rootPath = ZKFileUtilsTest.writeFileRootPath + "compressFileTest";
            File file1, file2;
            String content = "test write \r\n 测试";
            String dir1 = "compressFile", dir2 = "childDir";
            String fileName = "compressTest.txt";

            file1 = ZKFileUtils.createFile(rootPath + File.separator + dir1, fileName, true);
            ZKFileUtils.writeFile(content.getBytes(), file1, false);

            file2 = ZKFileUtils.createFile(rootPath + File.separator + dir1 + File.separator + dir2, fileName, true);
            ZKFileUtils.writeFile(content.getBytes(), file2, false);

            System.out.println("[^_^:20210415-2348-001] file1.path: " + file1.getPath());
            System.out.println("[^_^:20210415-2348-001] file2.path: " + file2.getPath());


            System.out.println("[^_^:20210415-2348-001] file1.getName: " + file1.getName());
            System.out.println("[^_^:20210415-2348-001] file1.getCanonicalPath: " + file1.getCanonicalPath());
            System.out.println("[^_^:20210415-2348-001] file1.getAbsolutePath: " + file1.getAbsolutePath());
            System.out.println("[^_^:20210415-2348-001] file1.getParent: " + file1.getParent());

            System.out.println("========================================================");
            File zipFile = new File(rootPath + File.separator + "compress.zip");
            System.out.println("[^_^:20210415-2348-001] file1.getPath: " + zipFile.getPath());

            FileOutputStream fileOutputStream = null;
            CheckedOutputStream cos = null;
            ZipOutputStream out = null;

            try {
                fileOutputStream = new FileOutputStream(zipFile);
                cos = new CheckedOutputStream(fileOutputStream, new CRC32());
                out = new ZipOutputStream(cos);

                ZKFileUtils.compress(file1.getParentFile(), out, "");
            } finally {
                ZKStreamUtils.closeStream(out);
                ZKStreamUtils.closeStream(cos);
                ZKStreamUtils.closeStream(fileOutputStream);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
