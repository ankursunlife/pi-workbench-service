package com.aarete.pi.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;


@Entity(name = "Claim_Line")
@Data
@Audited
public class ClaimLineEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "CLAIMLINE_ID")
	private long claimLineId;

	@Column(name = "CLAIM_NUM",nullable = false)
	private long claimNum;

	@Column(name = "CLAIM_LINE_NUM",nullable = false)
	private int claimLineNum;

	@Column(name = "BILL_TYPE_CD")
	private String billTypeCode;// e.g. 21G, 891, 13

	@Column(name = "BILLING_PROV_FULL_NAME")
	private String billingProviderFullName;

	@Column(name = "AAR_MEMBER_ID")
	private String aareteMemberId;

	@Column(name = "CLAIM_FORM_TYPE_CD")
	private String claimFormTypeCode;// e.g. H,M

	@Column(name = "DIAG_CD_1")
	private String diagnosisCode1;

	@Column(name = "DIAG_1_DESC")
	private String diagnosis1Desc;

	@Column(name = "BATCH_ID",nullable = false)
	private long batchId;

	@Column(name = "LINE_FROM_DT")
	private Date claimStartDate;

	@Column(name = "LINE_THRU_DT")
	private Date claimEndDate;

	@Column(name = "RECEIVED_DT")
	private Date receivedDate;

	@Column(name = "REMIT_DT")
	private Date paidDate;

	@Column(name = "LOAD_DATE")
	private Date batchDate;

	@Column(name = "TYPE_OF_SERVICE")
	private int typeOfService;

	@Column(name = "LINE_PAID_AMT")
	private BigDecimal linePaidAmount;

	@Column(name = "LINE_BILLED_AMT")
	private BigDecimal lineBilledAmount;

	@Column(name = "LINE_ALLOWED_AMT")
	private BigDecimal lineAllowedAmount;

	// Claimline

	@Column(name = "REVENUE_CD")
	private String revenueCode;

	@Column(name = "REVENUE_DESC")
	private String revenueDesc;

	@Column(name = "CLAIM_STATUS_CD")
	private String claimStatusCode;

	@Column(name = "PROC_CD_1")
	private int procedureCode1;

	@Column(name = "PLACE_OF_SVC_CD")
	private int posId;

	@Column(name = "APPROVER_LEVEL_ONE")
	private String approverLevelOne;

	@Column(name = "APPROVER_LEVEL_TWO")
	private String approverLevelTwo;

	@Column(name = "APPROVER_LEVEL_THREE")
	private String approverLevelThree;

	@Column(name = "APPROVER_LEVEL_FOUR")
	private String approverLevelFour;

	@Column(name = "CLAIM_STATUS_LEVEL_ONE")
	private String claimStatusLevelOne;

	@Column(name = "CLAIM_STATUS_LEVEL_TWO")
	private String claimStatusLevelTwo;

	@Column(name = "CLAIM_STATUS_LEVEL_THREE")
	private String claimStatusLevelThree;

	@Column(name = "CLAIM_STATUS_LEVEL_FOUR")
	private String claimStatusLevelFour;

	@Column(name = "ENGAGEMENT_ID")
	private String engagementId;

	@Column(name = "PRODUCT_LINE_OF_BUSINESS")
	private String lobId;

	@Column(name = "PRODUCT_STATE_CD")
	private String stateId;

	@Column(name = "OPP_LINE_PAID_AMT")
	private BigDecimal lineOpportunityAmount;

	@Column(name = "LINE_UNIT_CNT")
	private int units;

	@Column(name = "PRODUCT_TYPE")
	private String productType;// e.g. V, M

	@Column(name = "AAR_FINAL_STATUS_CD")
	private String aareteFinalClaimStatusCode;// e.g. Y

	@Column(name = "MEMBER_ID")
	private String memberId;// e.g. A123546579

	@Column(name = "BILLING_PROV_NUM")
	private String billingProviderNumber;

	@Column(name = "RENDERING_PROV_NUM")
	private String renderingProviderNumber;

	@Column(name = "BILLING_PROV_SPECIALTY_1")
	private String billingProviderSpecialityOne;

	@Column(name = "BILLING_PROV_SPECIALTY_2")
	private String billingProviderSpecialityTwo;

	@Column(name = "BILLING_PROV_NPI")
	private long billingProviderNpi;

	@Column(name = "BILLING_PROV_IRS_NUM")
	private long billingProviderIrs;

	@Column(name = "CREATED_BY",nullable = false)
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;

	@Column(name = "CREATED_TIME",nullable = false)
	private Timestamp createdTime = new Timestamp(System.currentTimeMillis());

	@Column(name = "UPDATED_TIME")
	private Timestamp updatedTime;

	@Column(name = "SUB_PRODUCT_CD")
	private String subProductCode;

	@Column(name = "MBR_STATE_CD")
	private String memberStateCode;

	@Column(name = "EX_CODE")
	private String exCode;

	@Column(name = "CPT4_PROC_CD")
	private String cptCode;

	@Column(name = "CPT4_PROC_DESC")
	private String cptDesc;

	@Column(name = "MODEL_SCORE")
	private long modelScore;

	@Column(name = "TAGGED")
	private boolean tagged;

	@Column(name = "PILLAR_ID")
	private String pillarId;

	@Column(name = "PILLAR_NAME")
	private String pillarName;

	@Column(name = "AAR_CLAIM_NUM_SEQ")
	private String sequence;

	@Column(name = "DRG_CD_PAID")
	private String drgCode;

	@Column(name = "CPT4_PROC_MOD_1")
	private String modifier;

	@Column(name = "CONFIDENCE_SCORE")
	private float confidenceScore;

	@Column(name = "EDIT")
	private String edit;

	@Column(name = "ASSIGNED_DATE_TO_APPROVER_ONE")
	private Date assignedToApproverOne;

	@Column(name = "ASSIGNED_DATE_TO_APPROVER_TWO")
	private Date assignedToApproverTwo;

	@Column(name = "ASSIGNED_DATE_TO_APPROVER_THREE")
	private Date assignedToApproverThree;

	@Column(name = "ASSIGNED_DATE_TO_APPROVER_FOUR")
	private Date assignedToApproverFour;

	@Column(name = "APPROVER_LEVEL_ONE_GROUP")
	private String approverLevelOneGroup;

	@Column(name = "APPROVER_LEVEL_TWO_GROUP")
	private String approverLevelTwoGroup;

	@Column(name = "APPROVER_LEVEL_THREE_GROUP")
	private String approverLevelThreeGroup;

	@Column(name = "APPROVER_LEVEL_FOUR_GROUP")
	private String approverLevelFourGroup;

	@Column(name = "APPROVER_ONE_ACTION")
	private String approverOneAction;

	@Column(name = "APPROVER_TWO_ACTION")
	private String approverTwoAction;

	@Column(name = "APPROVER_THREE_ACTION")
	private String approverThreeAction;

	@Column(name = "APPROVER_FOUR_ACTION")
	private String approverFourAction;

	@Column(name = "EX_CODE_LEVEL")
	private String exCodeLevel;

	@Column(name = "EDIT_TYPE")
	private String editType;

	@Column(name = "PRIORITIZATION_SCORE")
	private BigDecimal prioritizationScore;

	@Column(name = "ORDER_RATING")
	private String orderRating;

	@Column(name = "ASSIGNER")
	private String assigner;

	@Column(name = "PEND_REASON")
	private String pendReason;

	@Column(name = "OVERRIDE_REASON")
	private String overrideReason;

	@Column(name = "NO_OF_CLAIM_LINES")
	private int noOfClaimLines;

	@Column(name = "AARETE_USER_PEND_REASON")
	private String aareteUserPendReason;

	@Column(name = "CLIENT_USER_PEND_REASON")
	private String clientUserPendReason;

	@Column(name = "AARETE_USER_OVERRIDE_REASON")
	private String aareteUserOverrideReason;

	@Column(name = "CLIENT_USER_OVDERRIDE_REASON")
	private String clientUserOverrideReason;

	@Column(name = "AARETE_USER_ASSIGNER")
	private String aareteUserAssigner;

	@Column(name = "CLIENT_USER_ASSIGNER")
	private String clientUserAssigner;

	@Column(name = "AARETE_MANAGER_ASSIGNER")
	private String aareteManagerAssigner;

	@Column(name = "CLIENT_MANAGER_ASSIGNER")
	private String clientManagerAssigner;

	@Column(name= "AARETE_MANAGER_PEND_REASON")
	private String aareteManagerPendReason;

	@Column(name = "CLIENT_MANAGER_PEND_REASON")
	private String clientManagerPendReason;

	@Column(name = "AARETE_ASSIGNED_USER")
	private String aareteAssignedUser;

	@Column(name = "CLIENT_ASSIGNED_USER")
	private String clientAssignedUser;

	@Column(name = "PRIMARY_DX_CODE_DESC")
	private String primaryDxCodeDesc;

	@Column(name = "ADJUSTMENT_AMT")
	private BigDecimal adjustmentAmount;

	@OneToMany(mappedBy = "claimLineEntity", cascade = CascadeType.ALL)
	@NotAudited
	private List<ClaimlineExCodeEntity> exCodeBeanList = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_ID_FK")
	@NotAudited
	private MemberEntity member;

	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "PROV_NUM_FK")
	@NotAudited
	private ProviderEntity provider;

	@OneToMany(mappedBy = "claimLineEntity", cascade = CascadeType.ALL)
	@NotAudited
	private List<ClaimlineDiagnosisEntity> diagnosisBeanList = new ArrayList<>();

}
