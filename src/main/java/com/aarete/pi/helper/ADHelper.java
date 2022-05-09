/**
 * 
 */
package com.aarete.pi.helper;

import static org.springframework.util.StringUtils.hasText;
import static com.aarete.pi.constant.WorkbenchConstants.SELECT;
import static com.aarete.pi.util.JsonParser.extractResponse;
import static com.aarete.pi.util.JsonParser.fetchCustomAttributes;
import static com.aarete.pi.constant.WorkbenchConstants.ORDER_BY;

import java.util.ArrayList;
import java.util.List;

import com.aarete.pi.constant.WorkbenchConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.bean.UserGroup;
import com.aarete.pi.bean.adgraph.AdUsersConfigResponse;
import com.aarete.pi.bean.adgraph.GetAdUsersMetadataResponse;
import com.aarete.pi.bean.adgraph.GetUsersCustomeAttributeResponse;
import com.aarete.pi.bean.adgraph.TokenResponse;
import com.aarete.pi.exception.AccessDenied;
import com.aarete.pi.exception.BadRequestException;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.service.AzureADGraphService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author vjadhav
 *
 */
@Component
public class ADHelper {
		
	@Autowired
	AzureADGraphService adGraphService;
	
	private ADHelper() {}
	
	/**
	 * @param engagementId
	 * @param engagementRole
	 * @return
	 * @throws AccessDenied
	 * @throws JsonProcessingException
	 * @throws RecordNotFound
	 * @throws BadRequestException
	 */
	public List<UserGroup> getUserGroups(String engagementId, String engagementRole) throws AccessDenied, JsonProcessingException, RecordNotFound, BadRequestException{
		List<UserGroup> groups = null;
		if(hasText(engagementId) && hasText(engagementRole)) {
			UserDetailsBean userDetails = SecurityHelper.getUserDetailsBean();
			if (null != userDetails && hasText(userDetails.getToken())) {
				TokenResponse accessTokenResponse = adGraphService.getTokenResponse(userDetails);
				userDetails.setToken(accessTokenResponse.getAccessToken());
				StringBuilder buildSearch = new StringBuilder("displayName:");
				buildSearch.append(engagementId)
				.append("_")
				.append(engagementRole);
				groups = adGraphService.fetchUserGroups(userDetails, buildSearch.toString(), SELECT,ORDER_BY);
			} else {
				throw new RecordNotFound("User or Token details not found");
			}
		}else {
			throw new BadRequestException("engagement Id/Role can not be null");
		}
		return groups;
	}
	
	/**
	 * @param engagementId
	 * @param engagementRole
	 * @return
	 * @throws JsonProcessingException
	 * @throws AccessDenied
	 * @throws RecordNotFound
	 */
	public List<String> getGroupNamesFromADMatrix(String engagementId, String engagementRole, UserDetailsBean userDetails) throws JsonProcessingException, AccessDenied, RecordNotFound{
		//TODO fetch groups based on engagementId e.g: MHP = Moda/ModaConfig
		GetUsersCustomeAttributeResponse customAttributesResponse = null;
		TokenResponse accessTokenResponse = null;
		List<String> userGroups = new ArrayList<>();
		if(hasText(engagementId) && hasText(engagementRole)) {
			if (null != userDetails && hasText(userDetails.getToken())) {
				accessTokenResponse = adGraphService.getTokenResponse(userDetails);
				userDetails.setToken(accessTokenResponse.getAccessToken());
				String strCustomAttributes = adGraphService.getUserCustomeAttributeValues(userDetails);
				customAttributesResponse = fetchCustomAttributes(strCustomAttributes);
				if(null != customAttributesResponse && !CollectionUtils.isEmpty(customAttributesResponse.getExCodes())) {
					customAttributesResponse.getExCodes().forEach(matrixConfig -> {
						if(matrixConfig.contains("Group-")) {
							String[] groupName = matrixConfig.split("Group-");
							userGroups.add((groupName.length > 0 && !groupName[1].isBlank()) ? groupName[1] : WorkbenchConstants.DEFAULT_GROUP);
						}
					});
				}else {
					throw new RecordNotFound("AD custom attributes or assigned roles not configured");
				}
			}else {
				throw new RecordNotFound("User or token details not found");
			}
		}		
		return userGroups;
	}

	/**
	 * @param engagementId
	 * @param engagementRole
	 * @return
	 * @throws RecordNotFound 
	 * @throws AccessDenied 
	 * @throws JsonProcessingException 
	 */
	public List<String> getUserGroupNames(String engagementId, String engagementRole, UserDetailsBean userDetailsBean) throws JsonProcessingException, AccessDenied, RecordNotFound {
		List<String> groups = new ArrayList<>();
		if(hasText(engagementId) && hasText(engagementRole)) {
			groups = getGroupNamesFromADMatrix(engagementId, engagementRole, userDetailsBean);
		}

		if(groups.isEmpty()) {
			groups.add("AUGroup4");
		}
		return groups;
	}
	
	public GetAdUsersMetadataResponse getUsersMetadata(UserDetailsBean userInfo, String cName, String engagement) throws JsonProcessingException, RecordNotFound {
		TokenResponse accessTokenResponse = null;
		List<AdUsersConfigResponse> resMetadata = null;
		GetAdUsersMetadataResponse userMetadataResponse = null;
		if(null != userInfo && StringUtils.hasText(userInfo.getToken())) {
			accessTokenResponse = adGraphService.getTokenResponse(userInfo);
			userInfo.setToken(accessTokenResponse.getAccessToken());	
			resMetadata = adGraphService.getUsersWithMetadata(userInfo, cName, engagement, "Group", null);
			userMetadataResponse = extractResponse(resMetadata);
		}else {
			throw new RecordNotFound("User or Token details not found");
		}
		return userMetadataResponse;
	}
}