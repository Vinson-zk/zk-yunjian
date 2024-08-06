package com.zk.db.mybatis.spring;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.zk.db.mybatis.builder.ZKDBXMLConfigBuilder;
import com.zk.db.mybatis.session.ZKDBMybatisConfiguration;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description:
 * @ClassName ZKDBSqlSessionFactoryBean
 * @Package com.zk.db.mybatis.spring
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-10-07 01:06:47
 **/
public class ZKDBSqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {
    private static final Log LOGGER = LogFactory.getLog(SqlSessionFactoryBean.class);
    private Resource configLocation;
    private Configuration configuration;
    private Resource[] mapperLocations;
    private DataSource dataSource;
    private TransactionFactory transactionFactory;
    private Properties configurationProperties;
    private SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
    private SqlSessionFactory sqlSessionFactory;
    private String environment = SqlSessionFactoryBean.class.getSimpleName();
    private boolean failFast;
    private Interceptor[] plugins;
    private TypeHandler<?>[] typeHandlers;
    private String typeHandlersPackage;
    private Class<?>[] typeAliases;
    private String typeAliasesPackage;
    private Class<?> typeAliasesSuperType;
    private DatabaseIdProvider databaseIdProvider;
    private Class<? extends VFS> vfs;
    private Cache cache;
    private ObjectFactory objectFactory;
    private ObjectWrapperFactory objectWrapperFactory;

    public ZKDBSqlSessionFactoryBean() {
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
        this.objectWrapperFactory = objectWrapperFactory;
    }

    public DatabaseIdProvider getDatabaseIdProvider() {
        return this.databaseIdProvider;
    }

    public void setDatabaseIdProvider(DatabaseIdProvider databaseIdProvider) {
        this.databaseIdProvider = databaseIdProvider;
    }

    public Class<? extends VFS> getVfs() {
        return this.vfs;
    }

    public void setVfs(Class<? extends VFS> vfs) {
        this.vfs = vfs;
    }

    public Cache getCache() {
        return this.cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public void setPlugins(Interceptor[] plugins) {
        this.plugins = plugins;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType) {
        this.typeAliasesSuperType = typeAliasesSuperType;
    }

    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    public void setTypeHandlers(TypeHandler<?>[] typeHandlers) {
        this.typeHandlers = typeHandlers;
    }

    public void setTypeAliases(Class<?>[] typeAliases) {
        this.typeAliases = typeAliases;
    }

    public void setFailFast(boolean failFast) {
        this.failFast = failFast;
    }

    public void setConfigLocation(Resource configLocation) {
        this.configLocation = configLocation;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setMapperLocations(Resource[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public void setConfigurationProperties(Properties sqlSessionFactoryProperties) {
        this.configurationProperties = sqlSessionFactoryProperties;
    }

    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy) {
            this.dataSource = ((TransactionAwareDataSourceProxy)dataSource).getTargetDataSource();
        } else {
            this.dataSource = dataSource;
        }

    }

    public void setSqlSessionFactoryBuilder(SqlSessionFactoryBuilder sqlSessionFactoryBuilder) {
        this.sqlSessionFactoryBuilder = sqlSessionFactoryBuilder;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.dataSource, "Property 'dataSource' is required");
        Assert.notNull(this.sqlSessionFactoryBuilder, "Property 'sqlSessionFactoryBuilder' is required");
        Assert.state(this.configuration == null && this.configLocation == null || this.configuration == null || this.configLocation == null, "Property 'configuration' and 'configLocation' can not specified with together");
        this.sqlSessionFactory = this.buildSqlSessionFactory();
    }

    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        ZKDBXMLConfigBuilder xmlConfigBuilder = null;
        Configuration configuration;
        if (this.configuration != null) {
            configuration = this.configuration;
            if (configuration.getVariables() == null) {
                configuration.setVariables(this.configurationProperties);
            } else if (this.configurationProperties != null) {
                configuration.getVariables().putAll(this.configurationProperties);
            }
        } else if (this.configLocation != null) {
            xmlConfigBuilder = new ZKDBXMLConfigBuilder(this.configLocation.getInputStream(), (String)null, this.configurationProperties);
            configuration = xmlConfigBuilder.getConfiguration();
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Property 'configuration' or 'configLocation' not specified, using default MyBatis Configuration");
            }

            configuration = new ZKDBMybatisConfiguration();
            if (this.configurationProperties != null) {
                configuration.setVariables(this.configurationProperties);
            }
        }

        if (this.objectFactory != null) {
            configuration.setObjectFactory(this.objectFactory);
        }

        if (this.objectWrapperFactory != null) {
            configuration.setObjectWrapperFactory(this.objectWrapperFactory);
        }

        if (this.vfs != null) {
            configuration.setVfsImpl(this.vfs);
        }

