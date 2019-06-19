package com.sendtomoon.eroica2.allergo;

import org.apache.dubbo.common.extension.SPI;

import com.sendtomoon.eroica2.common.utils.URLUtils;

@SPI("local")
public interface AllergoManagerFactory {

	AllergoManager create(URLUtils url);

}
