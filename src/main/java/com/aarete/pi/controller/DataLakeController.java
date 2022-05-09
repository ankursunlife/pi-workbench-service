package com.aarete.pi.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aarete.pi.bean.ClaimDetailsRequest;
import com.aarete.pi.bean.ClaimDetailsResponse;
import com.aarete.pi.bean.ClaimsListRequest;
import com.aarete.pi.bean.ClaimsListResponse;
import com.aarete.pi.service.DataLakeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/datalake")
@Api(tags = "Historical Claims Management", value = "HistoricalClaims")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DataLakeController {
	@Autowired
	DataLakeService dataLakeService;

	/**
	 * @param claimDetailsRequest
	 * @return claimDetailsResponse
	 */
	@PostMapping(value = "/claim-details", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Get claim details for an engagement and claim number", notes = "Service returns results with max 100 rows (page size), populate queryExecutionId and nextToken for subsequent pages")
	public ResponseEntity<ClaimDetailsResponse> getClaimDetails(@Valid @RequestBody ClaimDetailsRequest claimDetailsRequest) {
		ClaimDetailsResponse claimDetailsResponse = dataLakeService.getClaimDetails(claimDetailsRequest);
		return new ResponseEntity<>(claimDetailsResponse, HttpStatus.OK);
	}
	
	/**
	 * @param claimsListRequest
	 * @return claimsListResponse
	 */
	@PostMapping(value = "/claims-list", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Get claims list for a given member / provider and/or additional filters", notes = "Service returns results with max 100 rows (page size), populate queryExecutionId and nextToken for subsequent pages")
	public ResponseEntity<ClaimsListResponse> getClaimsList(@Valid @RequestBody ClaimsListRequest claimsListRequest) {
		ClaimsListResponse claimsListResponse = dataLakeService.getClaimsList(claimsListRequest);
		return new ResponseEntity<>(claimsListResponse, HttpStatus.OK);
	}
	
}
