package com.aarete.pi.dao;

import com.aarete.pi.bean.IdNameBean;

import java.util.List;

public interface ApplicationDAO {
	
	List<IdNameBean> findAll(String name);
	
	IdNameBean findById(String name, int id);
	
	int deleteById(String name, String id);

	List<IdNameBean> save(String name, List<IdNameBean> idNameBeans);
	
	int update(String name, IdNameBean masterTableRequest);
}