package com.bid90.msemail.client;

import com.bid90.msemail.client.dto.Response;
import com.bid90.msemail.client.dto.ResponseContent;
import com.bid90.msemail.client.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.lang.reflect.ParameterizedType;

@Component
public class AuthClient {

    final String authServiceUrl;
    final RestClient restClient;

    public AuthClient(@Value("${auth.service.url}") String authServiceUrl,
                      RestClient restClient) {
        this.authServiceUrl = authServiceUrl;
        this.restClient = restClient;
    }


    public ResponseContent<UserDto> getCurrentUser(String authToken){
        return restClient.get()
                .uri(authServiceUrl+"/api/user/current")
                .header("Authorization", "Bearer "+ authToken)
                .header("accept", "*/*")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

}
