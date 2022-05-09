/**
 * 
 */
package com.aarete.pi.service.impl;


import static com.aarete.pi.constant.WorkbenchConstants.AUTHORIZATION;
import static com.aarete.pi.constant.WorkbenchConstants.CLIENT_ID;
import static com.aarete.pi.constant.WorkbenchConstants.CLIENT_SECRET;
import static com.aarete.pi.constant.WorkbenchConstants.GRANT_TYPE;
import static com.aarete.pi.constant.WorkbenchConstants.QUERY_LEVEL;
import static com.aarete.pi.constant.WorkbenchConstants.SCP;
import static com.aarete.pi.constant.WorkbenchConstants.TOKEN_BEARER;
import static com.aarete.pi.constant.WorkbenchConstants.TOKEN_USE;
import static com.aarete.pi.constant.WorkbenchConstants.U_TOKEN;
import static com.aarete.pi.util.CommonUtils.httpHeaders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.bean.UserGroup;
import com.aarete.pi.bean.adgraph.AdUsersConfigResponse;
import com.aarete.pi.bean.adgraph.AdUsersMetadataList;
import com.aarete.pi.bean.adgraph.GetAdUsersMetadataResponse;
import com.aarete.pi.bean.adgraph.TokenResponse;
import com.aarete.pi.dao.AzureADGraphDAO;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.helper.ADHelper;
import com.aarete.pi.service.AzureADGraphService;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author vjadhav
 *
 */
@Service
public class AzureADGraphServiceImpl implements AzureADGraphService {
	
	private static final Logger logger = LoggerFactory.getLogger(AzureADGraphServiceImpl.class);
	
	@Value("${aad.graphDefaultScope}")
	private String scope;
	@Value("${aad.graph.base.url}")
	private String graphUrl;
	@Value("${aad.graph.login.url}")
	private String loginUrl;
	@Value("${aad.graph.token.use}")
	private String tokenUse;
	@Value("${security.oauth2.client.client-id}")
	private String clientId;
	@Value("${security.oauth2.client.client-secret}")
	private String clientSecret;
	@Value("${aad.graph.grant.type}")
	private String grantType;
	@Value("${aad.graph.consistency.level}")
	private String consistencyLevel;
	@Value("${aad.graph.token.url}")
	private String tokenUrl;
	
	@Autowired
	private ADHelper adHelper;
	
	@Autowired
	private AzureADGraphDAO adGraphDAO;
	
