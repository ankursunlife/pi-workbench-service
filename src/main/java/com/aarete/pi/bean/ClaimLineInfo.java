package com.aarete.pi.bean;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

@Data
public class ClaimLineInfo {

	private long claimNum;
	
	private int claimLineNum;
	
	private int sequence;
	
	private Date lineFromDate;
	
	private String placeOfService;
	
	private String revenueCode;
	
	private String modifier;
	
	private String cptCode;
	
	private int units;
	
	private BigDecimal lineBilledAmount;
	
	private BigDecimal lineAllowedAmount;
	
	private BigDecimal linePaidAmount;
	
	private String exCode;
	
	private String drgCode;
}
