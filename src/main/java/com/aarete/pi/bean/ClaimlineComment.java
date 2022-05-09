package com.aarete.pi.bean;

import java.sql.Timestamp;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Bean to send/receive claimline comments")
public class ClaimlineComment {

	@ApiModelProperty(notes = "Add Comment for claimline/s level", example = "Only comment, engageMentId and claimLineIdList is expected in the request.", required = true)
	private String comment;

	@ApiModelProperty(notes = "Add a claimLineId/list to add comment at multiple claims/claimLines", required = true)
	private List<Long> claimLineIdList;

	@ApiModelProperty(notes = "Response field*")
	private Long claimLineId;

	@ApiModelProperty(notes = "Response field*")
	private String userId;

	@ApiModelProperty(notes = "Response field*")
	private long claimNum;

	@ApiModelProperty(notes = "Response field*")
	private int claimLineNum;

	@ApiModelProperty(notes = "Response field*")
	private Timestamp commentTime;

	@ApiModelProperty(notes = "Response field*")
	private String commentTimestamp;

	@ApiModelProperty(notes = "Response field*")
	private String userName;

	@ApiModelProperty(notes = "Role based on selected engagement", example = "AARETE_USER, AARETE_MANAGER, CLIENT_USER, CLIENT_MANAGER", required = true)
	private String engagementRole;

}
