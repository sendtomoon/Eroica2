package com.sendtomoon.eroica2.allergo.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class ShareBeanNamespaceHandler extends NamespaceHandlerSupport{

	@Override
	public void init() {
		this.registerBeanDefinitionParser("bean", new ShareBeanDefinitionParser());
	}

}
