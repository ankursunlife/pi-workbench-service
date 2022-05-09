package com.aarete.pi.service;

import java.util.List;

import javax.validation.Valid;

import com.aarete.pi.bean.User;
import com.aarete.pi.bean.UserRequest;
import com.aarete.pi.entity.UserEntity;

public interface MasterTableService {

	public List<UserEntity> addUser(@Valid List<User> userList);

	public List<UserEntity> getUsers(@Valid UserRequest userRequest, String userId);
	
}
