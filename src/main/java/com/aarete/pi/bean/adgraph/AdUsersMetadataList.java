/**
 * 
 */
package com.aarete.pi.bean.adgraph;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author vjadhav
 *
 */
@Data
public class AdUsersMetadataList {
	@JsonProperty("id")
	public String id;
	@JsonProperty("displayName")
	public String displayName;
	@JsonProperty("mail")
	public String mail;
	@JsonProperty("accountEnabled")
	public Boolean accountEnabled;
	@JsonProperty("usersCustomeAttributes")
	private GetUsersCustomeAttributeResponse usersCustomeAttributes;
	
}
