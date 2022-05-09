package com.aarete.pi.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

@Data
public class PlaylistResponse {
	
	@ApiModelProperty(notes = "This list will be populated when list of all/shared playlist is called.")
    private List<Playlist> playLists;
    
	@ApiModelProperty(notes = "This bean will be populated when Playlist details api is called.")
    private Playlist playlist;
    
    @ApiModelProperty(notes = "This list will be populated when Playlist details are called.")
    private List<ClaimLineBean> claimLines;
    
    private int claimCount;
    
    private BigDecimal playlistOpportunityAmount;
    
    private long playlistProviderCount;
    
    private long playlistExCodes;
    
    private String createdBy;
    
    private String updatedBy;
    
    private Timestamp createdTime;
    
    private Timestamp updatedTime;
}
