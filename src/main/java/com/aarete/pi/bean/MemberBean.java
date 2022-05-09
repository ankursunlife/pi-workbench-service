package com.aarete.pi.bean;

import java.sql.Date;
import java.sql.Timestamp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MemberBean {

	@ApiModelProperty(notes = "Auto generated at DB")
	private int id;

	@ApiModelProperty(notes = "MemberId to be shown at UI")
	private String memberId;

	private String memberLastName;

	private String memberFirstName;

	private String memberMiddleName;

	private String memberCity;

	private String memberGender;

	private String memberStateCode;

	private String memberPostalCode;

	private String memberHealthPlan;

	private String patientLastName;

	private String patientFirstName;

	private String patientGender;

	private Date patientBirthDate;
	
	private long batchId;
	
	private Timestamp createdTime;
    
    private Timestamp updatedTime;
}
