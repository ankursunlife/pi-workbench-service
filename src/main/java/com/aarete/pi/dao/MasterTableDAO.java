package com.aarete.pi.dao;

import java.util.List;

import javax.validation.Valid;

import com.aarete.pi.bean.UserRequest;
import com.aarete.pi.entity.UserEntity;

public interface MasterTableDAO {

	List<UserEntity> getUsers(@Valid UserRequest userRequest, String userId);

	List<String> getUserNames(@Valid List<String> userIds);
}
