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
 * @Title: ZKFileUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:51:20 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.commons.ZKContentType;
import com.zk.core.commons.ZKFileType;

/** 
* @ClassName: ZKFileUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileUtils {

    protected static Logger log = LogManager.getLogger(ZKFileUtils.class);

    // 判断文件是否存； true-文件存在；false-文件不存在；
    public static boolean isFileExists(String fileFullPath) {
        return isFileExists(new File(fileFullPath));
    }

    // 判断文件是否存； true-文件存在；false-文件不存在；
    public static boolean isFileExists(File file) {
        if (!file.exists() || !file.isFile()) {
            // 文件不存，或不是文件
            return false;
        }
        return true;
    }

    // 判断目录是否存； true-目录存在；false-目录不存在；
    public static boolean isDirExists(String dirPath) {
        return isDirExists(new File(dirPath));
    }

    // 判断目录是否存； true-目录存在；false-目录不存在；
    public static boolean isDirExists(File file) {
        if (!file.exists() || !file.isDirectory()) {
            // 目录不存在，或不是目录
            return false;
        }
        return true;
    }

    // 创建文件，文件存在不处理
    public static File createFile(String fileFullPath) throws IOException {
        return createFile(new File(fileFullPath));
    }

    // 创建文件，文件存在不处理
    public static File createFile(File file) throws IOException {
        // 文件存在
        if (file.exists()) {
            if (file.isDirectory()) {
                file.createNewFile();
            }
        }
        else {
            File pf = file.getParentFile();
            if (pf != null && !pf.isDirectory()) {
                // 上级目录不存或不是目录
                pf.mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }

    /**
     * 创建一个新的文件名；会自动添加文件后缀。
     *
     * @Title: newFileName
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 28, 2024 2:33:30 PM
     * @param contentType
     *            可以为 null；为 null 时，文件后缀 会从 fileName 后取。
     * @param fileName
     *            原文件名
     * @return String 返回新文件名，从参数中可以取到文件后缀时，包含文件后缀。
     */
    public static String newFileName(ZKContentType contentType, String fileName) {
        StringBuffer newFileNameSb = new StringBuffer();
        newFileNameSb = newFileNameSb.append(UUID.randomUUID().toString());
        if (contentType != null && contentType.getExtName() != null) {
            newFileNameSb = newFileNameSb.append(".").append(contentType.getExtName());
        }
        else {
            newFileNameSb = newFileNameSb.append(getExtensionName(fileName));
        }
        return newFileNameSb.toString();
    }

    // 通过文件名取后缀，包含 .
    public static String getExtensionName(String fileName) {
        if (ZKStringUtils.isEmpty(fileName)) {
            return "";
        }
        return fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf("."));

    }

    /**
     * 创建一个文件，
     * 
     * @Title: createFile
     * @Description: TODO(simple description this method what to do.)
     * @author zk
     * @date 2017年9月10日 上午11:34:52
     * @param path
     *            创建文件路径
     * @param fileName
     *            文件名
     * @param isOverride
     *            文件存在时是否覆盖
     * @return
     * @throws Exception
     * @return File
     */
    public static File createFile(String path, String fileName, boolean isOverride) {
        try {
            File file = new File(path);
            if (!file.exists() || !file.isDirectory()) {
                // 目录不存在，创建目录
                if (!file.mkdirs()) {
                    log.error("[>_<： 201709101104-001] 创建文件路径失败：path：{}", path);
                }
            }

            file = new File(path + File.separator + fileName);

            // 文件存在，且需要重命名
            if (file.exists() && file.isFile() && !isOverride) {
                // 文件存在
                if (!isOverride) { // 不覆盖，重命名
                    // 取文件点后缀名，包括字符点
                    String tEn = ZKFileUtils.getExtensionName(fileName);
                    String tFn = fileName.lastIndexOf(".") == -1 ? fileName
                            : fileName.substring(0, fileName.lastIndexOf("."));
                    int i = 1;
                    file = new File(String.format("%s/%s(%d)%s", path, tFn, i, tEn));
                    while (file.exists()) {
                        i += 1;
                        file = new File(String.format("%s/%s(%d)%s", path, tFn, i, tEn));
                    }
                }
            }

            if (!file.exists()) {
                // 文件不存在, 创建文件
                if (!file.createNewFile()) {
                    log.error("[>_<： 201709101104-002] 创建文件失败：file:：{}", file.getAbsolutePath());
                }
            }
            else {
                if (isOverride) {
                    if (!file.delete()) {
                        log.error("[>_<： 201709101104-003] 文件已存在，删除失败：file:：{}", file.getAbsolutePath());
                    }
                    if (!file.createNewFile()) {
                        log.error("[>_<： 201709101104-004] 创建文件失败：file:：{}", file.getAbsolutePath());
                    }
                }
            }
            return file;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 读文件 ------------------------------------------------
    public static byte[] readFile(File file) throws FileNotFoundException, IOException {
        return ZKFileUtils.readFile(-1, file);
    }

    public static long readFile(File file, OutputStream os) throws FileNotFoundException, IOException {
        return ZKFileUtils.readFile(-1, file, os);
    }

    /**
     * 读取文件，返回字节数组，读取失败时，返回一个空字节数组。
     *
     * @Title: readFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 18, 2019 1:10:00 PM
     * @param file
     * @return
     * @return byte[]
     */
    public static byte[] readFile(int readLength, File file) throws FileNotFoundException, IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZKFileUtils.readFile(readLength, file, os);
        byte[] zk = os.toByteArray();
        ZKStreamUtils.closeStream(os);
        return zk;
    }

    public static long readFile(long readLength, File file, OutputStream os) throws FileNotFoundException, IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return ZKStreamUtils.readAndWrite(is, os, readLength);
        } finally {
            ZKStreamUtils.closeStream(is);
        }
    }

    // 写文件 ------------------------------------------------
    public static long writeFile(String contentBytes, File file, boolean appendFlag) throws Exception {
        return ZKFileUtils.writeFile(-1, contentBytes.getBytes(), file, appendFlag);
    }

    public static long writeFile(long readLength, String contentBytes, File file, boolean appendFlag) throws Exception {
        return ZKFileUtils.writeFile(readLength, contentBytes.getBytes(), file, appendFlag);
    }

    public static long writeFile(byte[] contentBytes, File file, boolean appendFlag) throws Exception {
        return ZKFileUtils.writeFile(-1, contentBytes, file, appendFlag);
    }

    public static long writeFile(long readLength, byte[] contentBytes, File file, boolean appendFlag) throws Exception {
        ByteArrayInputStream byteInputStream = null;
        try {
            byteInputStream = new ByteArrayInputStream(contentBytes);
            return ZKFileUtils.writeFile(readLength, byteInputStream, file, appendFlag);
        } finally {
            if (byteInputStream != null) {
                ZKStreamUtils.closeStream(byteInputStream);
            }
        }
    }
    
    public static long writeFile(InputStream inputStream, File file, boolean appendFlag) throws Exception {
        return ZKFileUtils.writeFile(-1, inputStream, file, appendFlag);
    }

    /**
     * 从流中写入文件
     *
     * @Title: writeFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 18, 2019 4:19:19 PM
     * @param readLength
     *            读取的最大长度；<= 0 不限制
     * @param inputStream
     *            输入流，需求在外面关闭，谁创建谁管理；
     * @param file
     *            目标文件
     * @param appendFlag
     *            是否追加，true 追加，false 从头开始写
     * @return
     * @return long
     * @throws Exception
     */
    public static long writeFile(long readLength, InputStream inputStream, File file, boolean appendFlag)
            throws Exception {
        FileOutputStream fileOS = null;
        try {
            fileOS = new FileOutputStream(file, appendFlag);
            long size = ZKStreamUtils.readAndWrite(inputStream, fileOS, readLength);
            ZKStreamUtils.closeStream(fileOS);
            return size;
        } finally {
            if (fileOS != null) {
                ZKStreamUtils.closeStream(fileOS);
            }
        }
    }

    // 其他 ------------------------------------------------

    /**
     * 取文件头
     * 
     * @param file
     * @return
     */
    public static byte[] getFileHead(File file) {
        InputStream infile = null;
        byte[] resultByte = null;
        try {
            if (file.exists()) {
                infile = new FileInputStream(file);
                resultByte = new byte[50];
                int byteread = infile.read(resultByte);
                if (byteread == -1) {
                    resultByte = null;
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            ZKStreamUtils.closeStream(infile);
        }
        return resultByte == null ? new byte[0] : resultByte;
    }

    /**
     * 取文件类型
     * 
     * @param file
     * @return
     */
    public static ZKFileType getFileType(File file) {
        ZKFileType resultEt = null;
        if (file != null && file.isFile()) {
            byte[] fileHead = getFileHead(file);
            if (fileHead == null)
                return resultEt;
            String fileHeadStr = ZKEncodingUtils.encodeHex(fileHead);
            for (ZKFileType et : ZKFileType.values()) {
                if (fileHeadStr.toUpperCase().startsWith(et.getKey().toUpperCase())) {
                    if (resultEt == null) {
                        resultEt = et;
                    }
                    else {
                        if (resultEt.getKey().length() < et.getKey().length()) {
                            resultEt = et;
                        }
                    }
                }
            }
        }
        return resultEt;
    }

    /**
     * 压缩; file 可以是文件，也可以是目录
     *
     * @Title: compress
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 17, 2021 4:16:07 PM
     * @param file
     * @return
     * @throws IOException
     * @return File 返回压缩的文件
     */
    public static File compress(File file) throws IOException {
        return compress(file, file.getName());
    }

    public static File compress(File file, String fileName) throws IOException {
        FileOutputStream fileOutputStream = null;
        CheckedOutputStream cos = null;
        ZipOutputStream out = null;

        File zipFile = new File(file.getParent() + File.separator + fileName + ".zip");
        try {
            fileOutputStream = new FileOutputStream(zipFile);
            cos = new CheckedOutputStream(fileOutputStream, new CRC32());
            out = new ZipOutputStream(cos);

            ZKFileUtils.compress(file, out, "");
        } finally {
            ZKStreamUtils.closeStream(out);
            ZKStreamUtils.closeStream(cos);
            ZKStreamUtils.closeStream(fileOutputStream);
        }
        return zipFile;
    }


    /**
     * 压缩; file 可以是文件，也可以是目录
     * 
     * @param file
     * @param out
     * @param basedir
     */
    public static void compress(File file, ZipOutputStream out, String basedir) throws IOException {
        // 判断是目录还是文件
        if (file.isDirectory()) {
            compressDirectory(file, out, basedir);
        }
        else {
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     */
    protected static void compressDirectory(File dir, ZipOutputStream out, String basedir) throws IOException {
        if (!dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 递归
            compress(files[i], out, basedir + dir.getName() + File.separator);
        }
    }

    /**
     * 压缩一个文件
     *
     */
    protected static void compressFile(File file, ZipOutputStream out, String basedir) throws IOException {
        if (!file.exists()) {
            return;
        }
        ZipEntry entry = new ZipEntry(basedir + file.getName());
        out.putNextEntry(entry);
        readFile(file, out);
    }

    /**
     * 递归删除目录或文件，如果目录，递归删除目录下的所有文件及子目录
    *
    * @Title: deleteDir 
    * @Description: TODO(simple description this method what to do.) 
    * @author Vinson 
    * @date Apr 17, 2021 3:32:43 PM 
    * @param dir
    * @return
    * @return boolean
     */
    public static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                // 递归
                boolean success = deleteFile(f);
                if (!success) {
                    return false;
                }
            }

        }
        // 目录此时为空，可以删除
        return file.delete();
    }
}
