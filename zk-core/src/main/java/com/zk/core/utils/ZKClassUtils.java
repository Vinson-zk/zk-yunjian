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
 * @Title: ZKClassUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:32:27 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jmx.export.UnableToRegisterMBeanException;
import org.springframework.util.ClassUtils;

/**
 * Introspector java 内省；Introspector.getBeanInfo(Class<?>);
 * 
 * @ClassName: ZKClassUtils
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKClassUtils extends ClassUtils {

    protected static Logger log = LogManager.getLogger(ZKClassUtils.class);

    public static interface Param_Name {
        /**
         * 属性分隔符
         */
        public static final String attributeSeparator = ".";
    }

    /**
     * @since 1.0
     */
    private static final ClassLoaderAccessor THREAD_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() throws Throwable {
            return Thread.currentThread().getContextClassLoader();
        }
    };

    /**
     * @since 1.0
     */
    private static final ClassLoaderAccessor CLASS_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() throws Throwable {
            return ZKClassUtils.class.getClassLoader();
        }
    };

    /**
     * @since 1.0
     */
    private static final ClassLoaderAccessor SYSTEM_CL_ACCESSOR = new ExceptionIgnoringAccessor() {
        @Override
        protected ClassLoader doGetClassLoader() throws Throwable {
            return ClassLoader.getSystemClassLoader();
        }
    };

    /**
     * @since 1.0
     */
    private static interface ClassLoaderAccessor {
        @SuppressWarnings("rawtypes")
        Class loadClass(String classNameStr);

        InputStream getResourceStream(String name);
    }

    /**
     * @since 1.0
     */
    private static abstract class ExceptionIgnoringAccessor implements ClassLoaderAccessor {

        @Override
        @SuppressWarnings("rawtypes")
        public Class loadClass(String classNameStr) {
            Class clazz = null;
            ClassLoader cl = getClassLoader();
            if (cl != null) {
                try {
                    clazz = cl.loadClass(classNameStr);
                }
                catch(ClassNotFoundException e) {
                    if (log.isTraceEnabled()) {
                        log.trace(
                                "Unable to load clazz named [" + classNameStr + "] from class loader [" + cl + "]");
                    }
                }
            }
            return clazz;
        }

        public InputStream getResourceStream(String name) {
            InputStream is = null;
            ClassLoader cl = getClassLoader();
            if (cl != null) {
                is = cl.getResourceAsStream(name);
            }
            return is;
        }

        protected final ClassLoader getClassLoader() {
            try {
                return doGetClassLoader();
            }
            catch(Throwable t) {
                if (log.isDebugEnabled()) {
                    log.debug("Unable to acquire ClassLoader.", t);
                }
            }
            return null;
        }

        protected abstract ClassLoader doGetClassLoader() throws Throwable;
    }

    /**************************************************** */
    /*** java class 字段 */
    /**************************************************** */

    /**
     * 获取类所有的定义的字段【取不到只定义 getter 方法的属性】，包括父类的
     * 
     * @param classz
     * @return
     */
    public static List<Field> getAllField(Class<?> classz) {
        List<Field> rfs = new ArrayList<Field>();
        Field[] fields = classz.getDeclaredFields();
        for (Field f : fields) {
            rfs.add(f);
        }

        if (classz.getSuperclass() != null) {
            rfs.addAll(getAllField(classz.getSuperclass()));
        }

        return rfs;
    }

    /**
     * 获取类所有的定义的字段【取不到只定义 getter 方法的属性】，包括父类的
     * 
     * @param classz
     * @return
     */
    public static Field getField(Class<?> classz, final String fieldName) {
        List<Field> fields = getAllField(classz);
        return getField(fields, fieldName);
    }

    public static Field getField(List<Field> fields, final String fieldName) {
        for (Field f : fields) {
            if (f.getName().equals(fieldName)) {
                return f;
            }
        }
        return null;
    }

    /**
     * 取 java bean 的属性；这个与 getField 方法不同，这里可以取只定义 getter 方法的属性；
     *
     * @Title: getPropertys
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 17, 2020 11:41:46 AM
     * @param classz
     * @return
     * @throws IntrospectionException
     * @return PropertyDescriptor[]
     */
    public static PropertyDescriptor[] getAllProperty(Class<?> classz) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(classz);
        return beanInfo.getPropertyDescriptors();
    }

    public static PropertyDescriptor getProperty(Class<?> classz, final String fieldName)
            throws IntrospectionException {
        return getProperty(getAllProperty(classz), fieldName);
    }

    public static PropertyDescriptor getProperty(PropertyDescriptor[] propertyDescriptors, final String fieldName)
            throws IntrospectionException {
        for (PropertyDescriptor pd : propertyDescriptors) {
            if (pd.getName().equals(fieldName)) {
                return pd;
            }
        }
        return null;
    }

    public static Method getReadMethod(Class<?> classz, final String arrtName) throws IntrospectionException {
        return getReadMethod(classz, arrtName, true);
    }
    
    /**
     * 取 Bean 指定属性的 get Method
     *
     * @Title: getReadMethod
     * @author Vinson
     * @date Sep 10, 2020 3:30:16 PM
     * @param classz
     * @param fieldName
     * @param isIgnoreException
     *            未找到方法是否抛出异常；false-抛出异常；true-忽略异常，返回 null
     * @throws IntrospectionException
     * @return Method
     */
    public static Method getReadMethod(Class<?> classz, final String fieldName, boolean isIgnoreException)
            throws IntrospectionException {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(fieldName, classz);
            return descriptor.getReadMethod();
        }
        catch(IntrospectionException e) {
            if (!isIgnoreException) {
                throw e;
            }
            else {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Method getWriteMethod(Class<?> classz, final String fieldName) throws IntrospectionException {
        return getWriteMethod(classz, fieldName, true);
    }

    /**
     * 取 Bean 指定属性的 set Method
     *
     * @Title: getWriteMethod
     * @author Vinson
     * @date Sep 10, 2020 3:30:45 PM
     * @param classz
     * @param fieldName
     * @param isIgnoreException
     *            未找到方法是否抛出异常；false-抛出异常；true-忽略异常，返回 null
     * @throws IntrospectionException
     * @return Method
     */
    public static Method getWriteMethod(Class<?> classz, final String fieldName, boolean isIgnoreException)
            throws IntrospectionException {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(fieldName, classz);
            return descriptor.getWriteMethod();
        }
        catch(IntrospectionException e) {
            if (!isIgnoreException) {
                throw e;
            }
        }
        return null;
    }

    /**
     * 把值转化为相应类型
     * 
     * @param vClass
     * @param value
     * @return
     */
    public static Object getValueByClass(Class<?> vClass, Object value) {
        if (value == null)
            return null;
        if (vClass == null)
            return value;

        if (vClass.equals(value.getClass())) {
            return value;
        }

        try {
            if (Integer.class.equals(vClass) || int.class.equals(vClass)) {
                value = Integer.valueOf(value.toString());
            }
            else if (Float.class.equals(vClass) || float.class.equals(vClass)) {
                value = Float.valueOf(value.toString());
            }
            else if (Double.class.equals(vClass) || double.class.equals(vClass)) {
                value = Double.valueOf(value.toString());
            }
            else if (Long.class.equals(vClass) || long.class.equals(vClass)) {
                value = Long.valueOf(value.toString());
            }
            else if (Date.class.equals(vClass)) {
                String fStr = null;
                if (String.class.equals(value.getClass())) {
                    if (value.toString().indexOf("[") != -1) {
                        // 日期转换格式
                        fStr = value.toString().substring(value.toString().indexOf("[") + 1,
                                value.toString().indexOf("]"));
                    }
                }
                if (fStr != null) {
                    // 按格式转换日期 yyyy-MM-dd HH:mm:ss.SSSSSS
                    value = new SimpleDateFormat(fStr)
                            .parse(value.toString().substring(0, value.toString().indexOf("[")));
                }
                else {
                    value = new Date(Long.valueOf(value.toString()));
                }
            }
            return value;
        }
        catch(Exception e) {
            e.printStackTrace();
            log.error("getTypeValue() value: " + value + " to " + vClass.getName() + " error ");
            log.error(e.getMessage());
            return null;
        }
    }

    /**************************************************** */
    /*** java Bean 对象 反射 */
    /**************************************************** */

    /**
     * 取对象指定字段值
     * 
     * @param obj
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     */
    public static Object getFieldValue(final Object obj, final String fieldName) throws IllegalAccessException {
        Field f = getField(obj.getClass(), fieldName);
        if (f == null) {
            throw new IllegalAccessException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        makeAccessible(f);
        return f.get(obj);
    }

    /**
     * 设置对象指定字段值
     * 
     * @param obj
     * @param fieldName
     * @return
     * @throws IllegalAccessException
     */
    public static void setFieldValue(final Object obj, final String fieldName, final Object value)
            throws IllegalAccessException {
        Field f = getField(obj.getClass(), fieldName);
        if (f == null) {
            throw new IllegalAccessException("Could not find field [" + fieldName + "] on target [" + obj + "]");
        }
        makeAccessible(f);
        f.set(obj, value);
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager 报错。
     */
    @SuppressWarnings("deprecation")
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static InputStream getResourceAsStream(String name) {

        InputStream is = THREAD_CL_ACCESSOR.getResourceStream(name);

        if (is == null) {
            if (log.isTraceEnabled()) {
                log.trace("Resource [" + name + "] was not found via the thread context ClassLoader.  Trying the "
                        + "current ClassLoader...");
            }
            is = CLASS_CL_ACCESSOR.getResourceStream(name);
        }

        if (is == null) {
            if (log.isTraceEnabled()) {
                log.trace("Resource [" + name + "] was not found via the current class loader.  Trying the "
                        + "system/application ClassLoader...");
            }
            is = SYSTEM_CL_ACCESSOR.getResourceStream(name);
        }

        if (is == null && log.isTraceEnabled()) {
            log.trace("Resource [" + name + "] was not found via the thread context, current, or "
                    + "system/application ClassLoaders.  All heuristics have been exhausted.  Returning null.");
        }

        return is;
    }

    /**************************************************** */
    /*** 取类支持的泛弄类名 Class<?> */
    /**************************************************** */
    /**
     * 取泛型类的类
     *
     * @Title: getSuperclassByName
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 4, 2022 11:58:59 AM
     * @param parentClassz
     *            泛型所在的父类
     * @param realClassz
     *            实现 真实的类
     * @param genericityTypeName
     *            要取类的泛型类的名称
     * @return
     * @return Class<?>
     */
    @SuppressWarnings("unchecked")
    public static <C> Class<C> getSuperclassByName(Class<?> parentClassz, Class<?> realClassz, String genericityTypeName) {

        Type t = getTypeBySuperclassAndName(parentClassz, realClassz, genericityTypeName);
        if(t != null && t instanceof Class){
            return (Class<C>)t;
        }
        return null;
    }

    protected static Type getTypeBySuperclassAndName(Class<?> parentClassz, Class<?> realClassz,
        String genericityTypeName) {

//        log.info("[^_^:20221012-1635-001] getSuperclassByName, parentClassz:{}, realClassz: {}, genericityTypeName: {}",
//            parentClassz.getName(), realClassz.getName(), genericityTypeName);
        Type resType = null;
        Class<?> rawType = null;
        TypeVariable<?>[] tvs = null;
        // 取出 子类或接口 支持的所有 泛型父类和接口
        Set<Type> types = new HashSet<>();
        if(!realClassz.isInterface()){
            types.add(realClassz.getGenericSuperclass());
        }
        if(parentClassz.isInterface()){
            types.addAll(Arrays.asList(realClassz.getGenericInterfaces()));
        }
        for(Type item:types){
            rawType = getClassByType(item);
            if(rawType == null){
                // 如果泛型 实现类取上级类为空，跳过；
                continue;
            }
            if(parentClassz.getName().equals(rawType.getName())){
                // rawType 是 parentClassz 的一级子类
                tvs = parentClassz.getTypeParameters();
                if(item instanceof ParameterizedType) {
                    resType = getTypeByParameterizedType((ParameterizedType) item, tvs, genericityTypeName);
                }
            }else{
                if(parentClassz.isAssignableFrom(rawType)){
                    // rawType 是 parentClassz 的子类或子接口，继续往下找
                    resType = getTypeBySuperclassAndName(parentClassz, rawType, genericityTypeName);
                    if(resType != null && resType instanceof TypeVariable){
                        TypeVariable<?> tv = (TypeVariable<?>) resType;
                        genericityTypeName = tv.getName();
                        tvs = rawType.getTypeParameters();
                        resType = getTypeByParameterizedType((ParameterizedType)item, tvs, genericityTypeName);
                    }
                    // 找一个泛型的实现类即可，不用一直循环找；
                    break;
                }
            }
        }
        return resType;
    }

    protected static Type getTypeByParameterizedType(ParameterizedType pt, TypeVariable<?>[] ts,
        String genericityTypeName) {
        Type[] cTypes = pt.getActualTypeArguments();
//        for (int i = 0; i < ts.length; ++i) {
//            log.info("[^_^:20221007-1124-001] parentClassz.ts: {}.{}: {}", genericityTypeName, i, ts[i].getName());
//        }
//        for (int i = 0; i < cTypes.length; ++i) {
//            log.info("[^_^:20221007-1124-001] realClassz.cTypes: {}.{}: {}", genericityTypeName, i,
//                cTypes[i].getTypeName());
//        }
        for (int i = 0; i < ts.length; ++i) {
            if (genericityTypeName.equals(ts[i].getName())) {
//                 log.info("[^_^:20221007-1121-001] genericityTypeName:{}, i:{}", genericityTypeName, i);
                return cTypes[i];
            }
        }
        return null;
    }

    protected static Class<?> getClassByType(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class<?>)((ParameterizedType)type).getRawType();
        } else if (type instanceof Class) {
            return (Class<?>)type;
        } else{
            return null;
        }
    }

    /**************************************************** */
    /*** 根据类名实例化 */
    /**************************************************** */
    /**
     * 根据类名实例化
     * 
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    public static <E> E newInstance(String classNameStr) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return newInstance(forName(classNameStr));
    }

    public static <E> E newInstance(String classNameStr, Object... args)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return newInstance(forName(classNameStr), args);
    }

    @SuppressWarnings("unchecked")
    public static <E> E newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (clazz == null) {
            String msg = "Class method parameter cannot be null.";
            throw new IllegalArgumentException(msg);
        }
//        return (E) clazz.newInstance();
        return (E) clazz.getDeclaredConstructor().newInstance();
    }

    public static <E> E newInstance(Class<?> clazz, Object... args)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?>[] argTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Constructor<?> ctor = getConstructor(clazz, argTypes);
        return instantiate(ctor, args);
    }

    public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... argTypes) {
        try {
            return clazz.getConstructor(argTypes);
        }
        catch(NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }

    }

    @SuppressWarnings("unchecked")
    public static <E> E instantiate(Constructor<?> ctor, Object... args)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return (E) ctor.newInstance(args);
    }

    public static Class<?> forName(String classNameStr) {

        Class<?> clazz = THREAD_CL_ACCESSOR.loadClass(classNameStr);

        if (clazz == null) {
            if (log.isTraceEnabled()) {
                log.trace("Unable to load class named [" + classNameStr
                        + "] from the thread context ClassLoader.  Trying the current ClassLoader...");
            }
            clazz = CLASS_CL_ACCESSOR.loadClass(classNameStr);
        }

        if (clazz == null) {
            if (log.isTraceEnabled()) {
                log.trace("Unable to load class named [" + classNameStr + "] from the current ClassLoader.  "
                        + "Trying the system/application ClassLoader...");
            }
            clazz = SYSTEM_CL_ACCESSOR.loadClass(classNameStr);
        }

        if (clazz == null) {
            String msg = "Unable to load class named [" + classNameStr + "] from the thread context, current, or "
                    + "system/application ClassLoaders.  All heuristics have been exhausted.  Class could not be found.";
            throw new UnableToRegisterMBeanException(msg);
        }

        return clazz;
    }

}
