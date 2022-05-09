package com.aarete.pi.bean;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "Bean to receive claimline comments")
public class ClaimlineCommentResponse {

	List<ClaimlineComment> claimlineCommentList;
    
}
