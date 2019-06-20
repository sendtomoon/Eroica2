package com.sendtomoon.eroica2.allergo;

public abstract class AllergoConstants {

	/** 应用名 */
	public static final String KEY_APP_NAME = "allergo.app.name";

	/** 应用名 */
	public static final String KEY_APP_NAME_1 = "eoapp.name";

	/** 资源管理实现 */
	public static final String KEY_MANAGER = "allergo.manager";

	/** 本地备份开关,true/false */
	public static final String KEY_LOCAL_BACKUP = "allergo.local.backup";

	/** 默认allergo配置文件 */
	public static final String DEF_CONFIG_FILE = "classpath:eroica_base.properties";

	/** 默认allergo配置文件 property name */
	public static final String KEY_CONFIG_FILE = "allergo.config.file";

	/** 领域ID */
	public static final String KEY_DOMAIN_ID = "allergo.domain.id";

	/**
	 * 环境属性：allergo.manager
	 */
	public static final String ENV_MANAGER = "ALLERGO_MANAGER";

	/**
	 * 环境属性：allergo.domain.id
	 */
	public static final String ENV_DOMAIN_ID = "ALLERGO_DOMAIN_ID";

	/**
	 * 环境属性：allergo.app.name
	 */
	public static final String ENV_APP_NAME = "ALLERGO_APP_NAME";

	/**
	 * 环境属性：eroica.log.home
	 */
	public static final String ENV_LOG_HOME = "EROICA_LOG_HOME";

	/***
	 * 默认Allergo资源根路径
	 */
	public static final String DEF_ROOT_PATH = "/eroicaConfs";

	/***
	 * Allergo资源组：组件仓库
	 */
	public static final String GROUP_LIB = "lib";

	/***
	 * Allergo资源组：应用配置文件
	 */
	public static final String GROUP_EOAPP = "eoapp";

	/***
	 * Allergo资源组：组件配置文件
	 */
	public static final String GROUP_SAR = "sar";

	/***
	 * Allergo资源组：zip文件仓库
	 */
	public static final String GROUP_ZIP = "zip";

	/***
	 * Allergo资源组：classpath资源文件组
	 */
	public static final String GROUP_RESOURCES = "resources";

	/***
	 * Allergo资源组：默认
	 */
	public static final String GROUP_DEFAULT = "def";

	/***
	 * Allergo资源组：esa
	 */
	public static final String GROUP_ESA = "esa";

	/***
	 * Allergo资源组：Redis连接池配置组
	 */
	public static final String GROUP_REDIS = "redis";

	/***
	 * Allergo资源组：DB连接池配置组
	 */
	public static final String GROUP_DATASOURCE = "datasource";

	/***
	 * Allergo资源组：mongoDB连接配置组
	 */
	public static final String GROUP_MONGODB = "mongodb";

	/***
	 * Allergo资源组：Kafka连接配置组
	 */
	public static final String GROUP_KAFKA = "kafka";

	/** Allergo资源组：全局变量 */
	public static final String GROUP_GLOBALVARS = "globalvars";
}
