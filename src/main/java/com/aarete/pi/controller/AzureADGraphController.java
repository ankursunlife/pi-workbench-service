package com.aarete.pi.controller;

import static com.aarete.pi.util.JsonParser.extractResponse;
import static com.aarete.pi.util.JsonParser.fetchCustomAttributes;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.bean.UserGroup;
import com.aarete.pi.bean.adgraph.AdUsersConfigResponse;
import com.aarete.pi.bean.adgraph.GetAdUsersMetadataResponse;
import com.aarete.pi.bean.adgraph.GetUsersCustomeAttributeResponse;
import com.aarete.pi.bean.adgraph.TokenResponse;
import com.aarete.pi.exception.AccessDenied;
import com.aarete.pi.exception.BadRequestException;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.helper.SecurityHelper;
import com.aarete.pi.helper.ValidationHelper;
import com.aarete.pi.service.AzureADGraphService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/v1/ad-graph")
@Api(tags = "Azure AD Graph API" , value = "AzureADGraphApi")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AzureADGraphController {
		
	@Autowired
	AzureADGraphService adGraphService;
	
	/**
	 * @return
	 * @throws AccessDenied
	 * @throws JsonProcessingException
	 * @throws RecordNotFound 
	 */
	@GetMapping(value = "/token", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "To get access_token to call AD Graph API's" , 
	notes = "API has GET operation to get access_token to call Microsoft AD graph API's")
	public ResponseEntity<TokenResponse> getGraphToken() throws AccessDenied, JsonProcessingException, RecordNotFound{
		TokenResponse accessTokenResponse = null;
		UserDetailsBean userInfo = SecurityHelper.getUserDetailsBean();
		if(null != userInfo && !userInfo.getToken().isEmpty()) {
			accessTokenResponse = adGraphService.getTokenResponse(userInfo);
		}
		return new ResponseEntity<>(accessTokenResponse, HttpStatus.OK);
	}
	
	/**
	 * @param userObjectId
	 * @return
	 * @throws AccessDenied
	 * @throws JsonProcessingException
	 * @throws RecordNotFound 
	 */
	@GetMapping(value = "/users/getcustom-attributes", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "To get User configured AD graph Custom Attributes" ,
	notes = "API has GET operation to get Custom Attributes - Ex Codes as configured. [Note:] Pass {oid} to get user specific attributes")
	public ResponseEntity<GetUsersCustomeAttributeResponse> getUserCustomAttributes(@Valid @RequestParam(name = "oid" , required = false) String userObjectId) throws AccessDenied, JsonProcessingException, RecordNotFound{
		TokenResponse accessTokenResponse = null;
		String strResponse = null;
		GetUsersCustomeAttributeResponse usersCustomAttributes = null;
		UserDetailsBean userInfo = SecurityHelper.getUserDetailsBean();
		if(null != userInfo && StringUtils.hasText(userInfo.getToken())) {
			accessTokenResponse = adGraphService.getTokenResponse(userInfo);
			userInfo.setToken(accessTokenResponse.getAccessToken());
			
			if(StringUtils.hasText(userObjectId))
				userInfo.setObjectId(userObjectId);
			
			strResponse = adGraphService.getUserCustomeAttributeValues(userInfo);
			usersCustomAttributes = fetchCustomAttributes(strResponse);
		}else {
			throw new RecordNotFound("User or Token details not found");
		}
		return new ResponseEntity<>(usersCustomAttributes, HttpStatus.OK);
	}
	
	/**
	 * @param cName
	 * @param engagement
	 * @param exCode
	 * @return
	 * @throws AccessDenied
	 * @throws JsonProcessingException
	 * @throws RecordNotFound
	 */
	@GetMapping(value = "/users/getusers-metadata", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "To get AD graph Users List per Engagement" , 
	notes = "API has GET operation to get Users List from Client, Engagement, Ex-codes & groupLevel as configured. [Note:] Pass clientName/engagement/excode/groupLevel to get users [e.g: Moda, ModaConfig, 'Group', ]")
	public ResponseEntity<GetAdUsersMetadataResponse> getUsers(@Valid @RequestParam(name = "clientName" , required = true) String cName,
			@Valid @RequestParam(name = "engagement" , required = true) String engagement, 
			@Valid @RequestParam(name = "exCode" , required = true) String exCode, @Valid @RequestParam(name = "groupLevel" , required = false) String groupLevel) 
			throws AccessDenied, JsonProcessingException, RecordNotFound{
		TokenResponse accessTokenResponse = null;
		List<AdUsersConfigResponse> resMetadata = null;
		GetAdUsersMetadataResponse userMetadataResponse = null;
		
		UserDetailsBean userInfo = SecurityHelper.getUserDetailsBean();
		if(null != userInfo && StringUtils.hasText(userInfo.getToken())) {
			accessTokenResponse = adGraphService.getTokenResponse(userInfo);
			userInfo.setToken(accessTokenResponse.getAccessToken());	
			resMetadata = adGraphService.getUsersWithMetadata(userInfo, cName, engagement, exCode, groupLevel);
			userMetadataResponse = extractResponse(resMetadata);
		}else {
			throw new RecordNotFound("User or Token details not found");
		}
		return new ResponseEntity<>(userMetadataResponse, HttpStatus.OK);
	}
	
	/**
	 * @param search
	 * @param select
	 * @param orderby
	 * @return
	 * @throws AccessDenied
	 * @throws JsonProcessingException
	 * @throws RecordNotFound
	 */
	@GetMapping(value = "/users/getusers-groups", produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "To get AD graph Users groups" , 
	notes = "API has GET operation to get Users Groups. [Note:] Pass $search & $select to filter groups [e.g: search=\"displayName:PI20\" & select=id,displayName ]")
	public ResponseEntity<List<UserGroup>> getUserGroups(@Valid @RequestParam(name = "search" , required = false) String search,
			@Valid @RequestParam(name = "select" , required = true) String select, 
			@Valid @RequestParam(name = "orderby" , required = false) String orderby) 
			throws AccessDenied, JsonProcessingException, RecordNotFound{
		TokenResponse accessTokenResponse = null;
		List<UserGroup> groupDetails = null;
		
		UserDetailsBean userInfo = SecurityHelper.getUserDetailsBean();
		if(null != userInfo && StringUtils.hasText(userInfo.getToken())) {
			accessTokenResponse = adGraphService.getTokenResponse(userInfo);
			userInfo.setToken(accessTokenResponse.getAccessToken());	
			groupDetails = adGraphService.fetchUserGroups(userInfo, search, select, orderby);
		}else {
			throw new RecordNotFound("User or Token details not found");
		}
		return new ResponseEntity<>(groupDetails, HttpStatus.OK);
	}
	
	/**
	 * @param clientName
	 * @param engagement
	 * @return
	 * @throws AccessDenied
	 * @throws BadRequestException
	 * @throws JsonProcessingException
	 * @throws RecordNotFound
	 */
	@PostMapping(value = "/addusers-metadata")
	@ApiOperation(value = "API to upsert user details from Azure AD",
			notes = "API has POST operation to get Users List from AD per engagement using Client & Engagement and it will update UserDetails Table")
	public ResponseEntity<String> addUsersMetadata(@Valid @RequestParam(name = "clientName" , required = true) String clientName,
			@Valid @RequestParam(name = "engagement" , required = true) String engagement)
			throws AccessDenied, BadRequestException, JsonProcessingException, RecordNotFound {
		ValidationHelper.validateRequest(clientName, engagement);
		UserDetailsBean userContext = SecurityHelper.getUserDetailsBean();
		adGraphService.addUsersDetails(userContext, clientName, engagement);
		return new ResponseEntity<>("AD User Details inserted/updated successfully", HttpStatus.OK);
	}
}