        String[] typeHandlersPackageArray;
        String[] var4;
        int var5;
        int var6;
        String packageToScan;
        if (StringUtils.hasLength(this.typeAliasesPackage)) {
            typeHandlersPackageArray = StringUtils.tokenizeToStringArray(this.typeAliasesPackage, ",; \t\n");
            var4 = typeHandlersPackageArray;
            var5 = typeHandlersPackageArray.length;

            for(var6 = 0; var6 < var5; ++var6) {
                packageToScan = var4[var6];
                configuration.getTypeAliasRegistry().registerAliases(packageToScan, this.typeAliasesSuperType == null ? Object.class : this.typeAliasesSuperType);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Scanned package: '" + packageToScan + "' for aliases");
                }
            }
        }

        int var27;
        if (!ObjectUtils.isEmpty(this.typeAliases)) {
            Class<?>[] var25 = this.typeAliases;
            var27 = var25.length;

            for(var5 = 0; var5 < var27; ++var5) {
                Class<?> typeAlias = var25[var5];
                configuration.getTypeAliasRegistry().registerAlias(typeAlias);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Registered type alias: '" + typeAlias + "'");
                }
            }
        }

        if (!ObjectUtils.isEmpty(this.plugins)) {
            Interceptor[] var26 = this.plugins;
            var27 = var26.length;

            for(var5 = 0; var5 < var27; ++var5) {
                Interceptor plugin = var26[var5];
                configuration.addInterceptor(plugin);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Registered plugin: '" + plugin + "'");
                }
            }
        }

        if (StringUtils.hasLength(this.typeHandlersPackage)) {
            typeHandlersPackageArray = StringUtils.tokenizeToStringArray(this.typeHandlersPackage, ",; \t\n");
            var4 = typeHandlersPackageArray;
            var5 = typeHandlersPackageArray.length;

            for(var6 = 0; var6 < var5; ++var6) {
                packageToScan = var4[var6];
                configuration.getTypeHandlerRegistry().register(packageToScan);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Scanned package: '" + packageToScan + "' for type handlers");
                }
            }
        }

        if (!ObjectUtils.isEmpty(this.typeHandlers)) {
            TypeHandler<?>[] var28 = this.typeHandlers;
            var27 = var28.length;

            for(var5 = 0; var5 < var27; ++var5) {
                TypeHandler<?> typeHandler = var28[var5];
                configuration.getTypeHandlerRegistry().register(typeHandler);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Registered type handler: '" + typeHandler + "'");
                }
            }
        }

        if (this.databaseIdProvider != null) {
            try {
                configuration.setDatabaseId(this.databaseIdProvider.getDatabaseId(this.dataSource));
            }
            catch(SQLException var24) {
                throw new IOException("Failed getting a databaseId", var24);
            }
        }

        if (this.cache != null) {
            configuration.addCache(this.cache);
        }

        if (xmlConfigBuilder != null) {
            try {
                xmlConfigBuilder.parse();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Parsed configuration file: '" + this.configLocation + "'");
                }
            }
            catch(Exception var22) {
                throw new IOException("Failed to parse config resource: " + this.configLocation, var22);
            } finally {
                ErrorContext.instance().reset();
            }
        }

        if (this.transactionFactory == null) {
            this.transactionFactory = new SpringManagedTransactionFactory();
        }

        configuration.setEnvironment(new Environment(this.environment, this.transactionFactory, this.dataSource));
        if (!ObjectUtils.isEmpty(this.mapperLocations)) {
            Resource[] var29 = this.mapperLocations;
            var27 = var29.length;

            for(var5 = 0; var5 < var27; ++var5) {
                Resource mapperLocation = var29[var5];
                if (mapperLocation != null) {
                    try {
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(mapperLocation.getInputStream(), configuration, mapperLocation.toString(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                    }
                    catch(Exception var20) {
                        throw new IOException("Failed to parse mapping resource: '" + mapperLocation + "'",
                                var20);
                    } finally {
                        ErrorContext.instance().reset();
                    }

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Parsed mapper file: '" + mapperLocation + "'");
                    }
                }
            }
        } else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Property 'mapperLocations' was not specified or no matching resources found");
        }

        return this.sqlSessionFactoryBuilder.build(configuration);
    }

    @Override
    public SqlSessionFactory getObject() throws Exception {
        if (this.sqlSessionFactory == null) {
            this.afterPropertiesSet();
        }

        return this.sqlSessionFactory;
    }

    @Override
    public Class<? extends SqlSessionFactory> getObjectType() {
        return this.sqlSessionFactory == null ? SqlSessionFactory.class : this.sqlSessionFactory.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (this.failFast && event instanceof ContextRefreshedEvent) {
            this.sqlSessionFactory.getConfiguration().getMappedStatementNames();
        }

    }
}