	public WebClient getWebClient(String baseUrl) {
		return WebClient.builder().baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
	
	@Override
	public TokenResponse getTokenResponse(UserDetailsBean userInfo) throws JsonProcessingException, RecordNotFound {
								
    	MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
    	valueMap.add(TOKEN_USE, tokenUse);
    	valueMap.add(CLIENT_ID, clientId);
    	valueMap.add(CLIENT_SECRET, clientSecret);
    	valueMap.add(GRANT_TYPE, grantType);
    	valueMap.add(SCP, scope);
    	valueMap.add(U_TOKEN, userInfo.getToken());
    	
    	TokenResponse response = getWebClient(loginUrl).post()
			    .uri(userInfo.getTid() + tokenUrl)
			    .header(AUTHORIZATION, TOKEN_BEARER +userInfo.getToken())
			    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
			    .accept(MediaType.APPLICATION_JSON)
			    .body(BodyInserters.fromFormData(valueMap))
			    .retrieve()
			    .bodyToMono(TokenResponse.class)
			    .block();
    	
    	if(null != response && StringUtils.hasText(response.getAccessToken())) {
    		logger.info("access_token: {}", response.getAccessToken());
    	}else {
    		throw new RecordNotFound("Access Token Not Found");
    	}
		return response;
	}

	@Override
	public String getUserCustomeAttributeValues(UserDetailsBean userInfo) {
		String response = getWebClient(graphUrl).get()
			    .uri(userInfo.getObjectId() +"?$select=customSecurityAttributes")
			    .header(AUTHORIZATION, TOKEN_BEARER +userInfo.getToken())
			    .accept(MediaType.APPLICATION_JSON)
			    .retrieve()
			    .bodyToMono(String.class)
			    .block();
		return response;
	}

	@Override
	public List<AdUsersConfigResponse> getUsersWithMetadata(UserDetailsBean userInfo, String clientName, String engagemenId, 
			String exCode, String groupLevel) {
		List<AdUsersConfigResponse> adUsersConfigResponseList = new ArrayList<>();
		List<String> engagementGroups = null;
		if(!StringUtils.hasText(groupLevel)) {
			engagementGroups = Arrays.asList(engagemenId, engagemenId+"Lvl1", engagemenId+"Lvl2", engagemenId+"Lvl3");
		}else {
			engagementGroups = Arrays.asList(groupLevel);
		}

		engagementGroups.stream().forEach(engagementGroup -> {
			StringBuilder uriBuilder = new StringBuilder(
					"?$count=true&$select=id,displayName,mail,AccountEnabled,customSecurityAttributes&$filter=");
			uriBuilder.append("startsWith(customSecurityAttributes")
			.append("/"+clientName)
			.append("/"+engagementGroup)
			.append(",'"+exCode+"')");
			
			Map<String,String> headerMap = new LinkedHashMap<>();
			headerMap.put(AUTHORIZATION, TOKEN_BEARER +userInfo.getToken());
			headerMap.put(QUERY_LEVEL, consistencyLevel);
			
			AdUsersConfigResponse response = getWebClient(graphUrl).get()
				    .uri(uriBuilder.toString())
				    .headers(httpHeaders(headerMap))
				    .accept(MediaType.APPLICATION_JSON)
				    .retrieve()
				    .bodyToMono(AdUsersConfigResponse.class)
				    .block();
			adUsersConfigResponseList.add(response);
		});
		return adUsersConfigResponseList;
	}

	@Override
	public List<UserGroup> fetchUserGroups(UserDetailsBean userInfo, String search, String select, String orderby) {
		StringBuilder uriBuilder = new StringBuilder();
		uriBuilder.append(userInfo.getObjectId())
		.append("/memberOf/microsoft.graph.group?")
		.append("$count=true&$orderby="+orderby)
		.append(StringUtils.hasText(search) ? "&$search="+"\""+search+"\"" : "")
		.append("&$select="+select);
		
		Map<String,String> headerMap = new LinkedHashMap<>();
		headerMap.put(AUTHORIZATION, TOKEN_BEARER +userInfo.getToken());
		headerMap.put(QUERY_LEVEL, consistencyLevel);
		
		LinkedHashMap<String, Object> payloadMap = (LinkedHashMap<String, Object>) getWebClient(graphUrl).get()
			    .uri(uriBuilder.toString())
			    .headers(httpHeaders(headerMap))
			    .accept(MediaType.APPLICATION_JSON)
			    .retrieve()
			    .bodyToMono(LinkedHashMap.class)
			    .block();
		return fetchGroups(payloadMap);
	}
	
	private List<UserGroup> fetchGroups(LinkedHashMap<String, Object> payloadMap){
		List<UserGroup> userGroupsList = new ArrayList<>();
		if(null != payloadMap && null != payloadMap.get("value")) {
			ArrayList<LinkedHashMap<String, String>> groupsData = (ArrayList<LinkedHashMap<String, String>>) payloadMap.get("value");
			groupsData.forEach(groupMap -> {
				UserGroup userGroup = new UserGroup();
				for (Map.Entry<String, String> entry : groupMap.entrySet()) {
					if("id".equals(entry.getKey()))
						userGroup.setId(entry.getValue());
					if("displayName".equals(entry.getKey()))
						userGroup.setGroupName(entry.getValue());
				}
				userGroupsList.add(userGroup);
			});
		}
		return userGroupsList;
	}

	@Override
	@Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = { SQLException.class })
	public void addUsersDetails(UserDetailsBean userContext, String clientName, String engagement) throws JsonProcessingException, RecordNotFound {
		GetAdUsersMetadataResponse metadata = adHelper.getUsersMetadata(userContext, clientName, engagement);
		if(Objects.nonNull(metadata) && !CollectionUtils.isEmpty(metadata.getAdUsersMetadataList())) {
			List<AdUsersMetadataList> usersList = metadata.getAdUsersMetadataList();
			adGraphDAO.addUserDetails(usersList, userContext);
		}
	}

}
