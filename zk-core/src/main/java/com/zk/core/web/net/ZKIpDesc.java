/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKIpDesc.java 
* @author Vinson 
* @Package com.zk.core.web.net 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2025 6:05:50 PM 
* @version V1.0 
*/
package com.zk.core.web.net;
/** 
* @ClassName: ZKIpDesc 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKIpDesc {

    String ip;

    String country; // 国家/地区

    String countryCode; // 国家/地区代码

    String city; // 城市

    String region; // 省

    String regionCode; // 省代码

    String lat; // 纬度

    String lon; // 经度

    String timezone; // 时区

    String isp; // 运营商

    public static ZKIpDesc as(String ip, String country, String countryCode, String city, String region,
            String regionCode, String lat, String lon, String timezone, String isp) {
        ZKIpDesc ipDesc = new ZKIpDesc(ip, country, countryCode, city, region, regionCode, lat, lon, timezone, isp);

        return ipDesc;
    }

    public ZKIpDesc(String ip, String country, String countryCode, String city, String region, String regionCode,
            String lat, String lon, String timezone, String isp) {
        this.ip = ip;
        this.country = country;
        this.countryCode = countryCode;
        this.city = city;
        this.region = region;
        this.regionCode = regionCode;
        this.lat = lat;
        this.lon = lon;
        this.timezone = timezone;
        this.isp = isp;
    }

    /**
     * @return ip sa
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip
     *            the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return country sa
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     *            the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return countryCode sa
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode
     *            the countryCode to set
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * @return city sa
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return region sa
     */
    public String getRegion() {
        return region;
    }

    /**
     * @param region
     *            the region to set
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * @return regionCode sa
     */
    public String getRegionCode() {
        return regionCode;
    }

    /**
     * @param regionCode
     *            the regionCode to set
     */
    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    /**
     * @return lat sa
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat
     *            the lat to set
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * @return lon sa
     */
    public String getLon() {
        return lon;
    }

    /**
     * @param lon
     *            the lon to set
     */
    public void setLon(String lon) {
        this.lon = lon;
    }

    /**
     * @return timezone sa
     */
    public String getTimezone() {
        return timezone;
    }

    /**
     * @param timezone
     *            the timezone to set
     */
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    /**
     * @return isp sa
     */
    public String getIsp() {
        return isp;
    }

    /**
     * @param isp
     *            the isp to set
     */
    public void setIsp(String isp) {
        this.isp = isp;
    }

}
