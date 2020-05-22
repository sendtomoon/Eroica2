package com.sendtomoon.eroica.common.beans;

import static java.util.Locale.ENGLISH;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import com.sendtomoon.eroica.common.beans.format.FieldFormatterFactory;


public class BeansConfigInfo {

	private Map<Class<?>, List<ParameterInfo>> cache;

	public BeansConfigInfo() {
		cache = new ConcurrentHashMap<Class<?>, List<ParameterInfo>>();
	}

	public List<ParameterInfo> getRestFields(Class<?> clazz) {
		return _getRestFields(clazz);

	}

	private List<ParameterInfo> _getRestFields(Class<?> clazz) {
		List<ParameterInfo> fields = cache.get(clazz);
		if (fields == null) {
			synchronized (cache) {
				fields = getParameterInfos(clazz);
				if (fields.size() > 0) {
					forFieldAnnotations(clazz, fields);
					// forMethodAnnotations(clazz,fields);
					cache.put(clazz, fields);
				}
			}
		}
		if (fields.size() == 0) {
			return null;
		} else {
			return fields;
		}
	}

	protected List<ParameterInfo> getParameterInfos(Class<?> clazz) {
		List<ParameterInfo> fields = new ArrayList<ParameterInfo>(8);
		Field clazzFields[] = clazz.getDeclaredFields();
		Class<?> superClass = clazz.getSuperclass();
		if (superClass != null && !superClass.equals(Object.class)) {
			fields.addAll(getParameterInfos(superClass));
		}
		for (Field clazzField : clazzFields) {
			if (Modifier.isStatic(clazzField.getModifiers())) {
				continue;
			}
			if (Modifier.isTransient(clazzField.getModifiers())) {
				continue;
			}
			Method writem = getWriteMethod(clazzField, clazz);
			Method readm = getReadMethod(clazzField, clazz);
			if (readm != null || writem != null) {
				fields.add(new BeanPropertyInfo(clazzField, writem, readm, clazz));
			}
		}
		return fields;
	}

	public Method getReadMethod(Field clazzField, Class<?> clazz) {
		String readMethodName = null;
		Class<?> fieldType = clazzField.getType();
		String name = clazzField.getName();
		if (fieldType == boolean.class || fieldType == null) {
			readMethodName = "is" + capitalizeBoolean(name);
		} else {
			readMethodName = "get" + capitalize(name);
		}
		try {
			Method[] methods = clazz.getDeclaredMethods();
			Method foundMethod = null;
			for (Method method : methods) {
				if((method.getName().startsWith("is") || method.getName().startsWith("get"))) {
					if (method.getName().equals(readMethodName) && method.getParameterTypes().length == 0 && method.getReturnType()==fieldType) {
						int mods = method.getModifiers();
						if (Modifier.isPublic(mods)) {
							foundMethod = method;
							break;
						}
					}
					//特殊处理boolean尅型，如：getIsxxx
					if(fieldType == boolean.class && foundMethod==null) {
						String readMethodName2 = "get" + capitalize(name);
						if (method.getName().equals(readMethodName2)){
							int mods = method.getModifiers();
							if (Modifier.isPublic(mods)) {
								foundMethod = method;
								break;
							}
						}
					}
				}
			}
			return foundMethod;
		} catch (Exception e) {
		}
		return null;
	}

	public Method getWriteMethod(Field clazzField, Class<?> clazz) {
		String writeMethodName = null;
		String name = clazzField.getName();

		Class<?> fieldType = clazzField.getType();
		if (fieldType == boolean.class || fieldType == null) {
			writeMethodName = "set" + capitalizeBoolean(name);
		} else {
			writeMethodName = "set" + capitalize(name);
		}

		Method[] methods = clazz.getDeclaredMethods();
		Method foundMethod = null;
		for (Method method : methods) {
			if(method.getName().startsWith("set")) {
				if (method.getName().equals(writeMethodName) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0]==fieldType) {
					int mods = method.getModifiers();
					if (Modifier.isPublic(mods)) {
						foundMethod = method;
						break;
					}
				}
				//特殊处理boolean类型的，如：setIsxxx
				if(fieldType == boolean.class && foundMethod==null) {
					String writeMethodName2 = "set" + capitalize(name);
					if(method.getName().equals(writeMethodName2)) {
						int mods = method.getModifiers();
						if (Modifier.isPublic(mods)) {
							foundMethod = method;
							break;
						}
					}
				}
			}
		}
		return foundMethod;
	}
	
	public String capitalizeBoolean(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		
		if(!name.startsWith("is")) {
			return capitalize(name);
		}else {
			//布尔类型，is开头，第三位大写，则要去掉is
			if(Character.isUpperCase(name.charAt(2))) {
				return name.substring(2, name.length());
			}
			return capitalize(name);
		}
	}

	public String capitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		//第二位如果是大写，则返回原值
		if(name.length()>1&&Character.isUpperCase(name.charAt(1))) {
			return name;
		}
		//其他第一位则是大写
		return name.substring(0, 1).toUpperCase(ENGLISH) + name.substring(1);
	}

	private void forFieldAnnotations(Class<?> targetClass, List<ParameterInfo> bindFields) {
		Field[] fields = targetClass.getDeclaredFields();
		if (fields == null)
			return;
		for (Field f : fields) {
			NumberFormat nf = f.getAnnotation(NumberFormat.class);
			DateTimeFormat df = f.getAnnotation(DateTimeFormat.class);
			if (nf != null || df != null) {
				ParameterInfo bindField = null;
				if (bindFields.size() > 0) {
					for (ParameterInfo temp : bindFields) {
						if (temp.getName().equalsIgnoreCase(f.getName())) {
							bindField = temp;
						}
					}
				}
				if (bindField == null) {
					Object temp = (nf != null ? nf : df);
					throw new InvalidPropertyException(targetClass, f.getName(), " no read/write method for annotation[" + temp + "].");
				} else {
					if (nf != null) {
						bindField.setFormatter(FieldFormatterFactory.getNumberFormatter(bindField, nf));
					}
					if (df != null) {
						bindField.setFormatter(FieldFormatterFactory.getDateFormatter(bindField, df));
					}
				}
			}
		}
	}

	public synchronized void clear() {
		this.cache.clear();
	}

}
