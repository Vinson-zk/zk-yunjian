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
* @Title: ZKSecDefaultFilterChainManager.java 
* @author Vinson 
* @Package com.zk.security.web.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 10:14:30 PM 
* @version V1.0 
*/
package com.zk.security.web.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.utils.ZKCollectionUtils;
import com.zk.core.utils.ZKStringUtils;
import com.zk.security.exception.ZKSecUnknownException;
import com.zk.security.web.filter.authc.ZKSecDefaultFilter;

/** 
* @ClassName: ZKSecDefaultFilterChainManager 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultFilterChainManager implements ZKSecFilterChainManager {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public static final char DEFAULT_DELIMITER_CHAR = ',';

    private Map<String, Filter> filtersMap;

    private Map<String, List<Filter>> filterChainsMap;

    public ZKSecDefaultFilterChainManager() {
        this.filtersMap = new LinkedHashMap<String, Filter>();
        this.filterChainsMap = new LinkedHashMap<String, List<Filter>>();
        addDefaultFilters(false);
    }

    public ZKSecDefaultFilterChainManager(FilterConfig filterConfig) {
        this.filtersMap = new LinkedHashMap<String, Filter>();
        this.filterChainsMap = new LinkedHashMap<String, List<Filter>>();
        setFilterConfig(filterConfig);
        addDefaultFilters(true);
    }

    @Override
    public void createChain(String urlPath, String chainDefinition) {
        if (!ZKStringUtils.hasText(urlPath)) {
            throw new NullPointerException("urlPath cannot be null or empty.");
        }
        if (!ZKStringUtils.hasText(chainDefinition)) {
            throw new NullPointerException("chainDefinition cannot be null or empty.");
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Creating chain [" + urlPath + "] from String definition [" + chainDefinition + "]");
        }

        String[] filterTokens = splitChainDefinition(chainDefinition);

        for (String token : filterTokens) {
            String[] nameConfigPair = toNameConfigPair(token);

            // now we have the filter name, path and (possibly null)
            // path-specific config. Let's apply them:
            addToChain(urlPath, nameConfigPair[0], nameConfigPair[1]);
        }
    }

    @Override
    public ZKSecFilterChain proxy(FilterChain original, String urlPath) {
        List<Filter> chains = getChains(urlPath);
        if (chains == null) {
            String msg = "There is no configured chain under the name/key [" + chains + "].";
            throw new IllegalArgumentException(msg);
        }
        return new ZKSecFilterChain(original, chains);
    }

    @Override
    public boolean hasChains() {
        return ZKCollectionUtils.isEmpty(this.filterChainsMap);
    }

    @Override
    public Set<String> getChainNames() {
        return this.filterChainsMap.keySet();
    }

    /*****************************************/
    protected String[] splitChainDefinition(String chainDefinition) {
        return ZKStringUtils.split(chainDefinition, DEFAULT_DELIMITER_CHAR, '[', ']', true, true);
    }

    protected String[] toNameConfigPair(String token) throws ZKSecUnknownException {
        try {
            String[] pair = token.split("\\[", 2);
            String name = ZKStringUtils.clean(pair[0]);

            if (name == null) {
                throw new IllegalArgumentException("Filter name not found for filter chain definition token: " + token);
            }
            String config = null;

            if (pair.length == 2) {
                config = ZKStringUtils.clean(pair[1]);

                config = config.substring(0, config.length() - 1);
                config = ZKStringUtils.clean(config);

                if (config != null && config.startsWith("\"") && config.endsWith("\"")) {
                    String stripped = config.substring(1, config.length() - 1);
                    stripped = ZKStringUtils.clean(stripped);

                    if (stripped != null && stripped.indexOf('"') == -1) {
                        config = stripped;
                    }

                }
            }
            return new String[] { name, config };
        }
        catch(Exception e) {
            String msg = "Unable to parse filter chain definition token: " + token;
            throw new ZKSecUnknownException(msg, e);
        }
    }

    public void addToChain(String urlPath, String filterName, String chainSpecificFilterConfig) {
        if (!ZKStringUtils.hasText(urlPath)) {
            throw new IllegalArgumentException("chainName cannot be null or empty.");
        }
        Filter filter = getFilter(filterName);
        if (filter == null) {
            throw new IllegalArgumentException("There is no filter with name '" + filterName + "' to apply to chain ["
                    + urlPath + "] in the pool of available Filters.  Ensure a "
                    + "filter with that name/path has first been registered with the addFilter method(s).");
        }
        List<Filter> chains = ensureChain(urlPath);
        chains.add(filter);
    }

    public List<Filter> getChains(String urlPath) {
        return this.filterChainsMap.get(urlPath);
    }

    public Filter getFilter(String name) {
        return this.filtersMap.get(name);
    }

    protected List<Filter> ensureChain(String urlPath) {
        List<Filter> chains = this.filterChainsMap.get(urlPath);
        if (chains == null) {
            chains = new ArrayList<>();
            this.filterChainsMap.put(urlPath, chains);
        }
        return chains;
    }

    private FilterConfig filterConfig;
