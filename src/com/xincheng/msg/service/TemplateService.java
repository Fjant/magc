package com.xincheng.msg.service;

import java.util.List;
import java.util.Map;

import com.xincheng.msg.model.Template;

public interface TemplateService {

	public void save(Template entity) throws Exception;

	public Template getById(long id) throws Exception;

	public void update(Template entity) throws Exception;

	public List<Template> getAll() throws Exception;
	
	public List<Template> getByPage(Map<String, Object> filters) throws Exception;
	
	public int count(Map<String, Object> filters) throws Exception;
	
	public void removeById(Long id) throws Exception;
}
