package com.aarete.pi.helper;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.GetSecretValueRequest;
import com.amazonaws.services.secretsmanager.model.GetSecretValueResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class KMSHelper {
    static final String DB_SM_NAME = System.getenv("DB_SM_NAME");
    static final String AWS_SM_REGION = System.getenv("AWS_SM_REGION");

    @SuppressWarnings("unchecked")
    public static Map<Object, Object> getSecrets() {
        System.out.println("Connecting to AWS Secrets Manager ...");
        Map<Object, Object> secretsMap = new HashMap<Object, Object>();
        AWSSecretsManager client = AWSSecretsManagerClientBuilder.standard().withRegion(AWS_SM_REGION).build();
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest().withSecretId(DB_SM_NAME);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
            if (getSecretValueResult.getSecretString() != null) {
                String secret = getSecretValueResult.getSecretString();
                ObjectMapper mapper = new ObjectMapper();
                secretsMap = mapper.readValue(secret, Map.class);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return secretsMap;
    }

}
