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
* @Title: ZKWebmvcLifecycleListener.java 
* @author Vinson 
* @Package com.zk.webmvc.tomcat.catalina 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 22, 2024 11:03:51 PM 
* @version V1.0 
*/
package com.zk.webmvc.tomcat.catalina;

import java.io.File;
import java.net.URL;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceRoot.ResourceSetType;
import org.apache.catalina.WebResourceSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.utils.ZKUtils;

/** 
* @ClassName: ZKWebmvcLifecycleListener 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebmvcLifecycleListener implements LifecycleListener {

    protected Logger log = LogManager.getLogger(this.getClass());

    private static final String APP_MOUNT = "/";

    private static final String BOOT_INF = "/BOOT-INF/classes";

    private static final String INTERNAL_PATHS = "/META-INF/resources";

    private final String[] resource_INTERNAL_PATHS;

    public ZKWebmvcLifecycleListener() {
        this.resource_INTERNAL_PATHS = new String[] {};
    }

    public ZKWebmvcLifecycleListener(String[] resourceSets) {
        this.resource_INTERNAL_PATHS = resourceSets;
    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        if (event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {

            if (event.getLifecycle() instanceof Context context) {
                WebResourceRoot webResourceRoot = context.getResources();
                if (webResourceRoot != null) {
                    this.addResources(webResourceRoot);
                    print("JarResources", webResourceRoot.getJarResources());
                    print("PostResources", webResourceRoot.getPostResources());
                    print("PreResources", webResourceRoot.getPreResources());
                    for (URL rUrl : webResourceRoot.getBaseUrls()) {
                        System.out.println("[^_^:20240622-1730-001] BaseUrls: " + rUrl.getPath());
                    }
                }
            }
        }

        if (event.getType().equals(Lifecycle.AFTER_START_EVENT)) {
            if (event.getLifecycle() instanceof Context context) {
                WebResourceRoot webResourceRoot = context.getResources();
                if (webResourceRoot != null) {
                    print("JarResources", webResourceRoot.getJarResources());
                    print("PostResources", webResourceRoot.getPostResources());
                    print("PreResources", webResourceRoot.getPreResources());
                    for (URL url : webResourceRoot.getBaseUrls()) {
                        System.out.println("[^_^:20240622-1730-002] BaseUrls: " + url.getPath());
                    }
                }
            }
        }

    }

    private void print(String flag, WebResourceSet[] wrss) {
        for (WebResourceSet wrs : wrss) {
            System.out.println("[^_^:20240622-1730-001] WebResourceSet." + flag + ": " + wrs.getBaseUrl());
        }
    }

    private void addResources(WebResourceRoot webResourceRoot) {

//        ProtectionDomain pd = this.getClass().getProtectionDomain();
//        CodeSource cs = pd.getCodeSource();
//        System.out.println("[^_^:20240623-2220-001] zk.classpath: " + cs.getLocation());
////         [^_^:20240623-2220-001] zk.classpath:
////         jar:nested:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/zk-serverCentral-3.0.1-exec.jar/!BOOT-INF/lib/zk-webmvc-3.0.1.jar!/
//
//        URL url = Thread.currentThread().getContextClassLoader().getResource("");
//        System.out.println("[^_^:20240623-2220-002-1] zk.classpath.getFile: " + url.getFile());
//        System.out.println("[^_^:20240623-2220-002-1] zk.classpath.getPath: " + url.getPath());
//        System.out.println("[^_^:20240623-2220-002-1] zk.classpath.toExternalForm: " + url.toExternalForm());
//        System.out.println("[^_^:20240623-2220-002-1] zk.classpath.toString: " + url.toString());
//        System.out.println("[^_^:20240623-2220-002-1] zk.classpath.getProtocol: " + url.getProtocol());
//        System.out.println("[^_^:20240623-2220-002-1] zk.classpath.getQuery: " + url.getQuery());
////        ----- jar
////        [^_^:20240623-2220-002-1] zk.classpath.getFile: nested:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/zk-serverCentral-3.0.1-exec.jar/!BOOT-INF/classes/!/
////        [^_^:20240623-2220-002-1] zk.classpath.getPath: nested:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/zk-serverCentral-3.0.1-exec.jar/!BOOT-INF/classes/!/
////        [^_^:20240623-2220-002-1] zk.classpath.toExternalForm: jar:nested:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/zk-serverCentral-3.0.1-exec.jar/!BOOT-INF/classes/!/
////        [^_^:20240623-2220-002-1] zk.classpath.toString: jar:nested:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/zk-serverCentral-3.0.1-exec.jar/!BOOT-INF/classes/!/
////        [^_^:20240623-2220-002-1] zk.classpath.getProtocol: jar
////        [^_^:20240623-2220-002-1] zk.classpath.getQuery: null
////        ----- file
////        [^_^:20240623-2220-002-1] zk.classpath.getFile: /Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/classes/
////        [^_^:20240623-2220-002-1] zk.classpath.getPath: /Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/classes/
////        [^_^:20240623-2220-002-1] zk.classpath.toExternalForm: file:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/classes/
////        [^_^:20240623-2220-002-1] zk.classpath.toString: file:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/classes/
////        [^_^:20240623-2220-002-1] zk.classpath.getProtocol: file
////        [^_^:20240623-2220-002-1] zk.classpath.getQuery: null
//
//        url = this.getClass().getResource("/");
//        System.out.println("[^_^:20240623-2220-002-2] zk.classpath.getFile: " + url.getFile());
//        System.out.println("[^_^:20240623-2220-002-2] zk.classpath.getPath: " + url.getPath());
//        System.out.println("[^_^:20240623-2220-002-2] zk.classpath.toExternalForm: " + url.toExternalForm());
//        System.out.println("[^_^:20240623-2220-002-2] zk.classpath.toString: " + url.toString());
//        System.out.println("[^_^:20240623-2220-002-2] zk.classpath.getProtocol: " + url.getProtocol());
//        System.out.println("[^_^:20240623-2220-002-2] zk.classpath.getQuery: " + url.getQuery());
////        [^_^:20240623-2220-002-2] zk.classpath.getFile: nested:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/zk-serverCentral-3.0.1-exec.jar/!BOOT-INF/classes/!/
////        [^_^:20240623-2220-002-2] zk.classpath.getPath: nested:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/zk-serverCentral-3.0.1-exec.jar/!BOOT-INF/classes/!/
////        [^_^:20240623-2220-002-2] zk.classpath.toExternalForm: jar:nested:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/zk-serverCentral-3.0.1-exec.jar/!BOOT-INF/classes/!/
////        [^_^:20240623-2220-002-2] zk.classpath.toString: jar:nested:/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target/zk-serverCentral-3.0.1-exec.jar/!BOOT-INF/classes/!/
////        [^_^:20240623-2220-002-2] zk.classpath.getProtocol: jar
////        [^_^:20240623-2220-002-2] zk.classpath.getQuery: null
//
        System.out.println("[^_^:20240623-2220-002-4] zk.classpath.user.dir: " + System.getProperty("user.dir"));
        System.out.println(
                "[^_^:20240623-2220-002-5] zk.classpath.java.class.path: " + System.getProperty("java.class.path"));
////        [^_^:20240623-2220-002-4] zk.classpath.user.dir: /Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-serverCentral/target
////        [^_^:20240623-2220-002-5] zk.classpath.java.class.path: zk-serverCentral-3.0.1-exec.jar

        // ----------------------------------------------------------------------
//        URL classUrl = Thread.currentThread().getContextClassLoader().getResource("");
////        classUrl.of(null, null)classUrl.GETH
//        this.addResourceSet(webResourceRoot, ZKUtils.getAbsolutePath(""));

        URL classUrl = ZKUtils.getClassPath();

        if (classUrl.getProtocol().equals("jar")) {
            String dir = System.getProperty("user.dir");
            String jarFile = System.getProperty("java.class.path");
            String jarPath = dir + File.separator + jarFile;
            if ((new File(jarPath)).isFile()) {
                log.info("[^_^:20240624-0011-001] 添加 servlet context 资源路径: {}", jarPath);
                webResourceRoot.createWebResourceSet(ResourceSetType.RESOURCE_JAR, APP_MOUNT, jarPath, null,
                        INTERNAL_PATHS);
                this.addJarResourceSet(webResourceRoot, jarPath);
            }
        }
        else {
            this.addDirResourceSet(webResourceRoot, classUrl.getPath());
        }
    }

    private void addJarResourceSet(WebResourceRoot webResourceRoot, String jarPath) {
        for (String internalPath : this.resource_INTERNAL_PATHS) {
            webResourceRoot.createWebResourceSet(ResourceSetType.RESOURCE_JAR, APP_MOUNT, jarPath, null,
                    BOOT_INF + internalPath);
        }
    }

    private void addDirResourceSet(WebResourceRoot webResourceRoot, String dirPath) {
        for (String internalPath : this.resource_INTERNAL_PATHS) {
            webResourceRoot.createWebResourceSet(ResourceSetType.RESOURCE_JAR, APP_MOUNT, dirPath, null, internalPath);
        }
    }

}
