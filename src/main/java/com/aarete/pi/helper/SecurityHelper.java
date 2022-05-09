package com.aarete.pi.helper;

import static com.aarete.pi.constant.WorkbenchConstants.PAYLOAD_NAME;
import static com.aarete.pi.constant.WorkbenchConstants.PAYLOAD_PREFERRED_USERNAME;
import static com.aarete.pi.constant.WorkbenchConstants.PAYLOAD_ROLES;

import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.exception.AccessDenied;
import com.aarete.pi.util.JsonParser;
import static com.aarete.pi.constant.WorkbenchConstants.OBJECT_ID;
import static com.aarete.pi.constant.WorkbenchConstants.USER_GROUPS;
import static com.aarete.pi.constant.WorkbenchConstants.TID;

public class SecurityHelper {
	
	private SecurityHelper() {

	}

	public static com.aarete.pi.bean.UserDetailsBean getUserDetailsBean() throws AccessDenied {
		UserDetailsBean userDetailsBean = null;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			if (Objects.nonNull(authentication) && Objects.nonNull(authentication.getDetails())) {
				final String tokenValue = ((OAuth2AuthenticationDetails) authentication.getDetails()).getTokenValue();
				String[] chunks = tokenValue.split("\\.");
				Base64.Decoder decoder = Base64.getDecoder();
				if (Objects.nonNull(chunks) && chunks.length > 1) {
					String payload = new String(decoder.decode(chunks[1]));
					LinkedHashMap<String, Object> payloadMap = (LinkedHashMap) JsonParser.convertToJson(payload);
					userDetailsBean = new UserDetailsBean();
					userDetailsBean.setName((String) payloadMap.get(PAYLOAD_NAME));
					userDetailsBean.setRoleList((List<String>) payloadMap.get(PAYLOAD_ROLES));
					userDetailsBean.setUserId((String) payloadMap.get(PAYLOAD_PREFERRED_USERNAME));
					userDetailsBean.setObjectId((String)payloadMap.get(OBJECT_ID));
					userDetailsBean.setGroups((List<String>)payloadMap.get(USER_GROUPS));
					userDetailsBean.setToken(tokenValue);
					userDetailsBean.setTid((String)payloadMap.get(TID));
				}
			} else {
				throw new AccessDenied("Token Not Found");
			}
		}

		return userDetailsBean;
	}

}
