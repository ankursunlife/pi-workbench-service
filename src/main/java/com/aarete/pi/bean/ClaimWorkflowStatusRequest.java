package com.aarete.pi.bean;

import lombok.Data;

import java.util.List;

@Data
public class ClaimWorkflowStatusRequest {

    private List<ClaimStatusRequest> claimStatusList;
}
