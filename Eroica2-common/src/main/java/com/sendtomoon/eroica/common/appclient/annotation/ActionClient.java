package com.sendtomoon.eroica.common.appclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionClient {
	/** 服务名 */
	String name() default "";

	/** 服务分组 */
	String group() default "";

	/** ac，默认为dubbo */
	String eroicaAc() default "";

}
