package com.aarete.pi.service;

import com.aarete.pi.bean.IdNameBean;

import java.util.List;

/**
 * @author mpalla
 */
public interface ApplicationJdbcService {

	List<IdNameBean> findAll(String name);

	IdNameBean findById(String name, int id);

	int deleteById(String name, String id);

	List<IdNameBean> save(String name, List<IdNameBean> bean);

	int update(String name, IdNameBean bean);
}
