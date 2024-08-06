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
* @Title: ZKSecHttpServletResponseWrapper.java 
* @author Vinson 
* @Package com.zk.security.web.support.servlet.wrapper
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 11:14:58 AM 
* @version V1.0 
*/
package com.zk.security.web.support.servlet.wrapper;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.web.support.servlet.wrapper.ZKHttpServletRequestWrapper;
import com.zk.core.web.support.servlet.wrapper.ZKHttpServletResponseWrapper;
import com.zk.security.common.ZKSecConstants;
import com.zk.security.ticket.ZKSecTicket;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @ClassName: ZKSecHttpServletResponseWrapper
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSecHttpServletResponseWrapper extends ZKHttpServletResponseWrapper implements ZKSecResponseWrapper {

    public ZKSecHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    private ServletContext context = null;

    // the associated request
    private ZKHttpServletRequestWrapper request = null;

    private boolean traceTicketId = false;

    public ZKSecHttpServletResponseWrapper(HttpServletResponse wrapped, ServletContext context,
            ZKHttpServletRequestWrapper request, boolean traceTicketId) {
        super(wrapped);
        this.context = context;
        this.request = request;
        this.traceTicketId = traceTicketId;
    }

    public ServletContext getContext() {
        return context;
    }

    public void setContext(ServletContext context) {
        this.context = context;
    }

    public ZKHttpServletRequestWrapper getRequest() {
        return request;
    }

    public void setRequest(ZKHttpServletRequestWrapper request) {
        this.request = request;
    }

    public String encodeRedirectURL(String url) {
        if (isEncodeable(toAbsolute(url))) {
            return toEncoded(url, request.getSession().getId());
        }
        else {
            return url;
        }
    }

    public String encodeRedirectUrl(String s) {
        return encodeRedirectURL(s);
    }

    /**
     * Encode the session identifier associated with this response into the
     * specified URL, if necessary.
     *
     * @param url
     *            URL to be encoded
     */
    public String encodeURL(String url) {
        String absolute = toAbsolute(url);
        if (isEncodeable(absolute)) {
            // W3c spec clearly said
            if (url.equalsIgnoreCase("")) {
                url = absolute;
            }
            return toEncoded(url, request.getSession().getId());
        }
        else {
            return url;
        }
    }

    public String encodeUrl(String s) {
        return encodeURL(s);
    }

    /**
     * Return <code>true</code> if the specified URL should be encoded with a
     * session identifier. This will be true if all of the following conditions
     * are met:
     * <ul>
     * <li>The request we are responding to asked for a valid session
     * <li>The requested session ID was not received via a cookie
     * <li>The specified URL points back to somewhere within the web application
     * that is responding to this request
     * </ul>
     *
     * @param location
     *            Absolute URL to be validated
     * @return {@code true} if the specified URL should be encoded with a
     *         session identifier, {@code false} otherwise.
     */
    protected boolean isEncodeable(final String location) {

        if (location == null)
            return (false);

        // Is this an intra-document reference?
        if (location.startsWith("#"))
            return (false);

        // Are we in a valid session that is not using cookies?
        final HttpServletRequest hreq = request;
        return doIsEncodeable(hreq, ZKSecSecurityUtils.getTikcet(), location);
    }

    private boolean doIsEncodeable(HttpServletRequest hreq, ZKSecTicket tk, String location) {
        // Is this a valid absolute URL?
        URL url;
        try {
//            url = new URL(location);
            url = URI.create(location).toURL();
        }
        catch(MalformedURLException e) {
            return (false);
        }

        // Does this URL match down to (and including) the context path?
        if (!hreq.getScheme().equalsIgnoreCase(url.getProtocol()))
            return (false);
        if (!hreq.getServerName().equalsIgnoreCase(url.getHost()))
            return (false);
        int serverPort = hreq.getServerPort();
        if (serverPort == -1) {
            if ("https".equals(hreq.getScheme()))
                serverPort = 443;
            else
                serverPort = 80;
        }
        int urlPort = url.getPort();
        if (urlPort == -1) {
            if ("https".equals(url.getProtocol()))
                urlPort = 443;
            else
                urlPort = 80;
        }
        if (serverPort != urlPort)
            return (false);

        String contextPath = getRequest().getContextPath();
        if (contextPath != null) {
            String file = url.getFile();
            if ((file == null) || !file.startsWith(contextPath))
                return (false);
//            String tok = ";" + WebSecManager.DEFAULT_TK_ID_NAME + "=" + session.getId();
//            if (file.indexOf(tok, contextPath.length()) >= 0)
//                return (false);
        }

        // This URL belongs to our web application, so it is encodeable
        return (true);

    }

    private String toAbsolute(String location) {

        if (location == null)
            return (location);

        boolean leadingSlash = location.startsWith("/");

        if (leadingSlash || !hasScheme(location)) {

            StringBuilder buf = new StringBuilder();

            String scheme = request.getScheme();
            String name = request.getServerName();
            int port = request.getServerPort();

            try {
                buf.append(scheme).append("://").append(name);
                if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
                    buf.append(':').append(port);
                }
                if (!leadingSlash) {
                    String relativePath = request.getRequestURI();
                    int pos = relativePath.lastIndexOf('/');
                    relativePath = relativePath.substring(0, pos);

                    String encodedURI = ZKEncodingUtils.urlEncode(relativePath, getCharacterEncoding());
                    buf.append(encodedURI).append('/');
                }
                buf.append(location);
            }
            catch(Exception e) {
                IllegalArgumentException iae = new IllegalArgumentException(location);
                iae.initCause(e);
                throw iae;
            }

            return buf.toString();

        }
        else {
            return location;
        }
    }

    /**
     * Determine if the character is allowed in the scheme of a URI. See RFC
     * 2396, Section 3.1
     *
     * @param c
     *            the character to check
     * @return {@code true} if the character is allowed in a URI scheme,
     *         {@code false} otherwise.
     */
    public static boolean isSchemeChar(char c) {
        return Character.isLetterOrDigit(c) || c == '+' || c == '-' || c == '.';
    }

    /**
     * Returns {@code true} if the URI string has a {@code scheme} component,
     * {@code false} otherwise.
     *
     * @param uri
     *            the URI string to check for a scheme component
     * @return {@code true} if the URI string has a {@code scheme} component,
     *         {@code false} otherwise.
     */
    private boolean hasScheme(String uri) {
        int len = uri.length();
        for (int i = 0; i < len; i++) {
            char c = uri.charAt(i);
            if (c == ':') {
                return i > 0;
            }
            else if (!isSchemeChar(c)) {
                return false;
            }
        }
        return false;
    }

    protected String toEncoded(String url, String sessionId) {

        if ((url == null) || (sessionId == null))
            return (url);

        String path = url;
        String query = "";
        String anchor = "";
        int question = url.indexOf('?');
        if (question >= 0) {
            path = url.substring(0, question);
            query = url.substring(question);
        }
        int pound = path.indexOf('#');
        if (pound >= 0) {
            anchor = path.substring(pound);
            path = path.substring(0, pound);
        }
        StringBuilder sb = new StringBuilder(path);
        if (traceTicketId && (sb.length() > 0)) { // session id param can't be
                                                  // first.
            sb.append(";");
            sb.append(ZKSecConstants.PARAM_NAME.TicketId);
            sb.append("=");
            sb.append(sessionId);
        }
        sb.append(anchor);
        sb.append(query);
        return (sb.toString());

    }

}
