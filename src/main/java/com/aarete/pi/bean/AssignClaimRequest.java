/**
 * 
 */
package com.aarete.pi.bean;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author vjadhav
 *
 */
@Data
@NoArgsConstructor
public class AssignClaimRequest {
	List<ClaimProcessRequest> claimProcessList;
}
