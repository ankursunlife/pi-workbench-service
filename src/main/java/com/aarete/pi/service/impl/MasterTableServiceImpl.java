/**
 * 
 */
package com.aarete.pi.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aarete.pi.bean.User;
import com.aarete.pi.bean.UserRequest;
import com.aarete.pi.dao.MasterTableDAO;
import com.aarete.pi.entity.UserEntity;
import com.aarete.pi.repository.MasterTableUserRepository;
import com.aarete.pi.service.MasterTableService;

/**
 * @author vjadhav
 *
 */
@Service
public class MasterTableServiceImpl implements MasterTableService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MasterTableUserRepository masterTableRepo;
	
	@Autowired
	private MasterTableDAO masterTableDAO;

	@Override
	public List<UserEntity> addUser(@Valid List<User> userList) {
		List<UserEntity> usersEntity = new ArrayList<>();
		try {
			userList.forEach(user -> {
				UserEntity userEntity = new UserEntity();
				userEntity.setUserId(user.getUserId());
				userEntity.setUserName(user.getUserName());
				userEntity.setUserType(user.getUserType());
				userEntity.setImageUrl(user.getImageUrl());
				usersEntity.add(userEntity);
			});
		}catch (Exception e) {
			logger.error(String.format("exception occured while add users: %s ", e.getMessage()));
		}
		return masterTableRepo.saveAll(usersEntity);
	}

	@Override
	public List<UserEntity> getUsers(@Valid UserRequest userRequest, String userId) {
		return masterTableDAO.getUsers(userRequest, userId);
	}

}
