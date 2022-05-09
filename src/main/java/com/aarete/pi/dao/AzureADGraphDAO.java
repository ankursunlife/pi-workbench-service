package com.aarete.pi.dao;

import java.util.List;

import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.bean.adgraph.AdUsersMetadataList;
import com.aarete.pi.exception.RecordNotFound;

public interface AzureADGraphDAO {
	/**
	 * @param userList
	 * @param userContext
	 * @throws RecordNotFound
	 */
	public void addUserDetails(List<AdUsersMetadataList> userList, UserDetailsBean userContext) throws RecordNotFound; 
}
