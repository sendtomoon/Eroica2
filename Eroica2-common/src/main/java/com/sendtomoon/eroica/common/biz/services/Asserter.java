package com.sendtomoon.eroica.common.biz.services;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.Assert;

public class Asserter {

	public static Asserter instance = new Asserter();

	public void isTrue(boolean expression, String message) {
		Assert.isTrue(expression, message);
	}

	public void isTrue(boolean expression) {
		Assert.isTrue(expression);
	}

	public void isNull(Object object, String message) {
		Assert.isNull(object, message);
	}

	public void isNull(Object object) {
		Assert.isNull(object);
	}

	public void notNull(Object object, String message) {
		Assert.notNull(object, message);
	}

	public void notNull(Object object) {
		Assert.notNull(object);
	}

	public void hasLength(String text, String message) {
		Assert.hasLength(text, message);
	}

	public void hasLength(String text) {
		Assert.hasLength(text);
	}

	public void hasText(String text, String message) {
		Assert.hasText(text, message);
	}

	public void hasText(String text) {
		Assert.hasText(text);
	}

	public void doesNotContain(String textToSearch, String substring, String message) {
		Assert.doesNotContain(textToSearch, substring, message);
	}

	public void doesNotContain(String textToSearch, String substring) {
		Assert.doesNotContain(textToSearch, substring);
	}

	public void notEmpty(Object[] array, String message) {
		Assert.notEmpty(array, message);
	}

	public void notEmpty(Object[] array) {
		Assert.notEmpty(array);
	}

	public void noNullElements(Object[] array, String message) {
		Assert.noNullElements(array, message);
	}

	public void noNullElements(Object[] array) {
		Assert.noNullElements(array);
	}

	public void notEmpty(Collection collection, String message) {
		Assert.notEmpty(collection, message);
	}

	public void notEmpty(Collection collection) {
		Assert.notEmpty(collection);
	}

	public void notEmpty(Map map, String message) {
		Assert.notEmpty(map, message);
	}

	public void notEmpty(Map map) {
		Assert.notEmpty(map);
	}

	public void isInstanceOf(Class clazz, Object obj) {
		Assert.isInstanceOf(clazz, obj);
	}

	public void isInstanceOf(Class type, Object obj, String message) {
		Assert.isInstanceOf(type, obj, message);
	}

	public void isAssignable(Class superType, Class subType) {
		Assert.isAssignable(superType, subType);
	}

	public void isAssignable(Class superType, Class subType, String message) {
		Assert.isAssignable(superType, subType, message);
	}

	public void state(boolean expression, String message) {
		Assert.state(expression, message);
	}

	public void state(boolean expression) {
		Assert.state(expression);
	}
}
