package com.springBoot1.boot;

import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("rawtypes")
public class AopConfig{
	private Class anno;
	private String adviceRef;
	private int order;
	
	public Class getAnno() {
		return anno;
	}
	public void setAnno(Class anno) {
		this.anno = anno;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getAdviceRef() {
		return adviceRef;
	}
	public void setAdviceRef(String adviceRef) {
		this.adviceRef = adviceRef;
	}
	
	public AopConfig(Class anno, int order) {
		super();
		this.anno = anno;
		this.order = order;
		String bean = anno.getSimpleName()+"Advice";
		this.adviceRef = StringUtils.uncapitalize(bean);
	}
	
	public AopConfig() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
	