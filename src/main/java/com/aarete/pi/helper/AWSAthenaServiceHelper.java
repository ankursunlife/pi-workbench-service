package com.aarete.pi.helper;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aarete.pi.bean.ClaimBean;
import com.aarete.pi.bean.ClaimDetailsRequest;
import com.aarete.pi.bean.ClaimDetailsResponse;
import com.aarete.pi.bean.ClaimLineInfo;
import com.aarete.pi.bean.ClaimsListRequest;
import com.aarete.pi.bean.ClaimsListResponse;
import com.aarete.pi.constant.DataLakeConstants;

import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.AthenaClientBuilder;
import software.amazon.awssdk.services.athena.model.ColumnInfo;
import software.amazon.awssdk.services.athena.model.Datum;
import software.amazon.awssdk.services.athena.model.GetQueryExecutionRequest;
import software.amazon.awssdk.services.athena.model.GetQueryExecutionResponse;
import software.amazon.awssdk.services.athena.model.GetQueryResultsRequest;
import software.amazon.awssdk.services.athena.model.GetQueryResultsResponse;
import software.amazon.awssdk.services.athena.model.QueryExecutionContext;
import software.amazon.awssdk.services.athena.model.QueryExecutionState;
import software.amazon.awssdk.services.athena.model.ResultConfiguration;
import software.amazon.awssdk.services.athena.model.Row;
import software.amazon.awssdk.services.athena.model.StartQueryExecutionRequest;
import software.amazon.awssdk.services.athena.model.StartQueryExecutionResponse;
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable;
import software.amazon.awssdk.utils.CollectionUtils;
import software.amazon.awssdk.utils.StringUtils;

@Component
public class AWSAthenaServiceHelper {
	@Autowired
	AthenaClientBuilder athenaClientBuilder;

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * getClaimDetails for a given query
	 * 
	 * @param claimDetailsRequest
	 * @return claimDetailsResponse
	 */
	public ClaimDetailsResponse getClaimDetails(ClaimDetailsRequest claimDetailsRequest) throws InterruptedException {
		String query = getClaimDetailsQuery(DataLakeConstants.ATHENA_CLAIM_DETAILS_BY_CLAIMNUM_QUERY, claimDetailsRequest);
		log.info("Query: {}", query);
		String queryExecutionId = claimDetailsRequest.getQueryExecutionId();
		String nextToken = claimDetailsRequest.getNextToken();
		ClaimDetailsResponse claimDetailsResponse = new ClaimDetailsResponse();
		try {
			AthenaClient athenaClient = athenaClientBuilder.build();
			if (StringUtils.isBlank(queryExecutionId) || StringUtils.isBlank(nextToken)) {
				queryExecutionId = submitAthenaQuery(athenaClient, query);
				waitForQueryToComplete(athenaClient, queryExecutionId);
				claimDetailsResponse = processClaimDetails(athenaClient, queryExecutionId);
			} else {
				claimDetailsResponse = processClaimDetails(athenaClient, queryExecutionId, nextToken);
			}
		} catch (Exception e) {
			log.error("Exception: {}", e);
			throw e;
		}
		return claimDetailsResponse;
	}

	private String getClaimDetailsQuery(String query, ClaimDetailsRequest claimDetailsRequest) {
		return query.replace(DataLakeConstants.PARAM_ENGAGEMENT_ID, claimDetailsRequest.getEngagementId())
				.replace(DataLakeConstants.PARAM_CLAIM_NUM, String.valueOf(claimDetailsRequest.getClaimNum()));
	}

	/**
	 * getClaimsList for a given query
	 * 
	 * @param claimDetailsRequest
	 * @return claimsListResponse
	 */
	public ClaimsListResponse getClaimsList(ClaimsListRequest claimsListRequest) throws InterruptedException {
		String query = getClaimsListQuery(DataLakeConstants.ATHENA_CLAIMS_LIST_BY_CRITERIA, claimsListRequest);
		log.info("Query: {}", query);
		String queryExecutionId = claimsListRequest.getQueryExecutionId();
		String nextToken = claimsListRequest.getNextToken();
		ClaimsListResponse claimsListResponse = new ClaimsListResponse();
		try {
			AthenaClient athenaClient = athenaClientBuilder.build();
			if (StringUtils.isBlank(queryExecutionId) || StringUtils.isBlank(nextToken)) {
				queryExecutionId = submitAthenaQuery(athenaClient, query);
				waitForQueryToComplete(athenaClient, queryExecutionId);
				claimsListResponse = processClaimsList(athenaClient, queryExecutionId);
			} else {
				claimsListResponse = processClaimsList(athenaClient, queryExecutionId, nextToken);
			}
		} catch (Exception e) {
			log.error("Exception: {}", e);
			throw e;
		}
		return claimsListResponse;
	}

