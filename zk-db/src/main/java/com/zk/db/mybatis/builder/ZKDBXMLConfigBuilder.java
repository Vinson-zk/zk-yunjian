package com.zk.db.mybatis.builder;

import java.io.InputStream;
import java.io.Reader;
import java.util.Iterator;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.BuilderException;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.datasource.DataSourceFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.loader.ProxyFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.io.VFS;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaClass;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.AutoMappingUnknownColumnBehavior;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.LocalCacheScope;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.type.JdbcType;

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
 * @ClassName ZKDBXMLConfigBuilder
 * @Package com.zk.db.mybatis.builder
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-10-07 09:53:14
 **/
public class ZKDBXMLConfigBuilder extends BaseBuilder {

    private boolean parsed;
    private final XPathParser parser;
    private String environment;
    private final ReflectorFactory localReflectorFactory;

    public ZKDBXMLConfigBuilder(Reader reader) {
        this((Reader)reader, (String)null, (Properties)null);
    }

    public ZKDBXMLConfigBuilder(Reader reader, String environment) {
        this((Reader)reader, environment, (Properties)null);
    }

    public ZKDBXMLConfigBuilder(Reader reader, String environment, Properties props) {
        this(new XPathParser(reader, true, props, new XMLMapperEntityResolver()), environment, props);
    }

    public ZKDBXMLConfigBuilder(InputStream inputStream) {
        this((InputStream)inputStream, (String)null, (Properties)null);
    }

    public ZKDBXMLConfigBuilder(InputStream inputStream, String environment) {
        this((InputStream)inputStream, environment, (Properties)null);
    }

    public ZKDBXMLConfigBuilder(InputStream inputStream, String environment, Properties props) {
        this(new XPathParser(inputStream, true, props, new XMLMapperEntityResolver()), environment, props);
    }

    private ZKDBXMLConfigBuilder(XPathParser parser, String environment, Properties props) {
        super(new ZKDBMybatisConfiguration());
        this.localReflectorFactory = new DefaultReflectorFactory();
        ErrorContext.instance().resource("SQL Mapper Configuration");
        this.configuration.setVariables(props);
        this.parsed = false;
        this.environment = environment;
        this.parser = parser;
    }

    public Configuration parse() {
        if (this.parsed) {
            throw new BuilderException("Each XMLConfigBuilder can only be used once.");
        } else {
            this.parsed = true;
            this.parseConfiguration(this.parser.evalNode("/configuration"));
            return this.configuration;
        }
    }

    private void parseConfiguration(XNode root) {
        try {
            this.propertiesElement(root.evalNode("properties"));
            Properties settings = this.settingsAsProperties(root.evalNode("settings"));
            this.loadCustomVfs(settings);
            this.loadCustomLogImpl(settings);
            this.typeAliasesElement(root.evalNode("typeAliases"));
            this.pluginElement(root.evalNode("plugins"));
            this.objectFactoryElement(root.evalNode("objectFactory"));
            this.objectWrapperFactoryElement(root.evalNode("objectWrapperFactory"));
            this.reflectorFactoryElement(root.evalNode("reflectorFactory"));
            this.settingsElement(settings);
            this.environmentsElement(root.evalNode("environments"));
            this.databaseIdProviderElement(root.evalNode("databaseIdProvider"));
            this.typeHandlerElement(root.evalNode("typeHandlers"));
            this.mapperElement(root.evalNode("mappers"));
        } catch (Exception var3) {
            throw new BuilderException("Error parsing SQL Mapper Configuration. Cause: " + var3, var3);
        }
    }

    private Properties settingsAsProperties(XNode context) {
        if (context == null) {
            return new Properties();
        } else {
            Properties props = context.getChildrenAsProperties();
            MetaClass metaConfig = MetaClass.forClass(Configuration.class, this.localReflectorFactory);
            Iterator<?> var4 = props.keySet().iterator();

            Object key;
            do {
                if (!var4.hasNext()) {
                    return props;
                }

                key = var4.next();
            } while(metaConfig.hasSetter(String.valueOf(key)));

            throw new BuilderException("The setting " + key + " is not known.  Make sure you spelled it correctly (case sensitive).");
        }
    }

