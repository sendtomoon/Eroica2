package com.sendtomoon.eroica.common.security;

import java.util.Properties;

import com.alibaba.dubbo.common.extension.SPI;

@SPI("Cyber")
public interface PasswordProvider {

	void refreshConfig(Properties properties) throws PasswordProviderException;

	String getPassword(PasswordContext context) throws PasswordProviderException;
}