	private String getClaimsListQuery(String query, ClaimsListRequest claimsListRequest) {
		StringBuilder newQuery = new StringBuilder();
		newQuery.append(query.replace(DataLakeConstants.PARAM_ENGAGEMENT_ID, claimsListRequest.getEngagementId()));
		if (StringUtils.isNotBlank(claimsListRequest.getMemberId())) {
			newQuery.append(" AND c.member_id = '").append(claimsListRequest.getMemberId()).append("'");
		}
		if (StringUtils.isNotBlank(claimsListRequest.getProviderNumber())) {
			newQuery.append(" AND c.provider_id = '").append(claimsListRequest.getProviderNumber()).append("'");
		}
		if (StringUtils.isNotBlank(claimsListRequest.getPillarId())) {
			newQuery.append(" AND c.piller_id = '").append(claimsListRequest.getPillarId()).append("'");
		}
		if (StringUtils.isNotBlank(claimsListRequest.getLobId())) {
			newQuery.append(" AND c.product_line_of_business = '").append(claimsListRequest.getLobId()).append("'");
		}
		if (StringUtils.isNotBlank(claimsListRequest.getEditType())) {
			newQuery.append(" AND c.eidt_type = '").append(claimsListRequest.getEditType()).append("'");
		}
		if (StringUtils.isNotBlank(claimsListRequest.getType())) {
			newQuery.append(" AND c.type = '").append(claimsListRequest.getType()).append("'");
		}
		if (claimsListRequest.getTimePeriod() != 0) {
			newQuery.append(" AND q.line_from_dt > current_date - interval '").append(claimsListRequest.getTimePeriod()).append("' day;");
		}
		return newQuery.toString();
	}

	/**
	 * Submits a sample query to Athena and returns the execution ID of the query.
	 */
	private String submitAthenaQuery(AthenaClient athenaClient, String query) {
		// The QueryExecutionContext allows us to set the Database.
		QueryExecutionContext queryExecutionContext = QueryExecutionContext.builder()
				.database(DataLakeConstants.ATHENA_PI_WORKBENCH_CONSOLIDATED_DB).build();

		// The result configuration specifies where the results of the query should go
		// in S3 and encryption options
		ResultConfiguration resultConfiguration = ResultConfiguration.builder()
				.outputLocation(DataLakeConstants.ATHENA_PI_WORKBENCH_OUTPUT_BUCKET).build();

		// Create the StartQueryExecutionRequest to send to Athena which will start the
		// query.
		StartQueryExecutionRequest startQueryExecutionRequest = StartQueryExecutionRequest.builder()
				.queryString(query)
				.queryExecutionContext(queryExecutionContext).resultConfiguration(resultConfiguration).build();

		StartQueryExecutionResponse startQueryExecutionResponse = athenaClient
				.startQueryExecution(startQueryExecutionRequest);
		return startQueryExecutionResponse.queryExecutionId();
	}

	/**
	 * Wait for an Athena query to complete, fail or to be cancelled. This is done
	 * by polling Athena over an interval of time. If a query fails or is cancelled,
	 * then it will throw an exception.
	 */