    private void loadCustomVfs(Properties props) throws ClassNotFoundException {
        String value = props.getProperty("vfsImpl");
        if (value != null) {
            String[] clazzes = value.split(",");
            String[] var4 = clazzes;
            int var5 = clazzes.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String clazz = var4[var6];
                if (!clazz.isEmpty()) {
                    @SuppressWarnings("unchecked")
                    Class<? extends VFS> vfsImpl = (Class<? extends VFS>) Resources.classForName(clazz);
                    this.configuration.setVfsImpl(vfsImpl);
                }
            }
        }

    }

    private void loadCustomLogImpl(Properties props) {
        Class<? extends Log> logImpl = this.resolveClass(props.getProperty("logImpl"));
        this.configuration.setLogImpl(logImpl);
    }

    private void typeAliasesElement(XNode parent) {
        if (parent != null) {
            Iterator<?> var2 = parent.getChildren().iterator();

            while(var2.hasNext()) {
                XNode child = (XNode)var2.next();
                String alias;
                if ("package".equals(child.getName())) {
                    alias = child.getStringAttribute("name");
                    this.configuration.getTypeAliasRegistry().registerAliases(alias);
                } else {
                    alias = child.getStringAttribute("alias");
                    String type = child.getStringAttribute("type");

                    try {
                        Class<?> clazz = Resources.classForName(type);
                        if (alias == null) {
                            this.typeAliasRegistry.registerAlias(clazz);
                        } else {
                            this.typeAliasRegistry.registerAlias(alias, clazz);
                        }
                    } catch (ClassNotFoundException var7) {
                        throw new BuilderException("Error registering typeAlias for '" + alias + "'. Cause: " + var7, var7);
                    }
                }
            }
        }

    }

    private void pluginElement(XNode parent) throws Exception {
        if (parent != null) {
            Iterator<?> var2 = parent.getChildren().iterator();

            while(var2.hasNext()) {
                XNode child = (XNode)var2.next();
                String interceptor = child.getStringAttribute("interceptor");
                Properties properties = child.getChildrenAsProperties();
                Interceptor interceptorInstance = (Interceptor)this.resolveClass(interceptor).getDeclaredConstructor().newInstance();
                interceptorInstance.setProperties(properties);
                this.configuration.addInterceptor(interceptorInstance);
            }
        }

    }

    private void objectFactoryElement(XNode context) throws Exception {
        if (context != null) {
            String type = context.getStringAttribute("type");
            Properties properties = context.getChildrenAsProperties();
            ObjectFactory factory = (ObjectFactory)this.resolveClass(type).getDeclaredConstructor().newInstance();
            factory.setProperties(properties);
            this.configuration.setObjectFactory(factory);
        }

    }

    private void objectWrapperFactoryElement(XNode context) throws Exception {
        if (context != null) {
            String type = context.getStringAttribute("type");
            ObjectWrapperFactory factory = (ObjectWrapperFactory)this.resolveClass(type).getDeclaredConstructor().newInstance();
            this.configuration.setObjectWrapperFactory(factory);
        }

    }

    private void reflectorFactoryElement(XNode context) throws Exception {
        if (context != null) {
            String type = context.getStringAttribute("type");
            ReflectorFactory factory = (ReflectorFactory)this.resolveClass(type).getDeclaredConstructor().newInstance();
            this.configuration.setReflectorFactory(factory);
        }

    }

    private void propertiesElement(XNode context) throws Exception {
        if (context != null) {
            Properties defaults = context.getChildrenAsProperties();
            String resource = context.getStringAttribute("resource");
            String url = context.getStringAttribute("url");
            if (resource != null && url != null) {
                throw new BuilderException("The properties element cannot specify both a URL and a resource based property file reference.  Please specify one or the other.");
            }

            if (resource != null) {
                defaults.putAll(Resources.getResourceAsProperties(resource));
            } else if (url != null) {
                defaults.putAll(Resources.getUrlAsProperties(url));
            }

            Properties vars = this.configuration.getVariables();
            if (vars != null) {
                defaults.putAll(vars);
            }

            this.parser.setVariables(defaults);
            this.configuration.setVariables(defaults);
        }

    }

    private void settingsElement(Properties props) {
        this.configuration.setAutoMappingBehavior(AutoMappingBehavior.valueOf(props.getProperty("autoMappingBehavior", "PARTIAL")));
        this.configuration.setAutoMappingUnknownColumnBehavior(AutoMappingUnknownColumnBehavior.valueOf(props.getProperty("autoMappingUnknownColumnBehavior", "NONE")));
        this.configuration.setCacheEnabled(this.booleanValueOf(props.getProperty("cacheEnabled"), true));
        this.configuration.setProxyFactory((ProxyFactory)this.createInstance(props.getProperty("proxyFactory")));
        this.configuration.setLazyLoadingEnabled(this.booleanValueOf(props.getProperty("lazyLoadingEnabled"), false));
        this.configuration.setAggressiveLazyLoading(this.booleanValueOf(props.getProperty("aggressiveLazyLoading"), false));
        this.configuration.setMultipleResultSetsEnabled(this.booleanValueOf(props.getProperty("multipleResultSetsEnabled"), true));
        this.configuration.setUseColumnLabel(this.booleanValueOf(props.getProperty("useColumnLabel"), true));
        this.configuration.setUseGeneratedKeys(this.booleanValueOf(props.getProperty("useGeneratedKeys"), false));
        this.configuration.setDefaultExecutorType(ExecutorType.valueOf(props.getProperty("defaultExecutorType", "SIMPLE")));
        this.configuration.setDefaultStatementTimeout(this.integerValueOf(props.getProperty("defaultStatementTimeout"), (Integer)null));
        this.configuration.setDefaultFetchSize(this.integerValueOf(props.getProperty("defaultFetchSize"), (Integer)null));
        this.configuration.setDefaultResultSetType(this.resolveResultSetType(props.getProperty("defaultResultSetType")));
        this.configuration.setMapUnderscoreToCamelCase(this.booleanValueOf(props.getProperty("mapUnderscoreToCamelCase"), false));
        this.configuration.setSafeRowBoundsEnabled(this.booleanValueOf(props.getProperty("safeRowBoundsEnabled"), false));
        this.configuration.setLocalCacheScope(LocalCacheScope.valueOf(props.getProperty("localCacheScope", "SESSION")));
        this.configuration.setJdbcTypeForNull(JdbcType.valueOf(props.getProperty("jdbcTypeForNull", "OTHER")));
        this.configuration.setLazyLoadTriggerMethods(this.stringSetValueOf(props.getProperty("lazyLoadTriggerMethods"), "equals,clone,hashCode,toString"));
        this.configuration.setSafeResultHandlerEnabled(this.booleanValueOf(props.getProperty("safeResultHandlerEnabled"), true));
        this.configuration.setDefaultScriptingLanguage(this.resolveClass(props.getProperty("defaultScriptingLanguage")));
        this.configuration.setDefaultEnumTypeHandler(this.resolveClass(props.getProperty("defaultEnumTypeHandler")));
        this.configuration.setCallSettersOnNulls(this.booleanValueOf(props.getProperty("callSettersOnNulls"), false));
        this.configuration.setUseActualParamName(this.booleanValueOf(props.getProperty("useActualParamName"), true));
        this.configuration.setReturnInstanceForEmptyRow(this.booleanValueOf(props.getProperty("returnInstanceForEmptyRow"), false));
        this.configuration.setLogPrefix(props.getProperty("logPrefix"));
        this.configuration.setConfigurationFactory(this.resolveClass(props.getProperty("configurationFactory")));
        this.configuration.setShrinkWhitespacesInSql(this.booleanValueOf(props.getProperty("shrinkWhitespacesInSql"), false));
        this.configuration.setDefaultSqlProviderType(this.resolveClass(props.getProperty("defaultSqlProviderType")));
    }

    private void environmentsElement(XNode context) throws Exception {
        if (context != null) {
            if (this.environment == null) {
                this.environment = context.getStringAttribute("default");
            }

            Iterator<?> var2 = context.getChildren().iterator();

            while(var2.hasNext()) {
                XNode child = (XNode)var2.next();
                String id = child.getStringAttribute("id");
                if (this.isSpecifiedEnvironment(id)) {
                    TransactionFactory txFactory = this.transactionManagerElement(child.evalNode("transactionManager"));
                    DataSourceFactory dsFactory = this.dataSourceElement(child.evalNode("dataSource"));
                    DataSource dataSource = dsFactory.getDataSource();
                    Environment.Builder environmentBuilder = (new Environment.Builder(id)).transactionFactory(txFactory).dataSource(dataSource);
                    this.configuration.setEnvironment(environmentBuilder.build());
                }
            }
        }

    }

    private void databaseIdProviderElement(XNode context) throws Exception {
        DatabaseIdProvider databaseIdProvider = null;
        if (context != null) {
            String type = context.getStringAttribute("type");
            if ("VENDOR".equals(type)) {
                type = "DB_VENDOR";
            }

            Properties properties = context.getChildrenAsProperties();
            databaseIdProvider = (DatabaseIdProvider)this.resolveClass(type).getDeclaredConstructor().newInstance();
            databaseIdProvider.setProperties(properties);
        }

        Environment environment = this.configuration.getEnvironment();
        if (environment != null && databaseIdProvider != null) {
            String databaseId = databaseIdProvider.getDatabaseId(environment.getDataSource());
            this.configuration.setDatabaseId(databaseId);
        }

    }

    private TransactionFactory transactionManagerElement(XNode context) throws Exception {
        if (context != null) {
            String type = context.getStringAttribute("type");
            Properties props = context.getChildrenAsProperties();
            TransactionFactory factory = (TransactionFactory)this.resolveClass(type).getDeclaredConstructor().newInstance();
            factory.setProperties(props);
            return factory;
        } else {
            throw new BuilderException("Environment declaration requires a TransactionFactory.");
        }
    }

    private DataSourceFactory dataSourceElement(XNode context) throws Exception {
        if (context != null) {
            String type = context.getStringAttribute("type");
            Properties props = context.getChildrenAsProperties();
            DataSourceFactory factory = (DataSourceFactory)this.resolveClass(type).getDeclaredConstructor().newInstance();
            factory.setProperties(props);
            return factory;
        } else {
            throw new BuilderException("Environment declaration requires a DataSourceFactory.");
        }
    }

    private void typeHandlerElement(XNode parent) {
        if (parent != null) {
            Iterator<?> var2 = parent.getChildren().iterator();

            while(var2.hasNext()) {
                XNode child = (XNode)var2.next();
                String typeHandlerPackage;
                if ("package".equals(child.getName())) {
                    typeHandlerPackage = child.getStringAttribute("name");
                    this.typeHandlerRegistry.register(typeHandlerPackage);
                } else {
                    typeHandlerPackage = child.getStringAttribute("javaType");
                    String jdbcTypeName = child.getStringAttribute("jdbcType");
                    String handlerTypeName = child.getStringAttribute("handler");
                    Class<?> javaTypeClass = this.resolveClass(typeHandlerPackage);
                    JdbcType jdbcType = this.resolveJdbcType(jdbcTypeName);
                    Class<?> typeHandlerClass = this.resolveClass(handlerTypeName);
                    if (javaTypeClass != null) {
                        if (jdbcType == null) {
                            this.typeHandlerRegistry.register(javaTypeClass, typeHandlerClass);
                        } else {
                            this.typeHandlerRegistry.register(javaTypeClass, jdbcType, typeHandlerClass);
                        }
                    } else {
                        this.typeHandlerRegistry.register(typeHandlerClass);
                    }
                }
            }
        }

    }

    private void mapperElement(XNode parent) throws Exception {
        if (parent != null) {
            Iterator<?> var2 = parent.getChildren().iterator();

            while(true) {
                while(var2.hasNext()) {
                    XNode child = (XNode)var2.next();
                    String resource;
                    if ("package".equals(child.getName())) {
                        resource = child.getStringAttribute("name");
                        this.configuration.addMappers(resource);
                    } else {
                        resource = child.getStringAttribute("resource");
                        String url = child.getStringAttribute("url");
                        String mapperClass = child.getStringAttribute("class");
                        XMLMapperBuilder mapperParser;
                        InputStream inputStream;
                        if (resource != null && url == null && mapperClass == null) {
                            ErrorContext.instance().resource(resource);
                            inputStream = Resources.getResourceAsStream(resource);
                            mapperParser = new XMLMapperBuilder(inputStream, this.configuration, resource, this.configuration.getSqlFragments());
                            mapperParser.parse();
                        } else if (resource == null && url != null && mapperClass == null) {
                            ErrorContext.instance().resource(url);
                            inputStream = Resources.getUrlAsStream(url);
                            mapperParser = new XMLMapperBuilder(inputStream, this.configuration, url, this.configuration.getSqlFragments());
                            mapperParser.parse();
                        } else {
                            if (resource != null || url != null || mapperClass == null) {
                                throw new BuilderException("A mapper element may only specify a url, resource or class, but not more than one.");
                            }

                            Class<?> mapperInterface = Resources.classForName(mapperClass);
                            this.configuration.addMapper(mapperInterface);
                        }
                    }
                }

                return;
            }
        }
    }

    private boolean isSpecifiedEnvironment(String id) {
        if (this.environment == null) {
            throw new BuilderException("No environment specified.");
        } else if (id == null) {
            throw new BuilderException("Environment requires an id attribute.");
        } else {
            return this.environment.equals(id);
        }
    }

}
