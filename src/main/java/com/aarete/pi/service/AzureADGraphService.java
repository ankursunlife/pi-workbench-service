/**
 * 
 */
package com.aarete.pi.service;

import java.util.List;

import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.bean.UserGroup;
import com.aarete.pi.bean.adgraph.AdUsersConfigResponse;
import com.aarete.pi.bean.adgraph.TokenResponse;
import com.aarete.pi.exception.RecordNotFound;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author vjadhav
 *
 */
public interface AzureADGraphService {
	/**
	 * @param userInfo
	 * @return
	 * @throws JsonProcessingException
	 * @throws RecordNotFound
	 */
	public TokenResponse getTokenResponse(UserDetailsBean userInfo) throws JsonProcessingException, RecordNotFound;
	/**
	 * @param userInfo
	 * @return
	 */
	public String getUserCustomeAttributeValues(UserDetailsBean userInfo);
	/**
	 * @param userInfo
	 * @param clientName
	 * @param engagemenId
	 * @param exCode
	 * @param groupLevel
	 * @return
	 */
	public List<AdUsersConfigResponse> getUsersWithMetadata(UserDetailsBean userInfo,String clientName, String engagemenId, String exCode, String groupLevel);
	/**
	 * @param userInfo
	 * @param search
	 * @param select
	 * @param orderby
	 * @return
	 */
	public List<UserGroup> fetchUserGroups(UserDetailsBean userInfo, String search, String select,
			String orderby);
	/**
	 * @param userContext
	 * @param clientName
	 * @param engagement
	 * @throws JsonProcessingException
	 * @throws RecordNotFound
	 */
	public void addUsersDetails(UserDetailsBean userContext, String clientName, String engagement) throws JsonProcessingException, RecordNotFound;
}
