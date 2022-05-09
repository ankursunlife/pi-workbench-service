package com.aarete.pi.bean;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class Playlist {

	@ApiModelProperty(notes = "Response only and Use while sharing playlist")
	private long playlistId;
	
	@ApiModelProperty(notes = "Need to send when creating Playlist")
	private String playlistName;
	
	@ApiModelProperty(notes = "Need to send when creating Playlist")
    private String playlistDesc;
	
	@ApiModelProperty(notes = "Put claimline ids that need to be added to the playlist")
    private List<Long> claimLineIdsToAdd = new ArrayList<>();
    
	@ApiModelProperty(notes = "Put claimline ids that need to be removed to the playlist")
    private List<Long> claimLineIdsToRemove = new ArrayList<>();

    @ApiModelProperty(notes = "Response only: returns duplicate claims in playlist")
    private List<String> duplicateClaimLineIds = new ArrayList<>();

    @ApiModelProperty(notes = "Response only: returns duplicate user names")
    private List<String> duplicateUserNames = new ArrayList<>();
    
    @ApiModelProperty(notes = "Send user id list of the user list with whom playlist needs to be shared")
    private List<String> sharedUserIdList;
    
    @ApiModelProperty(notes = "Request only: Put playlist ids to which single/multiple claim line can be added")
    private List<Integer> playlistIds;
    
    @ApiModelProperty(notes = "Do not send it in the request")
    private String playlistOwnerId;
    
    @ApiModelProperty(notes = "Do not send it in the request")
    private String loggedInUser;

    @ApiModelProperty(notes = "Response only: my playlist or shared playlist count")
    private int claimCount;

    @ApiModelProperty(notes = "Request only: send MY_QUEUE when claim lines needs to be added to MY_QUEUE." +
            "\n Set this variable while calling from GROUP_QUEUE screen.")
    private String queueType;

    @ApiModelProperty(notes = "Role based on selected engagement", example = "AARETE_USER, AARETE_MANAGER, CLIENT_USER, CLIENT_MANAGER")
    private String engagementRole;

    @ApiModelProperty(notes = "Engagement Id is mandatory")
    private String engagementId;

    @ApiModelProperty(notes = "List of users details with whom playlist is shared")
    private List<User> sharedUserList;
    
}
