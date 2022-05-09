package com.aarete.pi.constant;

public class DataLakeConstants {
	public static final int ATHENA_CLIENT_EXECUTION_TIMEOUT = 100000;
	// get s3 bucket name from Dynamo DB and store it in environment variable
	// public static final String ATHENA_PI_WORKBENCH_OUTPUT_BUCKET = System.getenv("ATHENA_PI_WORKBENCH_OUTPUT_BUCKET");
    public static final String ATHENA_PI_WORKBENCH_OUTPUT_BUCKET = "s3://pi-claim-workflow-lambda/athena-output/";
    public static final String ATHENA_CLAIM_DETAILS_BY_CLAIMNUM_QUERY = "SELECT claim_num, claim_line_num, aar_claim_line_seq, line_from_dt, place_of_svc_cd, revenue_cd, cpt4_proc_mod_1, cpt4_proc_cd, line_unit_cnt, line_billed_amt, line_allowed_amt, line_paid_amt  FROM claim_line where engagement = '{ENGAGEMENT_ID}' and claim_num = '{CLAIM_NUM}';";
    public static final String ATHENA_CLAIMS_LIST_BY_CRITERIA = "SELECT a.claim_num, a.line_from_dt, a.line_thru_dt, b.claim_form_type_cd, "
    						+ "        b.bill_type_cd, c.line_unit_cnt, b.claim_billed_amt, b.claim_paid_amt FROM claim_aarete a "
    						+ "    JOIN claim_header b ON a.aar_claim_num = b.aar_claim_num "
    						+ "    JOIN claim_line c ON b.claim_num = c.claim_num "
    						+ "    WHERE c.engagement = '{ENGAGEMENT_ID}' "; //a.line_from_dt > current_date - interval '142' day;";
    public static final long ATHENA_SLEEP_AMOUNT_IN_MS = 1000;
    // get DB name from Dynamo DB and store it in environment variable
    //public static final String ATHENA_PI_WORKBENCH_CONSOLIDATED_DB = System.getenv("ATHENA_PI_WORKBENCH_CONSOLIDATED_DB");
    public static final String ATHENA_PI_WORKBENCH_CONSOLIDATED_DB = "moda_curated";
    public static final String PARAM_ENGAGEMENT_ID = "{ENGAGEMENT_ID}";
    public static final String PARAM_CLAIM_NUM = "{CLAIM_NUM}";
}
