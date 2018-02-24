package com.xincheng.competence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Competence implements Serializable, Comparable<Competence> {

	private String id; // 当前权限URL的id
	private String name; // 当前权限的名称
	private String url; // 当前权限的路径,没有路径值为null
	private Integer order;
	private List<Competence> subMenus; // 下级名称和路径

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Competence> getSubMenus() {
		return subMenus;
	}

	public void setSubMenus(List<Competence> subMenus) {
		this.subMenus = subMenus;
	}

	@Override
	public int compareTo(Competence arg0) {
		if (arg0 == null || arg0.getOrder() == null) {
			return -1;
		}

		if (this == arg0 || this.getOrder() == null) {
			return 0;
		}

		return arg0.getOrder() <= this.getOrder() ? 1 : -1;
	}
}
