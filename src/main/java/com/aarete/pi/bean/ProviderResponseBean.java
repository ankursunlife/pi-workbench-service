package com.aarete.pi.bean;

import java.util.List;

import lombok.Data;

@Data
public class ProviderResponseBean {

	private List<IdNameBean> providerNameList;
	
	private List<IdNameBean> providerNPIList;
	
	private List<IdNameBean> providerIRSList;
}
