package com.aarete.pi.service.impl;

import com.aarete.pi.bean.IdNameBean;
import com.aarete.pi.dao.ApplicationDAO;
import com.aarete.pi.service.ApplicationJdbcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mpalla
 */

@Service
public class ApplicationJdbcServiceImpl implements ApplicationJdbcService {

    @Autowired
    private ApplicationDAO dao;

    @Override
    public List<IdNameBean> findAll(String name) {
        return dao.findAll(name);
    }

    @Override
    public IdNameBean findById(String name, int id) {
        return dao.findById(name, id);
    }

    @Override
    public int deleteById(String name, String id) {
        return dao.deleteById(name, id);
    }

    @Override
    public List<IdNameBean> save(String name, List<IdNameBean> idNameBeans) {
        return dao.save(name, idNameBeans);
    }

    @Override
    public int update(String name, IdNameBean bean) {
        return dao.update(name, bean);
    }

}
