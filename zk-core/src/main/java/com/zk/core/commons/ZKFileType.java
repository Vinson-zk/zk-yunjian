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
 * @Title: ZKFileType.java 
 * @author Vinson 
 * @Package com.zk.core.commons 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:47:31 PM 
 * @version V1.0   
*/
package com.zk.core.commons;

import java.util.ArrayList;
import java.util.List;

/** 
* @ClassName: ZKFileType 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public enum ZKFileType {

    // 3gg; 3gp; 3g2
    // 3GG
    _3GPP("3gp", "0000001466747970336770"), // 3rd Generation Partnership Project 3GPP (nn=0x14) and 3GPP2 (nn=0x20) multimedia files 
    _3GP2("3g2", "0000002066747970336770"), _3GP4("3g4", "0000001866747970336770"), 
    AMR_NB("amr", "2321414D52"), // Adaptive Multi-Rate ACELP (Algebraic Code Excited Linear Prediction) Codec, commonly audio format with GSM cell phones
    AMR_WB("amr", "2321414D522D5742"), 
    JPG("jpg", "FFD8FF"), // JPEG(jpg)
    JPEG("jpeg", "FFD8FF"), // JPEG(jpg)
    GIF("gif", "47494638"), // GIF(gif)
    PNG("png", "89504E47"), // PNG(png)
    BMP("bmp", "424D"), // WindowsBitmap(bmp)
    HTML("html", "68746D6C3E"), // HTML(html)
    ZIP("zip", "504B0304"), 
    XML("xml", "3C3F786D6C"), 
    RAR("rar", "52617221"), 
    XLS("xls", "D0CF11E0"), // MSWord
    DOC("doc", "D0CF11E0"), // MSExcel注意：word和excel的文件头一样
    PDF("pdf", "255044462D312E"), // AdobeAcrobat(pdf)
    RM("rm", "2E524D46"), // RealMedia(rm)
    DWG("dwg", "41433130"), // CAD(dwg)
    AVI("avi", "41564920"),
    TIF("tif", "49492A00"), // TIFF(tif)
    RTF("rtf", "7B5C727466"), // RichTextFormat(rtf)
    PSD("psd", "38425053"), // Photoshop(psd)
    EML("eml", "44656C69766572792D646174653A"), // Email[thoroughonly](eml)
    DBX("dbx", "CFAD12FEC5FD746F"), // OutlookExpress(dbx)
    PST("pst", "2142444E"), // Outlook(pst)
    MDB("mdb", "5374616E64617264204A"), // MSAccess(mdb)
    WPD("wpd", "FF575043"), // WordPerfect(wpd)
    EPS("eps", "252150532D41646F6265"), PS("ps", "252150532D41646F6265"), 
    QDF("qdf", "AC9EBD8F"), // Quicken(qdf)
    PWL("pwl", "E3828596"), // WindowsPassword(pwl)
    WAV("wav", "57415645"), // Wave(wav)
    RAM("ram", "2E7261FD"), // RealAudio(ram)
    MPG("mpg", "000001BA"), //
    MOV("mov", "6D6F6F76"), // Quicktime(mov)
    ASF("asf", "3026B2758E66CF11"), // WindowsMedia(asf)
    MID("mid", "4D546864"); // MIDI(mid)

    private String type;

    private String key;

    ZKFileType(String type, String key) {
        this.type = type;
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public static ZKFileType parseByKey(String key) {
        for (ZKFileType et : ZKFileType.values()) {
            if (et.getKey().equals(key)) {
                return et;
            }
        }
        return null;
    }

    public static ZKFileType parseByType(String type) {
        for (ZKFileType et : ZKFileType.values()) {
            if (et.getType().equals(type)) {
                return et;
            }
        }
        return null;
    }

    public static String[] getTypeString(ZKFileType[] xft) {
        String[] ss = new String[xft.length];
        for (int i = 0; i < ss.length; ++i) {
            ss[i] = xft[i].key;
        }
        return ss;
    }

    public static ZKFileType[] getFileTypeByString(String[] sss) {
        List<ZKFileType> fts = new ArrayList<>();
        ZKFileType ft = null;
        for (int i = 0; i < sss.length; ++i) {
            ft = null;
            ft = parseByType(sss[i]);
            if (ft != null) {
                fts.add(ft);
            }
        }
        return fts.size() == 0 ? new ZKFileType[0] : fts.toArray(new ZKFileType[fts.size()]);
    }

}
