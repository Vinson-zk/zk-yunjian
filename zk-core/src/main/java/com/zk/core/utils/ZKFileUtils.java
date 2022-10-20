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
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.commons.ZKFileType;

/** 
* @ClassName: ZKFileUtils 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileUtils {

    protected static Logger log = LoggerFactory.getLogger(ZKFileUtils.class);

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
                    String tEn = fileName.lastIndexOf(".") == -1 ? "" : fileName.substring(fileName.lastIndexOf("."));
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
    public static byte[] readFile(File file) throws FileNotFoundException, IOException {
        InputStream is = new FileInputStream(file);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZKStreamUtils.readAndWrite(is, os);
        byte[] zk = os.toByteArray();

        ZKStreamUtils.closeStream(is);
        ZKStreamUtils.closeStream(os);
        return zk;
    }

    public static void readFile(File file, OutputStream os) throws FileNotFoundException, IOException {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            ZKStreamUtils.readAndWrite(is, os);
        } finally {
            ZKStreamUtils.closeStream(is);
        }
    }

    /**
     * 从流中写入文件
     *
     * @Title: writeFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 18, 2019 4:19:19 PM
     * @param inputStream
     *            输入流，需求在外面关闭，谁创建谁管理；
     * @param file
     *            目标文件
     * @param appendFlag
     *            是否追加，true 追加，false 从头开始写
     * @return
     * @return long
     */
    public static long writeFile(InputStream inputStream, File file, boolean appendFlag) {
        FileOutputStream fileOS = null;
        try {
            fileOS = new FileOutputStream(file, appendFlag);
            long size = ZKStreamUtils.readAndWrite(inputStream, fileOS);
            ZKStreamUtils.closeStream(fileOS);
            return size;
        }
        catch(Exception e) {
            log.error("[>_<：20190318-1307-002] 写文件失败;");
            e.printStackTrace();
        } finally {
            if (fileOS != null) {
                ZKStreamUtils.closeStream(fileOS);
            }
        }
        return 0;
    }

    public static long writeFile(String contentBytes, File file, boolean appendFlag) {
        return writeFile(contentBytes.getBytes(), file, appendFlag);
    }
    /**
     * 将字节数组写到文件中
     *
     * @Title: writeFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 18, 2019 4:22:26 PM
     * @param contentBytes
     *            内容字节数组
     * @param file
     *            目标文件
     * @param appendFlag
     *            是否追加，true 追加，false 从头开始写
     * @return
     * @return long
     */
    public static long writeFile(byte[] contentBytes, File file, boolean appendFlag) {
        FileOutputStream fileOS = null;
        ByteArrayInputStream byteInputStream = null;
        try {
            fileOS = new FileOutputStream(file, appendFlag);
            byteInputStream = new ByteArrayInputStream(contentBytes);
            long size = ZKStreamUtils.readAndWrite(byteInputStream, fileOS);
            return size;
        }
        catch(Exception e) {
            log.error("[>_<：20190318-1307-002] 写文件失败;");
            e.printStackTrace();
        } finally {
            if (byteInputStream != null) {
                ZKStreamUtils.closeStream(byteInputStream);
            }
            if (fileOS != null) {
                ZKStreamUtils.closeStream(fileOS);
            }
        }
        return 0;
    }

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
