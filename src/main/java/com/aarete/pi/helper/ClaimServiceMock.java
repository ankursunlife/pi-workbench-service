package com.aarete.pi.helper;

import com.aarete.pi.bean.ClaimLineCount;
import com.aarete.pi.bean.ClaimSummary;
import com.aarete.pi.bean.IdNameBean;
import com.aarete.pi.bean.SummaryByRequest;
import com.aarete.pi.bean.SummaryResponse;

import java.util.LinkedList;
import java.util.List;

public class ClaimServiceMock {

    public static List<IdNameBean> getProviderName() {
        List<IdNameBean> idNameBeans = new LinkedList<>();

        IdNameBean idNameBean1 = new IdNameBean();
        idNameBean1.setId("C001");
        idNameBean1.setName("LabCorp");
        idNameBean1.setDesc("LabCorp");
        idNameBean1.setSelected(false);

        IdNameBean idNameBean2 = new IdNameBean();
        idNameBean2.setId("C002");
        idNameBean2.setName("Quest Diagnostics");
        idNameBean2.setDesc("Quest Diagnostics");
        idNameBean2.setSelected(false);

        IdNameBean idNameBean3 = new IdNameBean();
        idNameBean3.setId("C003");
        idNameBean3.setName("Tenet Health");
        idNameBean3.setDesc("Tenet Health");
        idNameBean3.setSelected(false);

        IdNameBean idNameBean4 = new IdNameBean();
        idNameBean4.setId("C004");
        idNameBean4.setName("Novant Health");
        idNameBean4.setDesc("Novant Health");
        idNameBean4.setSelected(false);

        idNameBeans.add(idNameBean1);
        idNameBeans.add(idNameBean2);
        idNameBeans.add(idNameBean3);
        idNameBeans.add(idNameBean4);

        return idNameBeans;
    }

    public static List<IdNameBean> getProviderNpi() {
        List<IdNameBean> idNameBeans = new LinkedList<>();
        IdNameBean idNameBean1 = new IdNameBean();
        idNameBean1.setId("7465367");
        idNameBean1.setName("7465367");
        idNameBean1.setDesc("7465367");
        idNameBean1.setSelected(false);

        IdNameBean idNameBean2 = new IdNameBean();
        idNameBean2.setId("7565367");
        idNameBean2.setName("7565367");
        idNameBean2.setDesc("7565367");
        idNameBean2.setSelected(false);

        IdNameBean idNameBean3 = new IdNameBean();
        idNameBean3.setId("7665367");
        idNameBean3.setName("7665367");
        idNameBean3.setDesc("7665367");
        idNameBean3.setSelected(false);

        IdNameBean idNameBean4 = new IdNameBean();
        idNameBean4.setId("7765367");
        idNameBean4.setName("7765367");
        idNameBean4.setDesc("7765367");
        idNameBean4.setSelected(false);

        idNameBeans.add(idNameBean1);
        idNameBeans.add(idNameBean2);
        idNameBeans.add(idNameBean3);
        idNameBeans.add(idNameBean4);

        return idNameBeans;
    }

    public static List<IdNameBean> getProviderIrs() {
        List<IdNameBean> idNameBeans = new LinkedList<>();
        IdNameBean idNameBean1 = new IdNameBean();
        idNameBean1.setId("6465367");
        idNameBean1.setName("6465367");
        idNameBean1.setDesc("6465367");
        idNameBean1.setSelected(false);

        IdNameBean idNameBean2 = new IdNameBean();
        idNameBean2.setId("6565367");
        idNameBean2.setName("6565367");
        idNameBean2.setDesc("6565367");
        idNameBean2.setSelected(false);

        IdNameBean idNameBean3 = new IdNameBean();
        idNameBean3.setId("6665367");
        idNameBean3.setName("6665367");
        idNameBean3.setDesc("6665367");
        idNameBean3.setSelected(false);

        IdNameBean idNameBean4 = new IdNameBean();
        idNameBean4.setId("6765367");
        idNameBean4.setName("6765367");
        idNameBean4.setDesc("6765367");
        idNameBean4.setSelected(false);

        idNameBeans.add(idNameBean1);
        idNameBeans.add(idNameBean2);
        idNameBeans.add(idNameBean3);
        idNameBeans.add(idNameBean4);

        return idNameBeans;
    }


