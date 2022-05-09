package com.aarete.pi.constant;

public class WorkbenchConstants {

	private WorkbenchConstants() {
	}

	public static final String PLAYLIST_STATUS_ACTIVE = "ACTIVE";
	public static final String PLAYLIST_STATUS_INACTIVE = "INACTIVE";
	public static final String PAYLOAD_NAME = "name";
	public static final String PAYLOAD_ROLES = "roles";
	public static final String PAYLOAD_PREFERRED_USERNAME = "preferred_username";
	public static final String OBJECT_ID = "oid";
	public static final String USER_GROUPS = "groups";
	public static final String TID = "tid";
	
	// ## AD Graph Token request
	public static final String TOKEN_USE = "requested_token_use";
	public static final String CLIENT_ID = "client_id";
	public static final String CLIENT_SECRET = "client_secret";
	public static final String GRANT_TYPE = "grant_type";
	public static final String SCP = "scope";
	public static final String U_TOKEN = "assertion";
	public static final String AUTHORIZATION = "Authorization";
	public static final String TOKEN_BEARER = "Bearer ";
	public static final String QUERY_LEVEL = "ConsistencyLevel";

	//## Logging related constants
	public static final String LOGGING_MDC_USER ="user";
	public static final String LOGGING_MDC_USER_ID ="user-id";
	public static final String LOGGING_MDC_FUNCTIONALITY_HEAD ="functionality-head";
	public static final String LOGGING_MDC_FUNCTIONALITY_SUB_HEAD ="functionality-subhead";
	
	public static final String DATE_FORMATTER = "MM.dd.yyyy hh:mm a";
	public static final String CST_ZONE = "CST";
	// AD filter/search for groups
	public static final String SELECT = "id,displayName";
	public static final String ORDER_BY = "displayName";
	
	public static final String PEND_BY_SELF = "Pended by Self";
	public static final String CLAIM_LINE_MSG_FORMAT = " - Line ";
	public static final String USER_ACTIVE = "ACTIVE";
	public static final String USER_DACTIVE = "INACTIVE";
	// AD default group name
	public static final String DEFAULT_GROUP = "AUGroup4";
	
}
