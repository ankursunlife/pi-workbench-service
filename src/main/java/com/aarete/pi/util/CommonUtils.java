/**
 * 
 */
package com.aarete.pi.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

/**
 * @author vjadhav
 *
 */
public class CommonUtils {
	private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
	
	private CommonUtils(){}
	
	public static String convertToZoneDateTime(String inputDate, String zoneId, String dFormate){
		try {
			String formattedDate = inputDate.split("\\+")[0];
			Timestamp ts= Timestamp.valueOf(LocalDateTime.parse(formattedDate.replace( " " , "T"))); 
			Date date = new Date(ts.getTime());

			// Converting local to CST Zone mm.dd.yyyy hh:mm 12 hour format 
			SimpleDateFormat simpleDateFormate = new SimpleDateFormat(dFormate);
			TimeZone tZone = TimeZone.getTimeZone(zoneId); 
			simpleDateFormate.setTimeZone(tZone);
			return simpleDateFormate.format(date);
		}catch (Exception e) {
			logger.error("Exception occured while converting Zone Date/Time", e);
		}
		return null;
	}
	
	public static String convertToZoneDateTime(Timestamp inputDate, String zoneId, String dFormate){
		try {
			Date lDate = new Date(inputDate.getTime());
			// Converting local to CST Zone mm.dd.yyyy hh:mm 12 hour format
			
	        SimpleDateFormat simpleDateFormate = new SimpleDateFormat(dFormate);
	        simpleDateFormate.setTimeZone(TimeZone.getTimeZone(zoneId));
			return simpleDateFormate.format(lDate);
		}catch (Exception e) {
			logger.error("Exception occured while converting Zone Date/Time", e);
		}
		return null;
	}
	
	public static Consumer<HttpHeaders> httpHeaders(Map<String, String> map){		
        return headers -> {
        	for(Map.Entry<String, String> entry : map.entrySet()) {
        		headers.add(entry.getKey(), entry.getValue());
    		}
        };
    }
}
