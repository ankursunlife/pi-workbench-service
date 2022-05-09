package com.aarete.pi.bean;

import java.util.List;

import lombok.Data;

@Data
public class UserDetailsBean {

	private String userId;//Email id
	private List<String> roleList;
	private String name;
	private String objectId;
	private List<String> groups;
	private String token;
	private String tid;
	private List<UserGroup> groupDetailList;
}
