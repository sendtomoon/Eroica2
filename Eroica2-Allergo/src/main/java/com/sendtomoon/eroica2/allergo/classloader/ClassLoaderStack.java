package com.sendtomoon.eroica2.allergo.classloader;

import java.util.LinkedList;

/***
 * 当前线程ClassLoader切换satck
 * @author LIXINGNAN945
 *
 */
public class ClassLoaderStack {
	

	private volatile static ThreadLocal<LinkedList<ClassLoader>> threadLocalStack=new ThreadLocal<LinkedList<ClassLoader>>();
	
	public static void push(final ClassLoader classLoader){
		Thread currentThread=Thread.currentThread();
		ClassLoader lastClassLoader=currentThread.getContextClassLoader();
		currentThread.setContextClassLoader(classLoader);
		//
		if(lastClassLoader!=null){
			LinkedList<ClassLoader> stack=threadLocalStack.get();
			if(stack==null){
				stack=new LinkedList<ClassLoader>();
				threadLocalStack.set(stack);
			}
			stack.addFirst(lastClassLoader);
		}
	}
	
	public static ClassLoader pop(){
		Thread curThread=Thread.currentThread();
		LinkedList<ClassLoader> stack=threadLocalStack.get();
		if(stack!=null){
			ClassLoader lastClassLoader=stack.removeFirst();
			if(lastClassLoader!=null){
				curThread.setContextClassLoader(lastClassLoader);
			}else{
				curThread.setContextClassLoader(null);
			}
			return lastClassLoader;
		}else{
			curThread.setContextClassLoader(null);
		}
		return null;
	}
}
