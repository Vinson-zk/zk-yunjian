
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
* @Title: ZKEnableDb.java 
* @author Vinson 
* @Package com.zk.db.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 3:18:01 PM 
* @version V1.0 
*/
package com.zk.db.configuration;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.db.annotation.ZKMyBatisDao;
import com.zk.db.configuration.ZKEnableDB.AnnotationKey.MapperKey;
import com.zk.db.configuration.ZKEnableDB.AnnotationKey.SqlSessionFactoryKey;
import com.zk.db.configuration.ZKEnableDB.ZKDBAnnotationConfiguration;
import com.zk.db.entity.ZKDBEntity;

/**
 * 需要要注入 "zkDBProperties" bean; 且 zkDBProperties bean 中 publicDruidPool 不能为空
 * 
 * @ClassName: ZKEnableDb
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//@Import(value = { ZKDBAnnotationConfiguration.class })
@ImportAutoConfiguration(value = { ZKDBAnnotationConfiguration.class })
public @interface ZKEnableDB {

    public static final String printLog = "[^_^:20230209-2148-004] ----- zk-db config: ";

    String basePackage() default "com.zk.**.dao";

    Class<? extends Annotation> annotationClass() default ZKMyBatisDao.class;

    // ------------------------------------------------------------

    String typeAliasesPackage() default "com.zk.**.entity";

    Class<?> typeAliasesSuperType() default ZKDBEntity.class;

    String[] mapperLocations() default { "classpath:mappers/**/*.xml" };

    String configLocation() default "classpath:xmlConfig/mybatis/mybatis_config.xml";

    /**
     * 
     * @ClassName: ZKDBAnnotationConfiguration
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public class ZKDBAnnotationConfiguration implements ImportBeanDefinitionRegistrar {

        AnnotationAttributes annoAttrs;

        public ZKDBAnnotationConfiguration() {
            System.out.println(printLog + "[" + this.getClass().getSimpleName() + "]");
        }

        // 这个方法，会在注解解析之前，执行；
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            System.out.println(ZKEnableDB.printLog + "registerBeanDefinitions --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
            
//            if(registry.containsBeanDefinition("ZKDBSourceConfiguration")) {
//                throw new ZKUnknownException("[^_^:20230207-1853-001] Annotated ZKEnableDB, ZKDBSourceConfiguration Repeat configuration!");
//            }
            // 取出 ZKEnableDB 注解
            Map<String, Object> classMetadata = importingClassMetadata.getAnnotationAttributes(ZKEnableDB.class.getName());
            if (classMetadata == null) {
                throw new ZKUnknownException(printLog + "not find ZKEnableDB Annotation ");
            }
            AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(classMetadata);

            this.registrySourceBeanDefinition(annoAttrs, registry);

            this.registryMapperScannerBeanDefinition(annoAttrs, registry);
        }

        /**
         * 注册数据源配置类
         *
         * @Title: registrySourceBeanDefinition
         * @Description: TODO(simple description this method what to do.)
         * @author Vinson
         * @date Feb 10, 2023 5:56:47 PM
         * @param annoAttrs
         * @param registry
         * @return void
         */
        protected void registrySourceBeanDefinition(AnnotationAttributes annoAttrs, BeanDefinitionRegistry registry) {
//            BeanDefinitionBuilder sourceConfigurationBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ZKDBSourceConfiguration.class);
            BeanDefinitionBuilder sourceConfigurationBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(ZKDBSourceConfiguration.class);
            // 没转配置类的一些注册的属性
            sourceConfigurationBeanDefinitionBuilder.addPropertyValue(SqlSessionFactoryKey.typeAliasesPackage, annoAttrs.getString(SqlSessionFactoryKey.typeAliasesPackage));
            sourceConfigurationBeanDefinitionBuilder.addPropertyValue(SqlSessionFactoryKey.typeAliasesSuperType, annoAttrs.getClass(SqlSessionFactoryKey.typeAliasesSuperType));
            sourceConfigurationBeanDefinitionBuilder.addPropertyValue(SqlSessionFactoryKey.mapperLocations, annoAttrs.getStringArray(SqlSessionFactoryKey.mapperLocations));
            sourceConfigurationBeanDefinitionBuilder.addPropertyValue(SqlSessionFactoryKey.configLocation, annoAttrs.getString(SqlSessionFactoryKey.configLocation));
//            sourceConfigurationBeanDefinitionBuilder.addDependsOn("zkEnvironment");
//            sourceConfigurationBeanDefinitionBuilder.addDependsOn("zkDBProperties");
//            sourceConfigurationBeanDefinitionBuilder.addPropertyValue("resourceLoader", this.resourceLoader);
            AbstractBeanDefinition sourceBeanDefinition = sourceConfigurationBeanDefinitionBuilder.getRawBeanDefinition();
//            sourceBeanDefinition.setDependsOn("zkEnvironment", "zkDBProperties");
            registry.registerBeanDefinition(ZKDBSourceConfiguration.class.getName(), sourceBeanDefinition);
        }
        
        /**
         * 注册 mapper 配置类
         *
         * @Title: registryMapperScannerBeanDefinition
         * @Description: TODO(simple description this method what to do.)
         * @author Vinson
         * @date Feb 10, 2023 6:24:05 PM
         * @param annoAttrs
         * @param registry
         * @return void
         */
        protected void registryMapperScannerBeanDefinition(AnnotationAttributes annoAttrs,
                BeanDefinitionRegistry registry) {
         // 添加 ZKMapperScannerConfiguration bean 定义
            BeanDefinitionBuilder mapperScannerConfigurationBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ZKDBMapperScannerConfiguration.class);
            mapperScannerConfigurationBeanDefinitionBuilder.addPropertyValue(MapperKey.basePackage, annoAttrs.getString(MapperKey.basePackage));
            mapperScannerConfigurationBeanDefinitionBuilder.addPropertyValue(MapperKey.annotationClass, annoAttrs.getClass(MapperKey.annotationClass));
            mapperScannerConfigurationBeanDefinitionBuilder.addDependsOn(ZKDBSourceConfiguration.class.getName());
            AbstractBeanDefinition mapperScannerBeanDefinition = mapperScannerConfigurationBeanDefinitionBuilder.getBeanDefinition();
            registry.registerBeanDefinition(ZKDBMapperScannerConfiguration.class.getName(), mapperScannerBeanDefinition);
        }

//        // 继承 ImportBeanDefinitionRegistrar 后，不再生效
//        @Autowired
//        public void testAutowired() {
//            System.out.println("==================");
//        }
//
//        // 继承 ImportBeanDefinitionRegistrar 后，不再生效
//        @Bean
//        public ZKDbSourceConfiguration testBean() {
//            return new ZKDbSourceConfiguration();
//        }

    }

    /**
     * 注解属性名称定义
     * 
     * @ClassName: AnnotationKey
     * @Description: TODO(simple description this class what to do. )
     * @author Vinson
     * @version 1.0
     */
    public interface AnnotationKey {
        
        public interface MapperKey {
            public static final String basePackage = "basePackage";

            public static final String annotationClass = "annotationClass";
        }

        public interface SqlSessionFactoryKey {
            public static final String typeAliasesPackage = "typeAliasesPackage";

            public static final String typeAliasesSuperType = "typeAliasesSuperType";

            public static final String mapperLocations = "mapperLocations";

            public static final String configLocation = "configLocation";
        }
    }

}











