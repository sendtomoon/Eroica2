package com.sendtomoon.eroica2.allergo.spring.schema;

import org.apache.dubbo.common.extension.SPI;
import org.springframework.beans.factory.config.BeanDefinition;

import com.sendtomoon.eroica2.allergo.classloader.AllergoURL;
import com.sendtomoon.eroica2.common.utils.URLUtils;

@SPI
public interface PizzaShareBean {

	BeanDefinition create(AllergoURL allergoURL, URLUtils configURL);
}
