package com.aarete.pi.bean.adgraph;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Value {

	@JsonProperty("id")
	public String id;
	@JsonProperty("displayName")
	public String displayName;
	@JsonProperty("mail")
	public String mail;
	@JsonProperty("accountEnabled")
	public Boolean accountEnabled;
	@JsonProperty("customSecurityAttributes")
	public CustomSecurityAttributes customSecurityAttributes;
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
