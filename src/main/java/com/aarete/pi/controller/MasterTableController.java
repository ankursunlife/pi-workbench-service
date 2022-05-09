package com.aarete.pi.controller;

import static com.aarete.pi.helper.SecurityHelper.getUserDetailsBean;
import static org.springframework.util.StringUtils.hasText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aarete.pi.bean.EngagementFilterRequest;
import com.aarete.pi.bean.FilterCodeResponseBean;
import com.aarete.pi.bean.IdNameBean;
import com.aarete.pi.bean.MyFilterRequestBean;
import com.aarete.pi.bean.ProviderResponseBean;
import com.aarete.pi.bean.User;
import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.bean.UserRequest;
import com.aarete.pi.entity.UserEntity;
import com.aarete.pi.exception.AccessDenied;
import com.aarete.pi.exception.BadRequestException;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.helper.ValidationHelper;
import com.aarete.pi.service.ApplicationJdbcService;
import com.aarete.pi.service.ClaimService;
import com.aarete.pi.service.MasterTableService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/list")
@Api(tags = "MasterTable", value = "Master Table CRUD Operations APIs")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MasterTableController {

	public static final String RECORD_NOT_FOUND = "Record not found";

	@Autowired
	private ApplicationJdbcService appJdbcService;

	@Autowired
	private ClaimService claimService;
	
	@Autowired
	private MasterTableService masterTableService;

	/**
	 * @param name
	 * @param idNameBeans
	 * @return
	 */
	@PostMapping("/{name}")
	@ApiOperation(value = "To add master data",
			notes = "This API will be used to add master data")
	public ResponseEntity<List<IdNameBean>> addMasterData(@NotBlank @PathVariable("name") String name,
			@Valid @RequestBody List<IdNameBean> idNameBeans) {
		List<IdNameBean> response = appJdbcService.save(name, idNameBeans);
		if (response != null) {
			return new ResponseEntity<>(idNameBeans, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * @param name
	 * @return
	 * @throws RecordNotFound
	 */
	@GetMapping("/all/{name}")
	@ApiOperation(value = "To get all master data",
			notes = "This API will be used to get master data")
	public ResponseEntity<List<IdNameBean>> getAllMasterData(@PathVariable("name") String name) throws RecordNotFound {
		List<IdNameBean> masterTableResponse = appJdbcService.findAll(name);
		if (masterTableResponse != null && !masterTableResponse.isEmpty()) {
			return new ResponseEntity<>(masterTableResponse, HttpStatus.OK);
		} else {
			throw new RecordNotFound(RECORD_NOT_FOUND);
		}
	}

	/**
	 * @param name
	 * @param idNameBean
	 * @return
	 * @throws RecordNotFound
	 */
	@PutMapping("/{name}")
	@ApiOperation(value = "To get update master data",
			notes = "This API will be used to update master data")
	public ResponseEntity<IdNameBean> updateMasterData(@PathVariable("name") String name,
			@Valid @RequestBody IdNameBean idNameBean) throws RecordNotFound {
		int count = appJdbcService.update(name, idNameBean);
		if (count > 0) {
			return new ResponseEntity<>(idNameBean, HttpStatus.OK);
		} else {
			throw new RecordNotFound(RECORD_NOT_FOUND);
		}
	}

	/**
	 * @param name
	 * @param id
	 * @return
	 * @throws RecordNotFound
	 */
	@DeleteMapping("/{name}/{id}")
	@ApiOperation(value = "To get delete master data",
			notes = "This API will be used to delete master data by id")
	public ResponseEntity<Void> deleteMasterData(@PathVariable("name") String name, @PathVariable("id") String id)
			throws RecordNotFound {
		int count = appJdbcService.deleteById(name, id);
		if (count > 0) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			throw new RecordNotFound(RECORD_NOT_FOUND);
		}
	}


	/**
	 * @param engagementFilterRequest
	 * @return
	 * @throws AccessDenied
	 */
	@PostMapping(value = "/providers", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Get Provider Name, NPI, IRS list.",
			notes = "This API will be used for the filter population. Engagement role and Id is mandatory.")
	public ResponseEntity<ProviderResponseBean> getProviderList(
			@Valid @RequestBody EngagementFilterRequest engagementFilterRequest) throws AccessDenied {

		UserDetailsBean userDetailsBean = getUserDetailsBean();
		engagementFilterRequest.setLoggedInUserEmailId(userDetailsBean.getUserId());
		ProviderResponseBean providerBean = claimService.getProvidersDetails(engagementFilterRequest);
		if (Objects.nonNull(providerBean)) {
			return new ResponseEntity<>(providerBean, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ProviderResponseBean(), HttpStatus.OK);
		}
	}

	/**
	 * @param myFilterRequestBean
	 * @return
	 * @throws BadRequestException
	 * @throws AccessDenied
	 */
	@PostMapping(value = "/my-code-list", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Get Revenue, CPT, Diagnosis list.",
	notes = "Based on logged in user and role. Get unique values from the claimlines and create a list with description to populate on the filter.")
	public ResponseEntity<FilterCodeResponseBean> getMyCodeList(
			@Valid @RequestBody MyFilterRequestBean myFilterRequestBean)
			throws BadRequestException, AccessDenied {

		ValidationHelper.validateCodesList(myFilterRequestBean);
		FilterCodeResponseBean codeResponseBean = new FilterCodeResponseBean();
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		myFilterRequestBean.getEngagementFilters().setLoggedInUserEmailId(userDetailsBean.getUserId());
		List<IdNameBean> myCodeList = claimService.getMyCodeList(myFilterRequestBean);
		if (Objects.isNull(myCodeList)) {
			myCodeList = new ArrayList<>();
		}
		codeResponseBean.setCodeList(myCodeList);
		return new ResponseEntity<>(codeResponseBean, HttpStatus.OK);
	}

	@PostMapping(value = "/batch-number", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Get Batch number list",
			notes = "Response will be having unique batch list. This API will be used for the Batch number filter population.")
	public ResponseEntity<List<IdNameBean>> getBatchNumberList(
			@Valid @RequestBody MyFilterRequestBean myFilterRequestBean) throws AccessDenied, RecordNotFound {
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		myFilterRequestBean.getEngagementFilters().setLoggedInUserEmailId(userDetailsBean.getUserId());
		Optional<List<IdNameBean>> batchListOptional = claimService.getBatchNumberList(myFilterRequestBean);
		if (batchListOptional.isPresent() && !CollectionUtils.isEmpty(batchListOptional.get())) {
			return new ResponseEntity<>(batchListOptional.get(), HttpStatus.OK);
		} else {
			throw new RecordNotFound(RECORD_NOT_FOUND);
		}
	}

	@PostMapping(value = "/user-list", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Get User list for Engagement to share PlayList",
	notes =  "API has POST operation to get User List from UserDetails table for an engagement" +
			" Note: logged in User will be excluded from list ")
	public ResponseEntity<List<UserEntity>> getUserList(@Valid @RequestBody UserRequest userRequest)
			throws BadRequestException, AccessDenied {
		if(!Objects.isNull(userRequest) && hasText(userRequest.getEngagementRole())) {
			UserDetailsBean userDetailsBean = getUserDetailsBean();
			List<UserEntity> userEntity = masterTableService.getUsers(userRequest, userDetailsBean.getUserId());
			return new ResponseEntity<>(userEntity, HttpStatus.OK);
		}else {
			throw new BadRequestException("Engagement details not provided");
		}
		
	}

	@PostMapping(value = "/add-user-list", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Add user list" ,
	notes = "API is used to insert user details | for internal call only - Not used in real time, because Users are bieng added from AD API - /v1/ad-graph/addusers-metadata")
	public ResponseEntity<List<UserEntity>> addUserList(@Valid @RequestBody List<User> userList)
			throws BadRequestException {
		List<UserEntity> userEntity;
		ValidationHelper.validateAddUser(userList);
		userEntity = masterTableService.addUser(userList);
		return new ResponseEntity<>(userEntity, HttpStatus.OK);
	}

}
