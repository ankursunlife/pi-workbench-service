package com.aarete.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "Member")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class MemberEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	@Column(name = "MEMBER_ID")
	private String memberId;

	@Column(name = "MBR_LAST_NAME")
	private String memberLastName;

	@Column(name = "MBR_FIRST_NAME")
	private String memberFirstName;

	@Column(name = "MBR_MIDDLE_NAME")
	private String memberMiddleName;

	@Column(name = "MBR_CITY")
	private String memberCity;
	
	@Column(name = "MBR_SEX")
	private String memberGender;

	@Column(name = "MBR_STATE_CD")
	private String memberStateCode;

	@Column(name = "MBR_POSTAL_CD")
	private String memberPostalCode;
	
	@Column(name = "MBR_HEALTH_PLAN")
	private String memberHealthPlan;

	@Column(name = "PAT_LAST_NAME")
	private String patientLastName;

	@Column(name = "PAT_FIRST_NAME")
	private String patientFirstName;

	@Column(name = "PAT_SEX")
	private String patientGender;

	@Column(name = "PAT_BIRTH_DT")
	private Date patientBirthDate;
	
	@Column(name = "BATCH_ID")
	private long batchId;
	
	@Column(name = "CREATED_TIME")
    private Timestamp createdTime;
    
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "UPDATED_TIME")
	private Timestamp updatedTime;

}
