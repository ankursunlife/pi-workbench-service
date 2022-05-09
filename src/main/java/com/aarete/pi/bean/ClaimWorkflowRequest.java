package com.aarete.pi.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ClaimWorkflowRequest {
    @NotNull
    private List<ClaimProcessRequest> claimProcessList;
}
