package com.sendtomoon.eroica2.allergo.spring.schema;

import com.alibaba.dubbo.common.extension.SPI;
import org.springframework.beans.factory.config.BeanDefinition;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica.common.utils.URLUtils;

@SPI
public interface AllergoShareBean {

	BeanDefinition create(AllergoURL allergoURL, URLUtils configURL);
}
