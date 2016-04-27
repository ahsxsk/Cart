package com.shike.common;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * HTTP工具类
 * Created by shike on 16/3/26.
 */
public final class HttpUtils {

    private static final Logger logger = Logger.getLogger(HttpUtils.class);

    private static String PARAMTER_NAME_REGEX = "";
    private static String PARAMTER_VALUE_REGEX = "";
    private static String PARAMTER_VALUE_REGEX_JSON = "";
    private static Properties properties = null;
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    static {
        try {
            if (properties != null) {
                PARAMTER_NAME_REGEX = properties.getProperty("paramterNameRegex");
                PARAMTER_VALUE_REGEX = properties.getProperty("paramterValueRegex");
                PARAMTER_VALUE_REGEX_JSON = properties.getProperty("paramterValueJsonRegex");
            } else {
                logger.info("*** regex expresion use the hard coding ***");
                PARAMTER_NAME_REGEX = "[^a-zA-Z0-9_\\-]";
                PARAMTER_VALUE_REGEX = "[~`%^'\\\\]";
                PARAMTER_VALUE_REGEX_JSON = "[~`%^\\\\]";
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            PARAMTER_NAME_REGEX = "[^a-zA-Z0-9_\\-]";
            PARAMTER_VALUE_REGEX = "[~`%^'\\\\]";
            PARAMTER_VALUE_REGEX_JSON = "[~`%^\\\\]";
        }
    }
    /**
     * 获取remoteIp
     * @param request 请求
     * @return ip
     */
    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-real-ip");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        //过滤反向代理IP
        String[] stemps = ip.split(",");
        if (stemps != null && stemps.length >= 1) {
            //真实IP
            ip = stemps[0];
        }

        ip = ip.trim();
        if (ip.length() > 23) {
            ip = ip.substring(0, 23);
        }

        return ip;
    }

    /**
     * 将request参数转换成Map
     * @param request
     * @return
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Map properties = request.getParameterMap();
        paramMap = getParameterMapRemoveEspi(properties);
        return paramMap;
    }

    /**
     * 去除ESAPI
     *
     * @param properties
     * @return
     */
    public static Map<String, String> getParameterMapRemoveEspi(Map properties) {
        Map<String, String> paramMap = new HashMap<String, String>();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        boolean flagName = false;
        boolean flagValue = false;
        while (entries.hasNext()) {
            entry = (Map.Entry) entries.next();
            name = ((String) entry.getKey()).replaceAll(PARAMTER_NAME_REGEX, "");
            if ("passwd".equals(name)) {
                Object valueObj = entry.getValue();
                if (null == valueObj) {
                    value = "";
                } else if (valueObj instanceof String[]) {
                    String[] values = (String[]) valueObj;
                    for (int i = 0; i < values.length; i++) {
                        value = values[i] + ",";
                    }
                    value = value.substring(0, value.length() - 1);
                } else {
                    value = valueObj.toString();
                }
                paramMap.put(name, value);
            } else {
                if (!name.equals((String) entry.getKey())) {
                    flagName = true;
                }
                String temp = "";
                Object valueObj = entry.getValue();
                if (null == valueObj) {
                    value = "";
                } else if (valueObj instanceof String[]) {
                    String[] values = (String[]) valueObj;
                    for (int i = 0; i < values.length; i++) {
                        temp = values[i] + ",";
                    }
                    temp = temp.substring(0, temp.length() - 1);
                    value = temp.replaceAll(PARAMTER_VALUE_REGEX, "");
                    if (!value.equals(temp)) {
                        flagValue = true;
                    }
                } else {
                    temp = valueObj.toString();
                    value = temp.replaceAll(PARAMTER_VALUE_REGEX, "");
                    if (!value.equals(valueObj.toString())) {
                        flagValue = true;
                    }
                }
                if (flagName || flagValue) {
                    flagName = false;
                    flagValue = false;
                    logger.info("The request parameter : [" + (String) entry.getKey() + "=" + temp + "] replaced by : [" + name + "=" + value + "]");
                }
                paramMap.put(name, value);
            }
        }
        return paramMap;
    }
}