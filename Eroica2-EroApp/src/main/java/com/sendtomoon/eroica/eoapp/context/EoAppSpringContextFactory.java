package com.sendtomoon.eroica.eoapp.context;

import javax.servlet.ServletContext;

import com.sendtomoon.eroica.adagio.Pola;


public interface EoAppSpringContextFactory {

	EoAppSpringContext create(ClassLoader classLoader,
			final ServletContext originalServletContext,final Pola pola);
}
