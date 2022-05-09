package com.aarete.pi.controller;

import com.aarete.pi.bean.ClaimLineResponse;
import com.aarete.pi.bean.ClaimWorkflowStatusRequest;
import com.aarete.pi.exception.RecordNotFound;
import com.aarete.pi.service.ClaimWorkflowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/workflow-process")
@Api(tags = "Workflow Management Note: Do not use this api's now", value = "WorkflowManagement")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ClaimWorkflowController {

    @Autowired
    private ClaimWorkflowService claimWorkflowService;

    /**
     * @param claimWorkflowStatusRequest
     * @return
     * @throws RecordNotFound
     */
    @PutMapping(value = "/update-claim-status", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 200, message = "Update Success") })
    @ApiOperation(value = "Call this api to update the status of claim.", notes = "Possible values IN_PROGRESS, CLOSED, NULL")
    public ResponseEntity<Void> updateClaimStatus(@Valid @RequestBody ClaimWorkflowStatusRequest claimWorkflowStatusRequest) throws RecordNotFound {
        // TODO add validation for ClaimStatusRequest
        claimWorkflowService.updateClaimStatus(claimWorkflowStatusRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param engagementId
     * @param claimNum
     * @return
     */
    @PostMapping("/get-all-claimlines/{engagementId}/{claimNum}")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Server error"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 200, message = "Success") })
    @ApiOperation(value = "Call this api to get all claimlines", notes = "Possible values IN_PROGRESS, CLOSED, NULL")
    public ResponseEntity<List<ClaimLineResponse>> getAllClaimLines(@ApiParam(type = "long", required = true)
                                                                        @NotBlank @PathVariable("engagementId") String engagementId,
                                                                    @ApiParam(type = "long", required = true)
                                                                    @NotBlank @PathVariable("claimNum") long claimNum) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
