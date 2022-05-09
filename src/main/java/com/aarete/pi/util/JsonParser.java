package com.aarete.pi.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.aarete.pi.bean.adgraph.AdUsersConfigResponse;
import com.aarete.pi.bean.adgraph.AdUsersMetadataList;
import com.aarete.pi.bean.adgraph.CustomSecurityAttributes;
import com.aarete.pi.bean.adgraph.GetAdUsersMetadataResponse;
import com.aarete.pi.bean.adgraph.GetUsersCustomeAttributeResponse;
import com.aarete.pi.bean.adgraph.Value;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;

/**
 * @author mpalla
 */
public class JsonParser {

	private static final Logger logger = LoggerFactory.getLogger(JsonParser.class);
	
    private JsonParser() {
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Gson gson = new Gson();

    /**
     * This method takes string, remove escape characters and returns JSON string
     */
    public static Object convertToJson(String value) {
        Object object;
        try {
            object = gson.fromJson(value, Object.class).getClass();
            if (object.equals(String.class)) {
                return value;
            } else if (object.equals(LinkedTreeMap.class)) {
                return readValue(value, Map.class );
            } else if (object.equals(ArrayList.class)) {
                return readValue(value, List.class);
            }
        } catch (JsonSyntaxException | JsonProcessingException e) {
            return  value;
        }
        return value;
    }

    /**
     * This method converts JSON string to specific Class format
     */
    public static <T> T readValue(final String json,final Class<T> classType) throws JsonProcessingException {
        return objectMapper.readValue(json, classType);
    }
    
    /**
     * @param str
     * @return
     * @throws JsonProcessingException
     */
    public static GetUsersCustomeAttributeResponse fetchCustomAttributes(final String str) throws JsonProcessingException {
    	
    	GetUsersCustomeAttributeResponse response = new GetUsersCustomeAttributeResponse();
    	JsonFactory factory = new JsonFactory();
	       ObjectMapper mapper = new ObjectMapper(factory);
	       JsonNode rootNode = mapper.readTree(str);  

	       Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.get("customSecurityAttributes").fields();
	       while (fieldsIterator.hasNext()) {
	           Map.Entry<String,JsonNode> field = fieldsIterator.next();
	           response.setClientName(field.getKey());
	           
    		   JsonNode engagementValue = field.getValue();
    		   Iterator<Map.Entry<String,JsonNode>> engegementItr = engagementValue.fields();
    		   int count = 0;
    		   while(engegementItr.hasNext()) {
    			   count++;
    			   Map.Entry<String,JsonNode> engegementfield = engegementItr.next();
    			   if(count == 3) {
    				   //EngagementId
    				   response.setEngagementId(engegementfield.getKey());
    				   JsonNode exCodeArray = engegementfield.getValue();
    				   List<String> list = new ArrayList<>();
    				   for (int i = 0; i < exCodeArray.size(); ++i) {
    				    list.add(exCodeArray.get(i).asText());
    				   }
    				   response.setExCodes(list); //ExCodes
    			   }   				   
    		   }
	       }
	       return response;
    }
    
    public static GetAdUsersMetadataResponse extractResponse(List<AdUsersConfigResponse> inputMetadata) {
    	GetAdUsersMetadataResponse outputMetadata = new GetAdUsersMetadataResponse();
    	List<Integer> dataCount = new ArrayList<>();
    	if(!CollectionUtils.isEmpty(inputMetadata)) {
    		List<AdUsersMetadataList> usersList = new ArrayList<>();
    		inputMetadata.stream().forEach(adUserConfigResponse -> {
    	    	if(null != adUserConfigResponse) {
    	    		dataCount.add(adUserConfigResponse.getOdataCount());
    	    		if(null != adUserConfigResponse.getValue() && !adUserConfigResponse.getValue().isEmpty()) {
    	    			List<Value> userValues = adUserConfigResponse.getValue();
    	    			userValues.forEach(userDetails -> {
    	    				AdUsersMetadataList data = new AdUsersMetadataList();
    	    				data.setId(userDetails.getId());
    	    				data.setDisplayName(userDetails.getDisplayName());
    	    				data.setMail(userDetails.getMail());
    	    				data.setAccountEnabled(userDetails.getAccountEnabled());
    	    				CustomSecurityAttributes customAttributes = userDetails.getCustomSecurityAttributes();
    	    				String response = null;
    	    				StringBuilder buildResponse = new StringBuilder("{");
    	    				try {
    	    					response = new ObjectMapper().writeValueAsString(customAttributes);
    	    					buildResponse.append("\"customSecurityAttributes\":")
    	    					.append(response)
    	    					.append("}");
    	    					data.setUsersCustomeAttributes(fetchCustomAttributes(buildResponse.toString()));
    						} catch (JsonProcessingException e) {
    							logger.error("Exception while extract customSecurityAttributes: {}", e.getMessage());
    						}
    	    				usersList.add(data);
    	    			});
    	    		}
    	    	}	
    		});
    		outputMetadata.setUserDataCount(dataCount.stream().mapToInt(Integer::intValue).sum());
    		outputMetadata.setAdUsersMetadataList(usersList);
    	}	
    	return outputMetadata;
    }
}
