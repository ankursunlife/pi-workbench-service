package com.aarete.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "Provider")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProviderEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	@Column(name = "PROV_NUM")
	private String providerNumber;

	@Column(name = "PROV_FULL_NAME")
	private String providerFullName;

	@Column(name = "PROV_STATE_CD")
	private String providerStateCode;
	
	@Column(name = "PROV_ZIP")
	private String providerZip;
	
	@Column(name = "PROV_GROUP_NAME")
	private String providerGroupName;
	
	@Column(name = "PROV_BILLING_NAME")
	private String providerBillingName;
	
	@Column(name = "PROV_TYPE")
	private String providerType;
	
	@Column(name = "BATCH_ID")
	private long batchId;
	
	@Column(name = "CREATED_TIME")
    private Timestamp createdTime = new Timestamp(System.currentTimeMillis());
    
    @Column(name = "UPDATED_TIME")
    private Timestamp updatedTime;

	@Column(name = "CREATED_BY",nullable = false)
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

}
