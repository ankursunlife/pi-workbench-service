package com.aarete.pi.bean.adgraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdUsersConfigResponse {
	@JsonProperty("@odata.context")
	public String odataContext;
	@JsonProperty("@odata.count")
	public Integer odataCount;
	@JsonProperty("value")
	public List<Value> value = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}
}
