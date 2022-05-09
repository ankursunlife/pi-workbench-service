package com.aarete.pi.bean;

import java.math.BigDecimal;
import java.sql.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "This bean will be sent when cross claim details are required.")
public class ClaimBean {

	private long claimNum;
	
	private Date claimFromDate;
	
	private Date claimThruDate;
	
	private String claimFormTypeCode;
	
	private String billTypeCode;
	
	private int units;
	
	private BigDecimal aarClaimBilled;

	private BigDecimal aarClaimAllowed;

	private BigDecimal aarClaimPaid;
	
	private String exCode;
}
