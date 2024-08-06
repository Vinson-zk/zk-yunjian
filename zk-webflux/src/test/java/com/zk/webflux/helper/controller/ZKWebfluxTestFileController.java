/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKWebfluxTestFileController.java 
* @author Vinson 
* @Package com.zk.webflux.helper.controller 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 25, 2024 12:15:57 AM 
* @version V1.0 
*/
package com.zk.webflux.helper.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.commons.reactor.ZKReactorRelational;
import com.zk.core.commons.reactor.ZKResultCollect;
import com.zk.core.utils.ZKStreamUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.support.webFlux.io.ZKSequenceInputStream;
import com.zk.core.web.utils.ZKHtmlUtils;
import com.zk.test.file.ZKFileUploadTest;

import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @ClassName: ZKWebfluxTestFileController
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@RestController
@RequestMapping("${zk.path.admin:zk}/${zk.path.webflux}/file")
public class ZKWebfluxTestFileController {

    @Autowired
    ZKFileTransfer zkFileTransfer;

    String targetPath = ZKFileUploadTest.uploadFileRootPath + File.separator + ZKFileUploadTest.upload;

    /**
     * 二进制流上传，不需要 MultipartResolver 适配器
     *
     * @Title: uploadStream
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 17, 2019 3:41:03 PM
     * @param hReq
     * @return
     * @throws IOException
     * @return void
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "uploadStream")
    public Mono<String> uploadStream(ServerWebExchange exchange, ServerHttpRequest shReq)
            throws UnsupportedEncodingException {

//        Mono<MultiValueMap<String, String>> d = exchange.getFormData();
        Flux<DataBuffer> dbFlux = shReq.getBody();

        return this.combineFluxStreams(dbFlux).flatMap(zkSequenceInputStream -> {
                    String fileName = shReq.getHeaders().getFirst("fileName");
                    if (!ZKStringUtils.isEmpty(fileName)) {
                        fileName = ZKHtmlUtils.urlDecode(fileName);
                    }

                    // 将输入流多个dataBuffer合并为一个InputStream后使用
                    InputStream is = null;
                    // 操作 inputStream 读取数据并处理
                    String fileAbsolutePath = null;
                    try {
                        is = zkSequenceInputStream.getInputStream();
                        fileAbsolutePath = zkFileTransfer.transferFile(is, targetPath, fileName, true, false, null,
                                null);
                    } finally {
                        if (is != null) {
                            ZKStreamUtils.closeStream(is);
                        }
                    }
                    System.out.println(
                            "[^_^:20240329-2201-001] ZKWebfluxTestFileController.uploadStream: " + fileAbsolutePath);
                    // 返回对应数据
                    return Mono.just(fileAbsolutePath);
                });

//        return request.getBody().collect(ServerHttpConnection.InputStreamCollector::new,
//                (t, dataBuffer) -> t.collectInputStream(dataBuffer.asInputStream()))
//        .flatMap(inputStreamCollector -> {
//           // 将输入流多个dataBuffer合并为一个InputStream后使用
//           InputStream requestInputStream = inputStreamCollector.getInputStream();
//           // 操作 inputStream 读取数据并处理
//           ......
//           ......
//           ......
//           // 这里返回 empty 只是示例，实际使用根据自己需求返回对应数据
//           return Mono.empty();
//        });

//        dbFlux.subscribe(ds -> {
//            String fileAbsolutePath = "";
//            InputStream is = null;
//            try {
//                is = ds.asInputStream();
//                fileAbsolutePath = zkFileTransfer.transferFile(is, targetPath, fileName, true, false);
//            } finally {
//                if (is != null) {
//                    ZKStreamUtils.closeStream(is);
//                }
//            }
////            return fileAbsolutePath;
//        });

    }

    Set<String> signs = new HashSet<>();

    /**
     *
     * @Title: uploadFilePart
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 17, 2019 3:40:47 PM
     * @param mf1
     * @param mfs
     * @return
     * @throws FileUploadException
     * @throws IOException
     * @return List<String>
     */
    @RequestMapping(path = "uploadFilePart", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<List<String>> uploadMultipart(ServerWebExchange serverWebExchange,
            @RequestPart(value = "fs", required = false) List<FilePart> pfs,
            @RequestPart(value = "f1") FilePart pf1) {
//        @RequestPart(value = "f1") Flux<FilePart> mf1Flux
//        @RequestPart(value = "f1") Part mf1Flux
//        @RequestPart(value = "f1") FilePart mf1
//        @RequestPart(value = "fs", required = false) List<FilePart> mfs
        
        System.out.println("[^_^:20240222-0042-001] ZKWebfluxTestFileController ======================"
                + Thread.currentThread().getName());
        System.out.println("[^_^:20240222-0042-001] getOriginalFilename: " + pf1.name());
        System.out.println("[^_^:20240222-0042-001] getName: " + pf1.filename());
        System.out.println("[^_^:20240222-0042-001] getContentType: " + pf1.headers().getContentType());
        System.out.println("[^_^:20240222-0042-001] getContentType: " + pf1.headers().getContentType().getSubtype());
//        System.out.println("[^_^:20240222-0042-001] getResource: " + mf1.getResource());

        List<FilePart> pfsList = new ArrayList<FilePart>();
        pfsList.add(pf1);
        if(pfs != null) {
            pfsList.addAll(pfs);
        }
        Mono<List<String>> resMono = Mono.just(pfsList).flatMap(mps -> {
            return Mono.create(callback->{
                ZKResultCollect<String> rc = new ZKResultCollect<String>(mps.size(), callback);
                mps.forEach(fpItem -> {
                    Mono<ZKSequenceInputStream> m = this.combineFluxStreams(fpItem.content());
                    m = m.doFinally(onFinally -> {
                        rc.doSubscribe();
                    });
                    ZKReactorRelational<String> rr = rc.createRelational();
                    Disposable d = m.subscribe(sequenceIS -> {
                        String path = this.zkFileTransfer.transferFile(sequenceIS.getInputStream(), this.targetPath,
                                fpItem.filename(), false, false, null, null);
                        rc.accept(path);
                    });
                    rr.setDisposable(d);
                });
                rc.doSubscribe();
            });
        });
        return resMono;
    }

    /**
     * 取文件
     *
     * @Title: getFile
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 1, 2024 9:08:20 AM
     * @param fName
     * @param hRes
     * @return
     * @throws FileUploadException
     * @throws IOException
     * @return void
     */
    @RequestMapping(path = "getFile")
    public Mono<Void> getFile(@RequestParam(value = "fName") String fName, ServerWebExchange exchange,
            ServerHttpResponse hRes) throws FileUploadException, IOException {

        String fileId = this.targetPath + File.separator + fName;
        // 下载文件，浏览器自动转为下载时，需要添加下面这一句。
//        hRes.setHeader("Content-Disposition", "attachment;fileName=" + fName);

        OutputStream os = null;
        try {
            DataBufferFactory factory = hRes.bufferFactory();
            DataBuffer bf = factory.allocateBuffer(128);
            os = bf.asOutputStream();
            this.zkFileTransfer.getFile(fileId, os);
            os.flush();
            return hRes.writeWith(Mono.just(bf));
        } finally {
            ZKStreamUtils.closeStream(os);
        }

    }

    public Mono<ZKSequenceInputStream> combineFluxStreams(Flux<DataBuffer> flux) {
        return flux.collect(() -> {
            return new ZKSequenceInputStream();
        }, (t, dataBuffer) -> {
            t.add(dataBuffer.asInputStream());
        });
    }

}

//public void  uploadAttachList(@RequestPart(“files”)  List<FilePart> file) {
//    for(FilePart file : files){
//        //获取文件请求头的类型
//        MediaType mediaType = file.headers().getContentType();
//        //获取文件类型
//        String fileType = mediaType.getSubtype();
//        //获取文件流式内容
//        Flux<DataBuffer> flux  = file.content();
//        //注意：上面获取的流内容可能会被分片，下面的方法是将分片的流组合在一起，没有这一步可能会导致有些文件缺失
//        Flux<DataBuffer> fluxNew = combineFluxStreams(flux);
//        try{
//            //上传的是base64 能获取到文件名称，如果是二进制，获取到名称是blob；
//            String fileName  = file.filename();
//            //文件名
//            String name = fileName + “.” + fileType;
//            //订阅流
//            fluxNew.subscribe(buffer ->{
//                //初始化字节数组
//                byte[] bytes = new byte[buffer.readableByteCount()];
//                //给数组写入内容
//                buffer.read(bytes);
//                //此次记得释放流，否则会引起堆外内存泄漏
//                DataBufferUtils.release(buffer);
//                try{
//                        InputStream fileInputStream = new ByteArrayInputStream(bytes);
//                        File uploadFile = new File(“D:\\Test\\”+name);
//                        OutputStream fos = new FileOutputStream(uploadFile);
//                        byte[]  buff = new byte[1024];
//                        int temp;
//                        while((temp =  fileInputStream.read(buff)) != -1 ){
//                            fos.write(buff);
//                        }
//                        fileInputStream.close();
//                        fos.close();
//                
//                }catch(Exception e){
//                }
//            });
//            
//        }catch(Exception e){
//        }
//        
//    }
//}
