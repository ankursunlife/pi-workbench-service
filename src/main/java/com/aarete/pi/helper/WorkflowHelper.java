package com.aarete.pi.helper;

import com.aarete.pi.bean.ClaimProcessRequest;
import com.aarete.pi.bean.ClaimWorkflowRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class WorkflowHelper {

    @Value("${claim.level.excode.accept.url}")
    private String claimLevelExCodeAcceptUrl;

    @Value("${claim.level.excode.reject.url}")
    private String claimLevelExCodeRejectUrl;

    @Value("${claimline.level.excode.accept.url}")
    private String claimLineLevelExCodeAcceptUrl;

    @Value("${claimline.level.excode.reject.url}")
    private String claimLineLevelExCodeRejectUrl;

    @Autowired
    private RestTemplate restTemplate;

    HttpHeaders headers = new HttpHeaders();

    @PostConstruct
    private void init() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }

    @Async
    public CompletableFuture<Map<Integer, List<ClaimProcessRequest>>> sendClaimLevelAccept(ClaimWorkflowRequest claimWorkflowRequest) {
        Map<Integer, List<ClaimProcessRequest>> httpStatusListMap = new HashMap<>();
        try {
            HttpEntity<ClaimWorkflowRequest> httpEntity = new HttpEntity(claimWorkflowRequest, headers);
            ResponseEntity<String> response = this.restTemplate.postForEntity(claimLevelExCodeAcceptUrl, httpEntity, String.class);
            httpStatusListMap.put(response.getStatusCode().value(), claimWorkflowRequest.getClaimProcessList());
        } catch (RestClientException e) {
            httpStatusListMap.put(HttpStatus.INTERNAL_SERVER_ERROR.value(), claimWorkflowRequest.getClaimProcessList());
        }
        return CompletableFuture.completedFuture(httpStatusListMap);
    }

    @Async
    public CompletableFuture<Map<Integer, List<ClaimProcessRequest>>> sendClaimLineLevelAccept(ClaimWorkflowRequest claimWorkflowRequest) {
        Map<Integer, List<ClaimProcessRequest>> httpStatusListMap = new HashMap<>();
        try {
            HttpEntity<ClaimWorkflowRequest> httpEntity = new HttpEntity(claimWorkflowRequest, headers);
            ResponseEntity<String> response = this.restTemplate.postForEntity(claimLineLevelExCodeAcceptUrl, httpEntity, String.class);
            httpStatusListMap.put(response.getStatusCode().value(), claimWorkflowRequest.getClaimProcessList());
        } catch (RestClientException e) {
            httpStatusListMap.put(HttpStatus.INTERNAL_SERVER_ERROR.value(), claimWorkflowRequest.getClaimProcessList());
        }
        return CompletableFuture.completedFuture(httpStatusListMap);
    }
}
