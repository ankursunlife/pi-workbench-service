package com.aarete.pi.dao.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.aarete.pi.bean.adgraph.GetUsersCustomeAttributeResponse;
import com.aarete.pi.constant.WorkbenchConstants;
import com.aarete.pi.enums.Role;
import com.aarete.pi.enums.UserGroupType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.aarete.pi.bean.UserDetailsBean;
import com.aarete.pi.bean.adgraph.AdUsersMetadataList;
import com.aarete.pi.dao.AzureADGraphDAO;
import com.aarete.pi.entity.QUserEntity;
import com.aarete.pi.entity.UserEntity;
import com.aarete.pi.exception.RecordNotFound;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.springframework.util.StringUtils;

import static com.aarete.pi.constant.WorkbenchConstants.USER_ACTIVE;
import static com.aarete.pi.constant.WorkbenchConstants.USER_DACTIVE;

@Component
public class AzureADGraphDAOImpl implements AzureADGraphDAO {
	private static final Logger LOGGER = LoggerFactory.getLogger(AzureADGraphDAOImpl.class);
	
    @PersistenceContext
    private EntityManager entityManager;

	@Override
	public void addUserDetails(List<AdUsersMetadataList> adUsersMetadataList, UserDetailsBean userContext) throws RecordNotFound {
		if(!CollectionUtils.isEmpty(adUsersMetadataList)) {
			final JPAQueryFactory jpaQuery = new JPAQueryFactory(entityManager);
			final QUserEntity userEntity = QUserEntity.userEntity;
			
			List<Tuple> uRows = jpaQuery.select(userEntity.userId, userEntity.userName).from(userEntity)
					.fetch(); // fetching existing user details
			
			ArrayList<String> dbUserList = new ArrayList<>();
			for(Tuple userDetails : uRows) {
				Object[] oData = userDetails.toArray();
				dbUserList.add((String)oData[0]);//UserId key
			}
			Collections.sort(dbUserList);
			adUsersMetadataList.stream().forEach(adUsersMetadata -> {
				String userType = null;
				try {
					userType = getUserType(adUsersMetadata);
					String aDUserEmail = adUsersMetadata.getMail();
					String aDUserName = adUsersMetadata.getDisplayName();
					boolean isActive = adUsersMetadata.getAccountEnabled().booleanValue();
					String userStatus = isActive ? USER_ACTIVE : USER_DACTIVE;
					if(!CollectionUtils.isEmpty(dbUserList) && dbUserList.contains(aDUserEmail)) {
						//updating user details
						BooleanBuilder builder = new BooleanBuilder();
						JPAUpdateClause update = jpaQuery.update(userEntity);
						builder.and(userEntity.userId.eq(aDUserEmail));
						update.set(userEntity.userName, aDUserName).set(userEntity.status, userStatus)
						.set(userEntity.updatedBy,userContext.getUserId())
						.set(userEntity.updatedTime, new Timestamp(System.currentTimeMillis()))
								.set(userEntity.userType, userType)
						.where(builder.getValue()).execute();
					}else {
						//Inserting user details
						UserEntity entity = new UserEntity();
						entity.setUserId(aDUserEmail);
						entity.setUserName(aDUserName);
						entity.setStatus(userStatus);
						entity.setCreatedBy(userContext.getUserId());
						entity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
						entity.setUserType(userType);
						entityManager.persist(entity);
						entityManager.flush();
					}
				}catch (Exception e) {
					LOGGER.error(String.format("Exception occurred: %s", e.getMessage()));
					try {
						throw e;
					} catch (RecordNotFound ex) {
						LOGGER.error(String.format("Exception occurred: %s", ex.getMessage()));
					}
				}
			});
			LOGGER.info("AD User Details inserted/updated successfully");
		}else {
			throw new RecordNotFound("AD user-list not found");
		}
	}

	private String getUserType(AdUsersMetadataList adUsersMetadataList) throws RecordNotFound {
		String userGroupType = null;
		GetUsersCustomeAttributeResponse customAttributesResponse = adUsersMetadataList.getUsersCustomeAttributes();
		if(null != customAttributesResponse && !CollectionUtils.isEmpty(customAttributesResponse.getExCodes())) {
			for(String matrixConfig : customAttributesResponse.getExCodes()){
				if(matrixConfig.contains("Group-")) {
					String[] groupName = matrixConfig.split("Group-");
					userGroupType = (groupName.length > 0 && !groupName[1].isBlank()) ? groupName[1] : WorkbenchConstants.DEFAULT_GROUP;
				}
			}
		}else {
			throw new RecordNotFound("AD custom attributes or assigned roles not configured");
		}
		return getGroupType(userGroupType);
	}

	private String getGroupType(String userType) {
		// e.g: AUGroup1
		UserGroupType groupType = UserGroupType.getInstanceByName(userType.split("Group")[0]);
		switch (groupType) {
			case AU:
				return Role.AARETE_USER.name();
			case LVL1:
				return Role.AARETE_MANAGER.name();
			case LVL2:
				return Role.CLIENT_USER.name();
			case LVL3:
				return Role.CLIENT_MANAGER.name();
			default:
				break;
		}
		return null;
	}
}
