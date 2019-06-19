package com.sendtomoon.eroica2.allergo.log4j;

import org.springframework.beans.factory.InitializingBean;

import com.sendtomoon.eroica2.allergo.AllergoContext;
import com.sendtomoon.eroica2.allergo.spring.AllergoResourceListener;

public interface Log4jInitialization extends AllergoResourceListener,InitializingBean {
 
	void setPizzaContext(AllergoContext allergoContext);
}
