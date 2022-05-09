package com.aarete.pi.bean;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AssignClaimResponse {
	@ApiModelProperty(notes = "success list of assign claim/lines")
	List<String> assignClaimSuccessList;
	@ApiModelProperty(notes = "failed list of assign claim/lines")
	List<String> assignClaimFailedList;
}
