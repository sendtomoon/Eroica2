package com.sendtomoon.eroica.eoapp.sar.lib;

import javax.servlet.ServletContext;

import com.sendtomoon.eroica2.allergo.classloader.AllergoClassLoader;

public interface SARClassLoaderFactory {

	AllergoClassLoader createClassLoader(ClassLoader parentClassLoader, ServletContext servletContext);

}