	private void waitForQueryToComplete(AthenaClient athenaClient, String queryExecutionId)
			throws InterruptedException {
		GetQueryExecutionRequest getQueryExecutionRequest = GetQueryExecutionRequest.builder()
				.queryExecutionId(queryExecutionId).build();

		GetQueryExecutionResponse getQueryExecutionResponse = null;
		boolean isQueryStillRunning = true;
		while (isQueryStillRunning) {
			getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest);
			String queryState = getQueryExecutionResponse.queryExecution().status().state().toString();
			if (queryState.equals(QueryExecutionState.FAILED.toString())) {
				throw new RuntimeException("The Amazon Athena query failed to run with error message: "
						+ getQueryExecutionResponse.queryExecution().status().stateChangeReason());
			} else if (queryState.equals(QueryExecutionState.CANCELLED.toString())) {
				throw new RuntimeException("The Amazon Athena query was cancelled.");
			} else if (queryState.equals(QueryExecutionState.SUCCEEDED.toString())) {
				isQueryStillRunning = false;
			} else {
				// Sleep an amount of time before retrying again.
				Thread.sleep(DataLakeConstants.ATHENA_SLEEP_AMOUNT_IN_MS);
			}
			log.info("Current Status is: {}", queryState);
		}
	}

	/**
	 * This code calls Athena and retrieves the results of a query. The query must
	 * be in a completed state before the results can be retrieved and paginated.
	 * The first row of results are the column headers.
	 */
	private ClaimDetailsResponse processClaimDetails(AthenaClient athenaClient, String queryExecutionId) {
		GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
				.maxResults(100).queryExecutionId(queryExecutionId).build();

		GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);

		List<ClaimLineInfo> list = new ArrayList<ClaimLineInfo>();
		log.info("Processing results");
		for (GetQueryResultsResponse result : getQueryResultsResults) {
			String nextToken = result.nextToken();
			log.info("Next token returned: {}", nextToken);
			List<ColumnInfo> columnInfoList = result.resultSet().resultSetMetadata().columnInfo();
			List<Row> results = result.resultSet().rows();
			log.info("No of rows: {}", results.size());
			list = mapClaimDetails(results, columnInfoList);
			ClaimDetailsResponse claimDetailsResponse = updateClaimDetails(list, queryExecutionId, nextToken);
			return claimDetailsResponse;
		}
		return null;
	}

	private ClaimDetailsResponse processClaimDetails(AthenaClient athenaClient, String queryExecutionId,
			String nextToken) {
		log.info("Next token request: {}", nextToken);
		GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder().nextToken(nextToken)
				.maxResults(100).queryExecutionId(queryExecutionId).build();

		GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);

		List<ClaimLineInfo> list = new ArrayList<ClaimLineInfo>();
		log.info("Processing results");
		for (GetQueryResultsResponse result : getQueryResultsResults) {
			nextToken = result.nextToken();
			log.info("Next token returned: {}", nextToken);
			List<ColumnInfo> columnInfoList = result.resultSet().resultSetMetadata().columnInfo();
			List<Row> results = result.resultSet().rows();
			log.info("No of rows: {}", results.size());
			// Return first page only
			list = mapClaimDetails(results, columnInfoList);
			ClaimDetailsResponse claimDetailsResponse = updateClaimDetails(list, queryExecutionId, nextToken);
			return claimDetailsResponse;
		}

		return null;
	}

	private ClaimDetailsResponse updateClaimDetails(List<ClaimLineInfo> list, String queryExecutionId,
			String nextToken) {
		ClaimDetailsResponse claimDetailsResponse = new ClaimDetailsResponse();
		if (!CollectionUtils.isNullOrEmpty(list)) {
			claimDetailsResponse.setClaimLines(list);
			claimDetailsResponse.setQueryExecutionId(queryExecutionId);
			claimDetailsResponse.setNextToken(nextToken);
		}
		return claimDetailsResponse;
	}

	private ClaimsListResponse processClaimsList(AthenaClient athenaClient, String queryExecutionId, String nextToken) {
		log.info("Next token request: {}", nextToken);
		GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder().nextToken(nextToken)
				.maxResults(100).queryExecutionId(queryExecutionId).build();
		GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);

		List<ClaimBean> list = new ArrayList<ClaimBean>();
		log.info("Processing results");
		for (GetQueryResultsResponse result : getQueryResultsResults) {
			nextToken = result.nextToken();
			log.info("Next token returned: {}", nextToken);
			List<ColumnInfo> columnInfoList = result.resultSet().resultSetMetadata().columnInfo();
			List<Row> results = result.resultSet().rows();
			log.info("No of rows: {}", results.size());
			list = mapClaimsList(results, columnInfoList);
			ClaimsListResponse claimsListResponse = updateClaimsList(list, queryExecutionId, nextToken);
			return claimsListResponse;
		}
		return null;
	}

	private ClaimsListResponse processClaimsList(AthenaClient athenaClient, String queryExecutionId) {
		GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
				.maxResults(100).queryExecutionId(queryExecutionId).build();

		GetQueryResultsIterable getQueryResultsResults = athenaClient.getQueryResultsPaginator(getQueryResultsRequest);

		List<ClaimBean> list = new ArrayList<ClaimBean>();
		log.info("Processing results");
		for (GetQueryResultsResponse result : getQueryResultsResults) {
			String nextToken = result.nextToken();
			log.info("Next token returned: {}", nextToken);
			List<ColumnInfo> columnInfoList = result.resultSet().resultSetMetadata().columnInfo();
			List<Row> results = result.resultSet().rows();
			log.info("No of rows: {}", results.size());
			list = mapClaimsList(results, columnInfoList);
			ClaimsListResponse claimsListResponse = updateClaimsList(list, queryExecutionId, nextToken);
			return claimsListResponse;
		}
		return null;
	}

	private ClaimsListResponse updateClaimsList(List<ClaimBean> list, String queryExecutionId, String nextToken) {
		ClaimsListResponse claimsListResponse = new ClaimsListResponse();
		if (!CollectionUtils.isNullOrEmpty(list)) {
			claimsListResponse.setClaims(list);
			claimsListResponse.setQueryExecutionId(queryExecutionId);
			claimsListResponse.setNextToken(nextToken);
		}
		return claimsListResponse;
	}

	private List<ClaimLineInfo> mapClaimDetails(List<Row> results, List<ColumnInfo> columnInfoList) {
		List<ClaimLineInfo> list = new ArrayList<ClaimLineInfo>();
		for (Row record : results) {
			ClaimLineInfo line = new ClaimLineInfo();
			List<Datum> allData = record.data();
			int index = 0;
			for (Datum data : allData) {
				// System.out.println("The value of the column is " + data.varCharValue());
				// Ignore header line
				if ("claim_num".equalsIgnoreCase(data.varCharValue())) {
					line = null;
					break;
				}
				try {
					switch (columnInfoList.get(index).name()) {
					case "claim_num":
						line.setClaimNum(Long.valueOf(data.varCharValue()));
						break;
					case "claim_line_num":
						line.setClaimLineNum(Integer.valueOf(data.varCharValue()));
						break;
					case "aar_claim_line_seq":
						line.setSequence(Integer.valueOf(data.varCharValue()));
						break;
					case "line_from_dt":
						java.util.Date date;
						try {
							date = new SimpleDateFormat("yyyy-MM-dd").parse(data.varCharValue());
							line.setLineFromDate(new java.sql.Date(date.getTime()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case "place_of_svc_cd":
						line.setPlaceOfService(data.varCharValue());
						break;
					case "revenue_cd":
						line.setRevenueCode(data.varCharValue());
						break;
					case "cpt4_proc_mod_1":
						line.setModifier(data.varCharValue());
						break;
					case "cpt4_proc_cd":
						line.setCptCode(data.varCharValue());
						break;
					case "line_unit_cnt":
						line.setUnits(
								Integer.valueOf(StringUtils.isBlank(data.varCharValue()) ? "0" : data.varCharValue()));
						break;
					case "line_billed_amt":
						line.setLineBilledAmount(new BigDecimal(data.varCharValue()));
						break;
					case "line_allowed_amt":
						line.setLineAllowedAmount(new BigDecimal(data.varCharValue()));
						break;
					case "line_paid_amt":
						line.setLinePaidAmount(new BigDecimal(data.varCharValue()));
						break;
					case "ex_code":
						line.setExCode(data.varCharValue());
						break;
					}
				} catch (Exception e) {
				}
				index++;
			}
			if (null != line) {
				list.add(line);
			}
		}

		return list;
	}
	
	private List<ClaimBean> mapClaimsList(List<Row> results, List<ColumnInfo> columnInfoList) {
		List<ClaimBean> list = new ArrayList<ClaimBean>();
		for (Row record : results) {
			ClaimBean line = new ClaimBean();
			List<Datum> allData = record.data();
			int index = 0;
			for (Datum data : allData) {
				// System.out.println("The value of the column is " + data.varCharValue());
				// Ignore header line
				if ("claim_num".equalsIgnoreCase(data.varCharValue())) {
					line = null;
					break;
				}
				try {
					switch (columnInfoList.get(index).name()) {
					case "claim_num":
						line.setClaimNum(Long.valueOf(data.varCharValue()));
						break;
					case "claim_from_dt":
						try {
							java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data.varCharValue());
							line.setClaimFromDate(new java.sql.Date(date.getTime()));
						} catch (ParseException e) {
							log.error("Exception: {}", e);
						}
						break;
					case "line_thru_dt":
						try {
							java.util.Date date = new SimpleDateFormat("yyyy-MM-dd").parse(data.varCharValue());
							line.setClaimThruDate(new java.sql.Date(date.getTime()));
						} catch (ParseException e) {
							log.error("Exception: {}", e);
						}
						break;
					case "claim_form_type_cd":
						line.setClaimFormTypeCode(data.varCharValue());
						break;
					case "bill_type_cd":
						line.setBillTypeCode(data.varCharValue());
						break;
					case "line_unit_cnt":
						line.setUnits(
								Integer.valueOf(StringUtils.isBlank(data.varCharValue()) ? "0" : data.varCharValue()));
						break;
					case "aar_claim_billed":
						line.setAarClaimBilled(new BigDecimal(data.varCharValue()));
						break;
					case "aar_claim_allowed":
						line.setAarClaimAllowed(new BigDecimal(data.varCharValue()));
						break;
					case "aar_claim_paid":
						line.setAarClaimPaid(new BigDecimal(data.varCharValue()));
						break;
					case "ex_code":
						line.setExCode(data.varCharValue());
						break;
					}
				} catch (Exception e) {
				}
				index++;
			}
			if (null != line) {
				list.add(line);
			}
		}

		return list;
	}


}