//
//    private Map<String, Filter> filters; //pool of filters available for creating chains
//
//    private Map<String, NameFilterList> filterChains; //key: chain name, value: chain
//
//    public DefaultFilterChainManager() {
//        this.filters = new LinkedHashMap<String, Filter>();
//        this.filterChains = new LinkedHashMap<String, NameFilterList>();
//        addDefaultFilters(false);
//    }
//
//    public DefaultFilterChainManager(FilterConfig filterConfig) {
//        this.filters = new LinkedHashMap<String, Filter>();
//        this.filterChains = new LinkedHashMap<String, NameFilterList>();
//        setFilterConfig(filterConfig);
//        addDefaultFilters(true);
//    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public Map<String, Filter> getFilters() {
        return this.filtersMap;
    }

//    public void setFilters(Map<String, Filter> filters) {
//        this.filters = filters;
//    }
//
//    public Map<String, NameFilterList> getFilterChains() {
//        return filterChains;
//    }
//
//    public void setFilterChains(Map<String, NameFilterList> filterChains) {
//        this.filterChains = filterChains;
//    }
//
//    public Filter getFilter(String name) {
//        return this.filters.get(name);
//    }
//
    @Override
    public void addFilter(String name, Filter filter) {
        addFilter(name, filter, false);
    }

    @Override
    public void addFilter(String name, Filter filter, boolean init) {
        addFilter(name, filter, init, true);
    }

    @Override
    public void addFilter(String name, Filter filter, boolean init, boolean overwrite) {
        Filter existing = getFilter(name);
        if (existing == null || overwrite) {
//            if (filter instanceof Nameable) {
//                ((Nameable) filter).setName(name);
//            }
            if (init) {
                initFilter(filter);
            }
            this.filtersMap.put(name, filter);
        }
    }

//    public void addToChain(String chainName, String filterName) {
//        addToChain(chainName, filterName, null);
//    }
//
//    public void addToChain(String chainName, String filterName, String chainSpecificFilterConfig) {
//        if (!StringUtils.hasText(chainName)) {
//            throw new IllegalArgumentException("chainName cannot be null or empty.");
//        }
//        Filter filter = getFilter(filterName);
//        if (filter == null) {
//            throw new IllegalArgumentException("There is no filter with name '" + filterName +
//                    "' to apply to chain [" + chainName + "] in the pool of available Filters.  Ensure a " +
//                    "filter with that name/path has first been registered with the addFilter method(s).");
//        }
//
////        applyChainConfig(chainName, filter, chainSpecificFilterConfig);
//
//        NameFilterList chain = ensureChain(chainName);
//        chain.add(filter);
//    }
//
////    protected void applyChainConfig(String chainName, Filter filter, String chainSpecificFilterConfig) {
////        if (logger.isDebugEnabled()) {
////            logger.debug("Attempting to apply path [" + chainName + "] to filter [" + filter + "] " +
////                    "with config [" + chainSpecificFilterConfig + "]");
////        }
////        if (filter instanceof PathConfigProcessor) {
////            ((PathConfigProcessor) filter).processPathConfig(chainName, chainSpecificFilterConfig);
////        } else {
////            if (StringUtils.hasText(chainSpecificFilterConfig)) {
////                //they specified a filter configuration, but the Filter doesn't implement PathConfigProcessor
////                //this is an erroneous config:
////                String msg = "chainSpecificFilterConfig was specified, but the underlying " +
////                        "Filter instance is not an 'instanceof' " +
////                        PathConfigProcessor.class.getName() + ".  This is required if the filter is to accept " +
////                        "chain-specific configuration.";
////                throw new SecurityException(msg);
////            }
////        }
////    }
//
//    public NameFilterList getChain(String chainName) {
//        return this.filterChains.get(chainName);
//    }
//
//    public boolean hasChains() {
//        return !CollectionUtils.isEmpty(this.filterChains);
//    }
//
//    @SuppressWarnings("unchecked")
//  public Set<String> getChainNames() {
//        return this.filterChains != null ? this.filterChains.keySet() : Collections.EMPTY_SET;
//    }
//
//
    /**
     * Initializes the filter by calling
     * <code>filter.init( {@link #getFilterConfig() getFilterConfig()} );</code>.
     *
     * @param filter
     *            the filter to initialize with the {@code FilterConfig}.
     */
    protected void initFilter(Filter filter) {
        FilterConfig filterConfig = getFilterConfig();
        if (filterConfig == null) {
            throw new IllegalStateException("FilterConfig attribute has not been set.  This must occur before filter "
                    + "initialization can occur.");
        }
        try {
            filter.init(filterConfig);
        }
        catch(ServletException e) {
            throw new SecurityException(e);
        }
    }

    @Override
    public void addDefaultFilters() {
        this.addDefaultFilters(false);
    }

    protected void addDefaultFilters(boolean init) {
        for (ZKSecDefaultFilter defaultFilter : ZKSecDefaultFilter.values()) {
            addFilter(defaultFilter.name(), defaultFilter.newInstance(), init, false);
        }
    }

}
