package com.sendtomoon.eroica2.allergo;

import com.alibaba.dubbo.common.extension.SPI;

import com.sendtomoon.eroica.common.utils.URLUtils;

@SPI("local")
public interface AllergoManagerFactory {

	AllergoManager create(URLUtils url);

}
