package com.sendtomoon.eroica.common.utils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.sendtomoon.eroica.common.exception.EroicaException;

public class ReflectUtils {

	public static Class<?> getGenericClass(PropertyDescriptor p, int idx) {
		Class<?> clazz = null;
		if (p.getWriteMethod() != null) {
			clazz = p.getWriteMethod().getDeclaringClass();
		} else if (p.getReadMethod() != null) {
			clazz = p.getReadMethod().getDeclaringClass();
		}
		if (clazz == null)
			return null;
		try {
			Field f = clazz.getDeclaredField(p.getName());
			if (f == null)
				return null;
			Type type = f.getGenericType();
			if (type != null) {
				return getGenericClass(type, idx);
			}
		} catch (Exception e) {
		}
		return null;
	}

	private static Class<?> getGenericClass(Type t, int i) {
		if (t instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) t;
			Type[] types = pt.getActualTypeArguments();
			if (types != null && types.length > i) {
				Type genericClass = pt.getActualTypeArguments()[i];
				if (genericClass != null && genericClass instanceof Class) {
					Class<?> temp = (Class<?>) genericClass;
					if (!(temp.isInterface() || temp.equals(Object.class))) {
						return temp;
					}
				}
			}
		}
		return null;
	}

	public static Class<?> getGenericClass(PropertyDescriptor prop) {
		return getGenericClass(prop, 0);
	}

	public static Class<?> getMapBindType(PropertyDescriptor p) {
		return getGenericClass(p, 1);
	}

	public static Class<?> getCollectionBindType(PropertyDescriptor p) {
		return getGenericClass(p);
	}

	public static <T extends Annotation> T getAnnotation(PropertyDescriptor p, Class<T> annotionClazz) {
		try {
			T bt = null;
			Class<?> clazz = null;
			if (bt == null && p.getWriteMethod() != null) {
				bt = p.getWriteMethod().getAnnotation(annotionClazz);
				if (clazz == null) {
					clazz = p.getWriteMethod().getDeclaringClass();
				}
			}
			if (bt == null && p.getReadMethod() != null) {
				bt = p.getReadMethod().getAnnotation(annotionClazz);
				if (clazz == null) {
					clazz = p.getReadMethod().getDeclaringClass();
				}
			}
			if (bt == null && clazz != null) {
				try {
					bt = clazz.getDeclaredField(p.getName()).getAnnotation(annotionClazz);
				} catch (Exception e) {
				}
			}
			return bt;
		} catch (Throwable ex) {
			throw new EroicaException(ex.getMessage(), ex);
		}
	}

	private static Map<Class<?>, Object> primitiveDefaultValue = new HashMap<Class<?>, Object>();

	static {
		primitiveDefaultValue.put(Integer.TYPE, new Integer(0));
		primitiveDefaultValue.put(Boolean.TYPE, Boolean.FALSE);
		primitiveDefaultValue.put(Double.TYPE, new Double(0d));
		primitiveDefaultValue.put(Long.TYPE, new Long(0l));
		primitiveDefaultValue.put(Void.TYPE, new Object());
		primitiveDefaultValue.put(Float.TYPE, new Float(0f));
		primitiveDefaultValue.put(Short.TYPE, new Short((short) 0));
		primitiveDefaultValue.put(Byte.TYPE, new Short((byte) 0));
		primitiveDefaultValue.put(Character.TYPE, new Character((char) 0));
	}

	public static Object getPrimitiveDefaultValue(Class<?> type) {
		return primitiveDefaultValue.get(type);
	}

}
