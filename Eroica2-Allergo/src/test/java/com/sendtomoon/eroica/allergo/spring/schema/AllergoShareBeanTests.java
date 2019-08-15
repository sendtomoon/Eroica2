package com.sendtomoon.eroica.allergo.spring.schema;

import com.alibaba.dubbo.common.extension.ExtensionLoader;

import com.sendtomoon.eroica2.allergo.spring.schema.AllergoShareBean;

public class AllergoShareBeanTests {

	public static void main(String args[]) {
		ExtensionLoader<AllergoShareBean> loader = ExtensionLoader.getExtensionLoader(AllergoShareBean.class);
		AllergoShareBean factory = loader.getDefaultExtension();
		System.err.println("factory=" + factory);
		System.err.println("factory=" + loader.getLoadedExtensions());
	}
}
