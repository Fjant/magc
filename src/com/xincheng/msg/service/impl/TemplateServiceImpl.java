package com.xincheng.msg.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xincheng.msg.dao.TemplateDao;
import com.xincheng.msg.model.Template;
import com.xincheng.msg.service.TemplateService;

@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private TemplateDao templateDao;

	@Override
	public void save(Template entity) throws Exception {
		templateDao.insert(entity);
	}

	@Override
	public Template getById(long id) throws Exception {
		return templateDao.get(id);
	}

	@Override
	public void update(Template entity) throws Exception {
		templateDao.update(entity);
	}

	@Override
	public List<Template> getAll() throws Exception {
		return templateDao.getAll();
	}

	@Override
	public List<Template> getByPage(Map<String, Object> filters)
			throws Exception {
		return templateDao.getByPage(filters);
	}

	@Override
	public int count(Map<String, Object> filters) throws Exception {
		return templateDao.count(filters);
	}

	@Override
	public void removeById(Long id) throws Exception{
		templateDao.removeById(id);
		
	}

}