    public static SummaryResponse summaryBy() {
        SummaryResponse summaryResponse = new SummaryResponse();

        ClaimLineCount claimLineCount = new ClaimLineCount();
        claimLineCount.setMyQueueCount(15);
        claimLineCount.setPendCount(10);
        claimLineCount.setWaitingCount(5);
        summaryResponse.setClaimLineCount(claimLineCount);

        ClaimSummary claimSummaryHeader = new ClaimSummary();
        claimSummaryHeader.setNameValue("All");
        claimSummaryHeader.setColumnOneValue("220");
        claimSummaryHeader.setColumnTwoValue("$0.40M");
        summaryResponse.setClaimSummaryHeader(claimSummaryHeader);

        List<ClaimSummary> countOfClaimSummary = new LinkedList<>();

        ClaimSummary claimSummary1 = new ClaimSummary();
        claimSummary1.setNameId("1");
        claimSummary1.setNameValue("CVS Health Corp.");
        claimSummary1.setColumnOneValue("10");
        claimSummary1.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary2 = new ClaimSummary();
        claimSummary2.setNameId("2");
        claimSummary2.setNameValue("United Health Group Inc.");
        claimSummary2.setColumnOneValue("20");
        claimSummary2.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary3 = new ClaimSummary();
        claimSummary3.setNameId("3");
        claimSummary3.setNameValue("Mount Sinai Hospital");
        claimSummary3.setColumnOneValue("14");
        claimSummary3.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary4 = new ClaimSummary();
        claimSummary4.setNameId("4");
        claimSummary4.setNameValue("AmerisourceBergen Corp.");
        claimSummary4.setColumnOneValue("14");
        claimSummary4.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary5 = new ClaimSummary();
        claimSummary5.setNameId("5");
        claimSummary5.setNameValue("Cigna Corp");
        claimSummary5.setColumnOneValue("14");
        claimSummary5.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary6 = new ClaimSummary();
        claimSummary6.setNameId("6");
        claimSummary6.setNameValue("Cardinal Health Inc. (CAH)");
        claimSummary6.setColumnOneValue("22");
        claimSummary6.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary7 = new ClaimSummary();
        claimSummary7.setNameId("7");
        claimSummary7.setNameValue("Walgreens Boots Alliance Inc.");
        claimSummary7.setColumnOneValue("18");
        claimSummary7.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary8 = new ClaimSummary();
        claimSummary8.setNameId("8");
        claimSummary8.setNameValue("Anthem Inc.");
        claimSummary8.setColumnOneValue("26");
        claimSummary8.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary9 = new ClaimSummary();
        claimSummary9.setNameId("9");
        claimSummary9.setNameValue("Johnson & Johnson");
        claimSummary9.setColumnOneValue("42");
        claimSummary9.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary10 = new ClaimSummary();
        claimSummary10.setNameId("10");
        claimSummary10.setNameValue("Centene Corp.");
        claimSummary10.setColumnOneValue("28");
        claimSummary10.setColumnTwoValue("$0.40M");

        ClaimSummary claimSummary11 = new ClaimSummary();
        claimSummary11.setNameId("11");
        claimSummary11.setNameValue("LabCorp");
        claimSummary11.setColumnOneValue("12");
        claimSummary11.setColumnTwoValue("$0.40M");

        countOfClaimSummary.add(claimSummary1);
        countOfClaimSummary.add(claimSummary2);
        countOfClaimSummary.add(claimSummary3);
        countOfClaimSummary.add(claimSummary4);
        countOfClaimSummary.add(claimSummary5);
        countOfClaimSummary.add(claimSummary6);
        countOfClaimSummary.add(claimSummary7);
        countOfClaimSummary.add(claimSummary8);
        countOfClaimSummary.add(claimSummary9);
        countOfClaimSummary.add(claimSummary10);
        countOfClaimSummary.add(claimSummary11);

        summaryResponse.setClaimSummaryList(countOfClaimSummary);

        summaryResponse.setSummaryByRequest(new SummaryByRequest());
        return summaryResponse;
    }

}
