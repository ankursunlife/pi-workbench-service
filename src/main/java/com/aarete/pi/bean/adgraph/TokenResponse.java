package com.aarete.pi.bean.adgraph;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author vjadhav
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TokenResponse {

	@JsonProperty("token_type")
	public String tokenType;
	@JsonProperty("scope")
	public String scope;
	@JsonProperty("expires_in")
	public Integer expiresIn;
	@JsonProperty("ext_expires_in")
	public Integer extExpiresIn;
	@JsonProperty("access_token")
	public String accessToken;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

}
