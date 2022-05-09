package com.aarete.pi.helper;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.aarete.pi.bean.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.aarete.pi.entity.ClaimLineEntity;

@Component
public class Helper {

	private static AtomicInteger counter = new AtomicInteger(0);

	private Helper() {

	}

	public static List<ClaimLineBean> dummySaveClaimlineData(long claimNum, int claimCount, String passPhrase,
															 String userId) throws Exception {

		if (!"Temp1234".equals(passPhrase)) {
			throw new Exception("Wrong pass phrase...");
		}
		if (claimCount > 10 && !(userId.equals("mpalla@aarete.com") || userId.equals("akadam@aarete.com"))) {
			throw new Exception("Too many claim count...");
		}

		List<ClaimLineBean> claimLineBeanList = new ArrayList<>();
		Map<Integer, String> providerMap = new HashMap<>();

		providerMap.put(5, "Belmont Hospital");
		providerMap.put(4, "Berkeley Medical Center");
		providerMap.put(2, "Jack Mitchell");
		providerMap.put(3, "Davis DME");
		providerMap.put(1, "Ronald Sanchez");

		Map<Integer, String> stateMap = new HashMap<>();
		stateMap.put(1, "CA");
		stateMap.put(4, "FL");
		stateMap.put(5, "NY");
		stateMap.put(3, "TX");
		stateMap.put(2, "OR");

		Map<Integer, String> providerSpecialityMap = new HashMap<>();
		providerSpecialityMap.put(1, "LPC");
		providerSpecialityMap.put(4, "PULM");
		providerSpecialityMap.put(5, "FMPR");
		providerSpecialityMap.put(3, "NP");
		providerSpecialityMap.put(2, "FMNR");

		Map<Integer, String> cptCodeMap = new HashMap<>();
		cptCodeMap.put(1, "A4385");
		cptCodeMap.put(2, "A0425");
		cptCodeMap.put(3, "97811");
		cptCodeMap.put(4, "99347");
		cptCodeMap.put(5, "K0553");
		cptCodeMap.put(6, "G0009");
		cptCodeMap.put(7, "80053");

		Map<Integer, Integer> posMap = new HashMap<>();
		posMap.put(1, 11);
		posMap.put(4, 21);
		posMap.put(5, 22);
		posMap.put(3, 31);
		posMap.put(2, 31);

		Map<Integer, Date> batchMap = new HashMap<>();
		batchMap.put(1, Date.valueOf(createRandomDate(2020, 2021)));
		batchMap.put(2, Date.valueOf(createRandomDate(2020, 2021)));
		batchMap.put(3, Date.valueOf(createRandomDate(2020, 2021)));
		batchMap.put(4, Date.valueOf(createRandomDate(2020, 2021)));
		batchMap.put(5, Date.valueOf(createRandomDate(2020, 2021)));

		List<String> lobs = Arrays.asList("Medicare", "Medicaid", "Commercial", "Marketplace");
		List<String> subProduct = Arrays.asList("TANAF", "SSI", "CHIP");
		List<String> billType = Arrays.asList("111", "117", "312", "137");
		List<String> claimForm = Arrays.asList("P", "D", "H");
		List<String> diagCode = Arrays.asList("K12.5", "N1.0", "R124.56", "M122.34");
		List<String> revCode = Arrays.asList("124", "112", "113", "141", "251", "254", "307", "311", "423", "430");
		List<String> gender = Arrays.asList("Male", "Female");
		List<String> actionTaken = Arrays.asList("ACCEPT", "REJECT");
		List<String> editType = Arrays.asList("Multi-Line", "Single");
		List<String> edits = Arrays.asList("Paid over Billed", "Corrected Claim Dupes", "MUE",
				"Bill Type ending in 8 or 0", "Add-On Codes", "Allergy Testing", "Lab Drug Testing",
				"DME Capped Rentals", "Global Surgeries","Procedure to Procedure - Medicaid");

		List<BigDecimal> opportunityAmounts = Arrays.asList(BigDecimal.valueOf(36.35), BigDecimal.valueOf(303.25), BigDecimal.valueOf(1279.29),
				BigDecimal.valueOf(20.46), BigDecimal.valueOf(923.52));

		List<BigDecimal> billedAmount = Arrays.asList(BigDecimal.valueOf(144.1), BigDecimal.valueOf(2212.5), BigDecimal.valueOf(305.69),
				BigDecimal.valueOf(313.44), BigDecimal.valueOf(535.04));

		List<BigDecimal> paidAmount = Arrays.asList(BigDecimal.valueOf(1987.95), BigDecimal.valueOf(55.36), BigDecimal.valueOf(245.54),
				BigDecimal.valueOf(313.44), BigDecimal.valueOf(2437.5));

		List<BigDecimal> allowedAmount = Arrays.asList(BigDecimal.valueOf(812.5), BigDecimal.valueOf(2437.5), BigDecimal.valueOf(5.68),
				BigDecimal.valueOf(155.16), BigDecimal.valueOf(535.04));

		List<BigDecimal> adjustmentAmount = Arrays.asList(BigDecimal.valueOf(81.5), BigDecimal.valueOf(243.5), BigDecimal.valueOf(5.68),
				BigDecimal.valueOf(15.16), BigDecimal.valueOf(55.04));

		List<Integer> units = Arrays.asList(2, 3, 6, 8, 10);

		List<String> memberIds = Arrays.asList("65779674879", "26193648088", "11712835743", "61491464530", "50535075317", "06182378763");
		String claimLevelEditType = "";
		Random random = new Random();
		for (int k = 1; k < 6; k++) {
			for (int i = 0; i < claimCount; i++) {
				int claimLineNum = 1;
				claimNum++;
				claimLevelEditType = editType.get(random.nextInt(editType.size()));
				for (int j = 0; j < 10; j++) {
					ClaimLineEntity entity = new ClaimLineEntity();
					ClaimLineBean claimLineBean = new ClaimLineBean();
					claimLineBean.setEditType(claimLevelEditType);
					claimLineBean.setEngagementId("MPH");
					claimLineBean.setLineOpportunityAmount(opportunityAmounts.get(random.nextInt(opportunityAmounts.size())));
					claimLineBean.setLineBilledAmount(billedAmount.get(random.nextInt(billedAmount.size())));
					claimLineBean.setLinePaidAmount(paidAmount.get(random.nextInt(paidAmount.size())));
					claimLineBean.setLineAllowedAmount(allowedAmount.get(random.nextInt(allowedAmount.size())));
					claimLineBean.setClaimLineNum(claimLineNum++);
					claimLineBean.setClaimNum(claimNum);
					claimLineBean.setBillingProviderNumber(String.valueOf(k));
					claimLineBean.setBillingProviderFullName(providerMap.get(k));
					claimLineBean.setBillingProviderNpi(claimNum + 2465);
					claimLineBean.setBillingProviderIrs(claimNum + 3761);
					claimLineBean.setStateId(stateMap.get(k));
					claimLineBean.setMemberStateCode(stateMap.get(k));
					claimLineBean.setBillingProviderSpecialityOne(providerSpecialityMap.get(k));
					claimLineBean.setBillingProviderSpecialityTwo(providerSpecialityMap.get(k));
					claimLineBean.setBillTypeCode(billType.get(random.nextInt(billType.size())));
					claimLineBean.setPosId(posMap.get(k));
					claimLineBean.setDiagnosisCode1(diagCode.get(random.nextInt(diagCode.size())));
					claimLineBean.setRevenueCode(revCode.get(random.nextInt(revCode.size())));
					claimLineBean.setMemberId(memberIds.get(random.nextInt(memberIds.size())));
					claimLineBean.setBatchDate(Date.valueOf(createRandomDate(2020, 2021)));
					claimLineBean.setReceivedDate(Date.valueOf(createRandomDate(2020, 2021)));
					claimLineBean.setPaidDate(Date.valueOf(createRandomDate(2020, 2021)));
					claimLineBean.setBatchDate(batchMap.get(k));
					claimLineBean.setLobId(lobs.get(random.nextInt(lobs.size())));
					claimLineBean.setSubProductCode(subProduct.get(random.nextInt(subProduct.size())));
					if(j < 3) {
						claimLineBean.setPillarId("PP");
						claimLineBean.setPillarName("Payment Policies");
					}else if (j < 6){
						claimLineBean.setPillarId("CB");
						claimLineBean.setPillarName("Covered Benefits");
					}else {
						claimLineBean.setPillarId("PC");
						claimLineBean.setPillarName("Provider Contracts");
					}
					claimLineBean.setSequence(String.valueOf(claimLineNum));
					claimLineBean.setDrgCode("D34." + claimLineNum);
					claimLineBean.setModifier(String.valueOf(claimLineNum + 1));
					claimLineBean.setEdit(edits.get(random.nextInt(edits.size())));

					claimLineBean.setClaimStartDate(Date.valueOf(createRandomDate(2016, 2022)));
					claimLineBean.setClaimEndDate(Date.valueOf(createRandomDate(2020, 2026)));
					claimLineBean.setAdjustmentAmount(adjustmentAmount.get(random.nextInt(adjustmentAmount.size())));
					claimLineBean.setUnits(units.get(random.nextInt(units.size())));

					ExCodeBean exCodeBeanOne = new ExCodeBean();
					ExCodeBean exCodeBeanTwo = new ExCodeBean();

					exCodeBeanOne.setLineOpportunityAmount(claimLineBean.getLineOpportunityAmount());
					exCodeBeanOne.setBrief("This line is typically limited to a number of units per day.");
					exCodeBeanOne.setSource("Provider Manual");
					exCodeBeanOne.setPillar("Payment Policies");
					exCodeBeanOne.setExCodeType("CLAIM_LINE");
					exCodeBeanOne.setApproverOneAction(actionTaken.get(random.nextInt(gender.size())));
					exCodeBeanOne.setConfidenceScore(95);
					if("Paid over Billed".equals(claimLineBean.getEdit())) {
						exCodeBeanOne.setExCodeId("PP.POB.PRO");
						exCodeBeanOne.setExCodeName("PP.POB.PRO");
						exCodeBeanTwo.setExCodeId("PP.POB.FAC");
						exCodeBeanTwo.setExCodeName("PP.POB.FAC");
						exCodeBeanTwo.setConfidenceScore(85);
						claimLineBean.setExCode("PP.POB.FAC");
					}else if("Corrected Claim Dupes".equals(claimLineBean.getEdit())) {
						exCodeBeanOne.setExCodeId("PP.DUP.COP");
						exCodeBeanOne.setExCodeName("PP.DUP.COP");
						exCodeBeanTwo.setExCodeId("PP.DUP.CCO");
						exCodeBeanTwo.setExCodeName("PP.DUP.CCO");
						exCodeBeanOne.setConfidenceScore(82);
						claimLineBean.setExCode("PP.DUP.CCO");
					}
					else if("MUE".equals(claimLineBean.getEdit())) {
						exCodeBeanOne.setExCodeId("PP.MUE.DME");
						exCodeBeanOne.setExCodeName("PP.MUE.DME");
						exCodeBeanTwo.setExCodeId("PP.MUE.PRO");
						exCodeBeanTwo.setExCodeName("PP.MUE.PRO");
						exCodeBeanTwo.setConfidenceScore(90);
						claimLineBean.setExCode("PP.MUE.PRO");
					}else if("DME Capped Rentals".equals(claimLineBean.getEdit())) {
						exCodeBeanOne.setExCodeId("PP.DME.OXY");
						exCodeBeanOne.setExCodeName("PP.DME.OXY");
						exCodeBeanTwo.setExCodeId("PP.DME.OTH");
						exCodeBeanTwo.setExCodeName("PP.DME.OTH");
						claimLineBean.setExCode("PP.DME.OXY");
						exCodeBeanTwo.setConfidenceScore(87);
					}
					else if("Bill Type ending in 8 or 0".equals(claimLineBean.getEdit())) {
						exCodeBeanOne.setExCodeId("PP.BT.8");
						exCodeBeanOne.setExCodeName("PP.BT.8");
						exCodeBeanTwo.setExCodeId("PP.BT.0");
						exCodeBeanTwo.setExCodeName("PP.BT.0");
						claimLineBean.setExCode("PP.BT.0");
						exCodeBeanTwo.setConfidenceScore(92);
					}else if("Add-On Codes".equals(claimLineBean.getEdit())) {
						exCodeBeanOne.setExCodeId("PP.Add.INP");
						exCodeBeanOne.setExCodeName("PP.Add.INP");
						exCodeBeanTwo.setExCodeId("PP.Add.MIP");
						exCodeBeanTwo.setExCodeName("PP.Add.MIP");
						claimLineBean.setExCode("PP.Add.MIP");
						exCodeBeanTwo.setConfidenceScore(80);
					}else if("Allergy Testing".equals(claimLineBean.getEdit())) {
						exCodeBeanOne.setExCodeId("PP.ALG.CPT1");
						exCodeBeanOne.setExCodeName("PP.ALG.CPT1");
						claimLineBean.setExCode("PP.ALG.CPT1");
						exCodeBeanOne.setConfidenceScore(85);
					}else if("Lab Drug Testing".equals(claimLineBean.getEdit())) {
						exCodeBeanTwo.setExCodeId("PP.DRU.PC7");
						exCodeBeanTwo.setExCodeName("PP.DRU.PC7");
						claimLineBean.setExCode("PP.DRU.PC7");
						exCodeBeanTwo.setConfidenceScore(84);
					}else if("Global Surgeries".equals(claimLineBean.getEdit())) {
						exCodeBeanOne.setExCodeId("PP.GS.DDDN");
						exCodeBeanOne.setExCodeName("PP.GS.DDDN");
						claimLineBean.setExCode("PP.PP.GS.DDDN");
						exCodeBeanOne.setConfidenceScore(93);
					}else if("Procedure to Procedure - Medicaid".equals(claimLineBean.getEdit())){
						exCodeBeanOne.setExCodeId("PP.PTI.DME");
						exCodeBeanOne.setExCodeName("PP.PTI.DME");
						exCodeBeanTwo.setExCodeId("PP.PTI.OP");
						exCodeBeanTwo.setExCodeName("PP.PTI.OP");
						claimLineBean.setExCode("PP.PTI.DME");
						exCodeBeanTwo.setConfidenceScore(95);
					}else {
						//default PP.SB.RE
						exCodeBeanOne.setExCodeId("PP.SB.RE");
						exCodeBeanOne.setExCodeName("PP.SB.RE");
						exCodeBeanOne.setConfidenceScore(80);
						claimLineBean.setExCode("PP.SB.RE");
					}
					exCodeBeanTwo.setLineOpportunityAmount(BigDecimal.valueOf(6.19 + (k * 10)));
					exCodeBeanTwo.setBrief("This line is a duplicate of Line 2 of this claim");
					exCodeBeanTwo.setSource("Provider Manual");
					exCodeBeanTwo.setPillar("Payment Policies");
					exCodeBeanTwo.setApproverOneAction(actionTaken.get(random.nextInt(gender.size())));
					List<ExCodeBean> exCodeBeans = new ArrayList<>();
					exCodeBeans.add(exCodeBeanOne);

					if("Multi-Line".equals(claimLineBean.getEditType())) {
						claimLineBean.setNoOfClaimLines(10);
						exCodeBeanTwo.setExCodeType("CLAIM");
						exCodeBeans.add(exCodeBeanTwo);
					} else {
						claimLineBean.setNoOfClaimLines(1);
						exCodeBeanTwo.setExCodeType("CLAIM_LINE");
					}

					claimLineBean.setExCodeBeanList(exCodeBeans);

					MemberBean memberBean = new MemberBean();
					memberBean.setMemberId(String.valueOf(claimNum + 100));
					memberBean.setMemberStateCode(stateMap.get(k));
					memberBean.setMemberHealthPlan("Healthnet");
					memberBean.setMemberGender(gender.get(random.nextInt(gender.size())));
					memberBean.setPatientBirthDate(Date.valueOf(createRandomDate(1970, 1990)));
					memberBean.setMemberPostalCode(String.valueOf(50000 + k));
					claimLineBean.setMember(memberBean);

					ProviderBean providerBean = new ProviderBean();
					providerBean.setProviderNumber(String.valueOf(claimNum + 1));
					providerBean.setProviderFullName(providerMap.get(k));
					providerBean.setProviderStateCode(stateMap.get(k));
					providerBean.setProviderZip(String.valueOf(40000 + k));
					providerBean.setProviderBillingName("John Smith");
					providerBean.setProviderGroupName("Advanced Physicians");
					providerBean.setCreatedBy(userId);
					claimLineBean.setProvider(providerBean);

					List<DiagnosisBean> diagnosisBeanList = new ArrayList<>();
					DiagnosisBean diagnosisBean1 = new DiagnosisBean();
					diagnosisBean1.setDxCode("Z933");
					diagnosisBean1.setPoa("X" + counter.incrementAndGet());
					diagnosisBean1.setDescription("Description of D20");

					DiagnosisBean diagnosisBean2 = new DiagnosisBean();
					diagnosisBean2.setDxCode("E1165");
					diagnosisBean2.setPoa("Y" + counter.incrementAndGet());
					diagnosisBean2.setDescription("Description of E30");

					DiagnosisBean diagnosisBean3 = new DiagnosisBean();
					diagnosisBean3.setDxCode("Z23");
					diagnosisBean3.setPoa("Z" + counter.incrementAndGet());
					diagnosisBean3.setDescription("Description of F40");
					diagnosisBeanList.add(diagnosisBean1);
					diagnosisBeanList.add(diagnosisBean2);
					diagnosisBeanList.add(diagnosisBean3);
					claimLineBean.setDiagnosisBeanList(diagnosisBeanList);

					claimLineBean.setClaimStatusLevelOne("GROUP_QUEUE");
					claimLineBean.setClaimStatusLevelTwo("GROUP_QUEUE");
					claimLineBean.setClaimStatusLevelThree("GROUP_QUEUE");
					claimLineBean.setClaimStatusLevelFour("GROUP_QUEUE");
					/*if (k < 3) {
						claimLineBean.setExCode("PTP");
					} else if (k < 5) {
						claimLineBean.setExCode("DUP");
					} else if (k < 6) {
						claimLineBean.setExCode("MUE");
					}*/

					if (j < 2) {
						claimLineBean.setApproverLevelOneGroup("AUGroup1");
						claimLineBean.setApproverLevelTwoGroup("Lvl1Group1");
						claimLineBean.setApproverLevelThreeGroup("Lvl2Group1");
						claimLineBean.setApproverLevelFourGroup("Lvl3Group1");
					} else if (j < 5) {
						claimLineBean.setApproverLevelOneGroup("AUGroup2");
						claimLineBean.setApproverLevelTwoGroup("Lvl1Group2");
						claimLineBean.setApproverLevelThreeGroup("Lvl2Group2");
						claimLineBean.setApproverLevelFourGroup("Lvl3Group1");
					} else if (j < 8) {
						claimLineBean.setApproverLevelOneGroup("AUGroup3");
						claimLineBean.setApproverLevelTwoGroup("Lvl1Group3");
						claimLineBean.setApproverLevelThreeGroup("Lvl2Group2");
						claimLineBean.setApproverLevelFourGroup("Lvl3Group1");
					}else {
						claimLineBean.setApproverLevelOneGroup("AUGroup4");
						claimLineBean.setApproverLevelTwoGroup("Lvl1Group1");
						claimLineBean.setApproverLevelThreeGroup("Lvl2Group1");
						claimLineBean.setApproverLevelFourGroup("Lvl3Group1");
					}

					claimLineBean.setClaimFormTypeCode(claimForm.get(random.nextInt(claimForm.size())));
					claimLineBean.setModelScore(j);
					claimLineBean.setCptCode(cptCodeMap.get(k));
					claimLineBean.setBatchId(k);

					claimLineBean.setCptDesc(claimLineBean.getCptCode() + " DESC");
					claimLineBean.setRevenueDesc(claimLineBean.getRevenueCode() + " DESC");
					claimLineBean.setDiagnosis1Desc(claimLineBean.getDiagnosisCode1() + " DESC");
					claimLineBean.setCreatedBy(userId);
					claimLineBean.setConfidenceScore(95);
					claimLineBean.setPrioritizationScore(claimLineBean.getLineOpportunityAmount().multiply(new BigDecimal(Float.toString(claimLineBean.getConfidenceScore()))));
					BeanUtils.copyProperties(claimLineBean, entity);
					claimLineBeanList.add(claimLineBean);
				}
			}
		}
		return claimLineBeanList;
	}

	public static int createRandomIntBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}

	public static LocalDate createRandomDate(int startYear, int endYear) {
		int day = createRandomIntBetween(1, 28);
		int month = createRandomIntBetween(1, 12);
		int year = createRandomIntBetween(startYear, endYear);
		return LocalDate.of(year, month, day);
	}
}