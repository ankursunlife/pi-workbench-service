/**
 * 
 */
package com.aarete.pi.bean.adgraph;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author vjadhav
 *
 */
@Data
public class GetAdUsersMetadataResponse {
	
	@JsonProperty("userDataCount")
	private Integer userDataCount;
	@JsonProperty("adUsersMetadataList")
	private List<AdUsersMetadataList> adUsersMetadataList;
}
