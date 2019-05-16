package com.springBoot1.boot;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;

import org.springframework.beans.factory.config.BeanDefinition;

@SuppressWarnings("rawtypes")
public class AopBean{

	private Class anno;
	private BeanDefinition beanDefinition;
	private Method method;
	private AopConfig aopConfig;
	private ElementType elementType = null;	// TYPE on class,METHOD on method
	
	public Class getAnno() {
		return anno;
	}
	public void setAnno(Class anno) {
		this.anno = anno;
	}
	public BeanDefinition getBeanDefinition() {
		return beanDefinition;
	}
	public void setBeanDefinition(BeanDefinition beanDefinition) {
		this.beanDefinition = beanDefinition;
	}
	public ElementType getElementType() {
		return elementType;
	}
	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public AopConfig getAopConfig() {
		return aopConfig;
	}
	public void setAopConfig(AopConfig aopConfig) {
		this.aopConfig = aopConfig;
	}
	
}
	