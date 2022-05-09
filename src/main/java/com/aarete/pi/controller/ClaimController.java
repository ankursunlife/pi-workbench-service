package com.aarete.pi.controller;

import static com.aarete.pi.helper.ClaimServiceHelper.getClaimLineEntities;
import static com.aarete.pi.helper.SecurityHelper.getUserDetailsBean;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.aarete.pi.bean.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aarete.pi.entity.ClaimLineEntity;
import com.aarete.pi.entity.ClaimlineCommentEntity;
import com.aarete.pi.exception.AccessDenied;
import com.aarete.pi.exception.BadRequestException;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.helper.ADHelper;
import com.aarete.pi.helper.ExcelHelper;
import com.aarete.pi.helper.Helper;
import com.aarete.pi.helper.ValidationHelper;
import com.aarete.pi.service.ClaimService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/claim")
@Api(tags = "Claims Management",
		value = "ClaimsManagement APIs required for populating claimlines, claimline details, claimline workflow, show summary, counts etc. ")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClaimController {

	@Autowired
	private ClaimService claimService;

	@Autowired
	private ADHelper adHelper;

	/**
	 * @param claimRequest
	 * @return
	 * @throws BadRequestException
	 * @throws AccessDenied
	 * @throws RecordNotFound 
	 * @throws JsonProcessingException 
	 */
	@PostMapping(value = "/summary-by", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Gets Claims Summary for selected filter", notes = "Populate filter, engagementFilters and summaryBy in ClaimRequest.")
	public ResponseEntity<SummaryResponse> summaryBy(@Valid @RequestBody ClaimRequest claimRequest)
			throws BadRequestException, AccessDenied, JsonProcessingException, RecordNotFound {
		SummaryResponse summaryResponse;
		ValidationHelper.validateSummaryByRequest(claimRequest);
		setUserAndGroupNames(claimRequest);
		summaryResponse = claimService.summaryBy(claimRequest);
		return new ResponseEntity<>(summaryResponse, HttpStatus.OK);
	}

	/**
	 * @param claimRequest
	 * @return
	 * @throws BadRequestException
	 * @throws AccessDenied
	 */
	@PostMapping(value = "/claimline-list", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Gets All Claimlists for selected filter", notes = "If no filter is selected, API will return all eligible records of the user.")
	public ResponseEntity<ClaimResponse> getClaimLineList(@Valid @RequestBody ClaimRequest claimRequest)
			throws BadRequestException, AccessDenied, RecordNotFound, JsonProcessingException {
		ClaimResponse claimResponse;
		ValidationHelper.validateGetClaimLineList(claimRequest);
		setUserAndGroupNames(claimRequest);
		claimResponse = claimService.getClaimLineList(claimRequest);
		return new ResponseEntity<>(claimResponse, HttpStatus.OK);
	}

	private void setUserAndGroupNames(ClaimRequest claimRequest) throws AccessDenied, JsonProcessingException, RecordNotFound {
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		claimRequest.getEngagementFilters().setLoggedInUserEmailId(userDetailsBean.getUserId());
		List<String> userGroupsNames = adHelper.getUserGroupNames(String.valueOf(claimRequest.getEngagementFilters().getEngagementId()), claimRequest.getEngagementFilters().getEngagementRole(), userDetailsBean);
		claimRequest.getEngagementFilters().setGroupNames(userGroupsNames);
	}

	/**
	 * @param claimRequest
	 * @return
	 * @throws BadRequestException
	 * @throws AccessDenied
	 * @throws IOException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	@PostMapping(value = "/download/claimline-list", consumes = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Gets All Claimlists for selected filter", notes = "If no filter is selected, API will return all eligible records of the user.")

	public ResponseEntity<ClaimDownloadResponse> downloadClaimLineList(@Valid @RequestBody ClaimRequest claimRequest)
			throws BadRequestException, AccessDenied, IllegalAccessException, InvocationTargetException,
			NoSuchMethodException, IOException {
		ClaimResponse claimResponse;
		ValidationHelper.validateGetClaimLineList(claimRequest);
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		claimRequest.getEngagementFilters().setLoggedInUserEmailId(userDetailsBean.getUserId());
		ClaimDownloadResponse claimDownloadResponse = new ClaimDownloadResponse();

		claimDownloadResponse.setStatus("SUCCESS");
		claimDownloadResponse.setMessage("Request processed successfully");
		// claimDownloadResponse.setLocation(fileLocation);
		claimResponse = claimService.getClaimLineList(claimRequest);
		downLoadExcelFile(claimRequest, claimResponse);
		return new ResponseEntity<>(claimDownloadResponse, HttpStatus.OK);

	}


	@Async
	private void downLoadExcelFile(ClaimRequest claimRequest, ClaimResponse claimResponse)
			throws IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		ExcelHelper.exportExcel(claimResponse.getClaimLines(), claimRequest.getExportRequest());
	}

	/**
	 * @param claimLineId
	 * @return
	 */
	@GetMapping("/claimline-details")
	@ApiOperation(value = "Gets All claimline details for the selected claimline", notes = "Send a climline ID received in the claimline list response. It is different from the claim num.")
	public ResponseEntity<ClaimLineDetailsResponse> getClaimLineBean(
			@Valid @RequestParam("claimLineId") long claimLineId) {
		ClaimLineDetailsResponse claimLineDetailsResponse = new ClaimLineDetailsResponse();
		// try {
		// UserDetailsBean userDetailsBean = getUserDetailsBean();
		// TODO get userid to get the data
		ClaimLineBean claimLineBean = claimService.getClaimLineBean(claimLineId);

		if (Objects.isNull(claimLineBean)) {
			return new ResponseEntity<>(claimLineDetailsResponse, HttpStatus.NO_CONTENT);
		}
		claimLineDetailsResponse.setClaimLineBean(claimLineBean);
		// } catch (Exception e) {
		// log.error("Error in getting claim line details.", e);
		// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		// }
		return new ResponseEntity<>(claimLineDetailsResponse, HttpStatus.OK);
	}


	/**
	 * @param claimWorkflowRequest
	 * @return
	 * @throws AccessDenied
	 */
	@PutMapping("/process-claims")
	@ApiOperation(value = "Need to call when user takes action on claimlines from the MyQueue page or Claimline Details.", notes = "Populate claimProcessRequestList from claimRequest. At a time only 1 user can take 1 type of action on multiple claimlines.")
	public ResponseEntity<String> processClaims(@Valid @RequestBody ClaimWorkflowRequest claimWorkflowRequest)
			throws AccessDenied {
		String claimResponse;
		
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		for(ClaimProcessRequest claimProcessRequest : claimWorkflowRequest.getClaimProcessList()) {
			claimProcessRequest.setActionTakenBy(userDetailsBean.getUserId());
		}
		claimResponse = claimService.processClaims(claimWorkflowRequest);
		return new ResponseEntity<>(claimResponse, HttpStatus.OK);
	}

	@GetMapping("/health-check")
	@ApiOperation(value = "Just simple Hi..!!!")
	public String healthCheck() {
		return "Hi, how are you? It's 22-04-2022 21:00??";
	}

	/**
	 * @param claimNumber
	 * @param claimCount
	 * @param passPhrase
	 * @return
	 * @throws Exception
	 */
	@GetMapping("/insert-dummy")
	@ApiOperation(value = "Internal API. Dummy data insert." ,notes = "Do not call this API. If claimCount is 1, it will insert 5 claims with 10 claimlines each.")
	public ResponseEntity<String> insertDummay(@NotBlank @RequestParam("claimNumber") long claimNumber,
			@NotBlank @RequestParam("claimCount") int claimCount,
			@NotBlank @RequestParam("passPhrase") String passPhrase) throws Exception {
		int totalRecordsAdded = 0;
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		List<ClaimLineBean> claimLineBeanList = Helper.dummySaveClaimlineData(claimNumber, claimCount, passPhrase,
				userDetailsBean.getUserId());
		List<ClaimLineEntity> claimLineEntities = getClaimLineEntities(claimLineBeanList);
		claimService.saveClaimLineList(claimLineEntities);
		totalRecordsAdded = claimLineEntities.size();
		return new ResponseEntity<>("Total records added : " + totalRecordsAdded, HttpStatus.OK);
	}

	/**
	 * @param claimLineBeans
	 * @return
	 */
	@ApiOperation(value = "Save Claimline entry in the DB.")
	@PostMapping(value = "/claimline", consumes = { MediaType.APPLICATION_JSON_VALUE }) // TODO make it list, should be
																						// able to save multiple records
																						// in 1 go
	public ResponseEntity<Void> saveClaimLineList(@Valid @RequestBody List<ClaimLineBean> claimLineBeans) {
		claimService.saveClaimLineList(getClaimLineEntities(claimLineBeans));
		return new ResponseEntity<>(HttpStatus.OK);
	}


	/**
	 * @param claimRequest
	 * @return
	 */
	@ApiOperation(value = "Export functionality for Excel and CSV files")
	@PostMapping(value = "/export", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Void> export(@Valid @RequestBody ClaimRequest claimRequest) {
		claimService.export(claimRequest);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * @param claimlineComment
	 * @return
	 * @throws BadRequestException
	 * @throws AccessDenied
	 */
	@ApiOperation(value = "Add comments at single/multiple claimline level")
	@PostMapping(value = "/add-comment", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ClaimlineCommentEntity>> addComment(
			@Valid @RequestBody ClaimlineComment claimlineComment) throws BadRequestException, AccessDenied {
		List<ClaimlineCommentEntity> claimLineCommentEntities;
		ValidationHelper.validateAddComment(claimlineComment);
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		claimlineComment.setUserId(userDetailsBean.getUserId());
		claimLineCommentEntities = claimService.addComment(claimlineComment);
		return new ResponseEntity<>(claimLineCommentEntities, HttpStatus.OK);
	}

	
	/**
	 * @param claimlineComment
	 * @return
	 */
	@ApiOperation(value = "Get comments at single/multiple claimline level")
	@PostMapping(value = "/get-comments", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ClaimlineCommentResponse> getComments(@Valid @RequestBody ClaimlineComment claimlineComment) {
		ClaimlineCommentResponse claimlineCommentResponse = claimService.getComments(claimlineComment);
		return new ResponseEntity<>(claimlineCommentResponse, HttpStatus.OK);
	}

	/**
	 * @param engagementId
	 * @param claimNum
	 * @return
	 */
	@GetMapping("/claim-details/{engagementId}/{claimNum}")
	@ApiOperation(value = "Get claim details", notes = "Get claim details, claimline list and its details for given claim num")
	public ResponseEntity<ClaimBean> getClaimDetails(@NotBlank @Valid @RequestParam("engagementId") String engagementId,
			@NotBlank @Valid @RequestParam("claimNum") long claimNum) {
		ClaimBean claimBean = claimService.getClaimDetails(claimNum);
		return new ResponseEntity<>(claimBean, HttpStatus.OK);
	}

	/**
	 * @param engagementId
	 * @param claimlineId
	 * @return
	 */
	@GetMapping("/claims-all-claimline/{engagementId}/{claimlineId}")
	@ApiOperation(value = "Get claims all claimlines of the claim", notes = "")
	public ResponseEntity<ClaimBean> getClaimsAllClaimlineList(
			@NotBlank @Valid @RequestParam("engagementId") String engagementId,
			@NotBlank @Valid @RequestParam("claimlineId") long claimlineId) {
		ClaimBean claimBean = claimService.getClaimsAllClaimlineList(claimlineId, engagementId);
		return new ResponseEntity<>(claimBean, HttpStatus.OK);
	}
	
	/**
	 * @param assignClaimRequest
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "Assign claims to MY_QUEUE/PEND", notes = "API is used to assign claims to MY_QUEUE/PEND when ActionTaken is ASSIGN or PEND")
	@PostMapping(value = "/assign-claims", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<AssignClaimResponse> processClaimLines( @Valid @RequestBody AssignClaimRequest assignClaimRequest) throws Exception {
		ValidationHelper.validateAssignClaimlines(assignClaimRequest);
		UserDetailsBean userDetailsBean = getUserDetailsBean();
		AssignClaimResponse assignResponse = claimService.processClaimLines(assignClaimRequest, userDetailsBean);
		return new ResponseEntity<>(assignResponse,HttpStatus.OK);
	}

	@ApiOperation(value = "Get summary claimline list details", notes = "Summary API is used to get the claimline's details for given claimline list")
	@PostMapping(value = "/summary-claimlinelist-details", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ClaimlineListDetailsResponse> getClaimlineListDetails(
			@Valid @RequestBody ClaimlineListDetailsRequest claimlineListDetailsRequest) throws BadRequestException{
		ValidationHelper.validateClaimLineList(claimlineListDetailsRequest);
		ClaimlineListDetailsResponse claimlineListDetailsResponse = claimService.getClaimLineListSummary(claimlineListDetailsRequest);
		return new ResponseEntity<>(claimlineListDetailsResponse,HttpStatus.OK);
	}

}
