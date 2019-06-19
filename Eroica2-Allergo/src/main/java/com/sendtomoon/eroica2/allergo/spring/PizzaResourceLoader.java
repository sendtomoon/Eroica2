package com.sendtomoon.eroica2.allergo.spring;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * <p> Title: PizzaResourceLoader </p>
 * <p> Description:  Pizza资源获取，如果是allergo协议，则返回allergo资源，否则执行DefaultResourceLoader</p>
 * <p> Copyright: PA TRUST Copyright (c) 2015 </p>
 * <p> Company: PA TRUST </p>
 *
 * @author 谢轶丹
 *
 * @version 
 * @since 2015年1月16日
 */
public class PizzaResourceLoader extends DefaultResourceLoader {

    public static String ALLERGO_URL_PREFIX = "allergo:";

    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "Location must not be null");
        if (location.startsWith(ALLERGO_URL_PREFIX)) {
        	String path=location.substring(ALLERGO_URL_PREFIX.length());
            return new AllergoResource(path);
        }
        else {
            return super.getResource(location);
        }
    }

}
