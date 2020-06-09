package com.oauth2.autenticacao.service;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Service
public class AutenticacaoService {

    @Value("${spring.security.oauth2.client.provider.idsvr.issuer-uri}")
    private String issuerUri;

    public boolean isValidToken(String token) throws IOException {
        String userInfoUri = issuerUri + "/connect/userinfo";
        HttpPost postRequest = new HttpPost(userInfoUri);
        postRequest.addHeader("Authorization", token);

        try (CloseableHttpResponse response = HttpClients.createDefault().execute(postRequest)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                return false;
            }
        }

        return true;
    }

    public void signOut(String authorizationHeader, HttpServletResponse response) throws IOException {
        String userInfoUri = issuerUri + "/connect/endsession";
        HttpPost postRequest = new HttpPost(userInfoUri);
        postRequest.addHeader("Authorization", authorizationHeader);

        try (CloseableHttpResponse closeResponse = HttpClients.createDefault().execute(postRequest)) {
            int status = closeResponse.getStatusLine().getStatusCode();
            if (status == 302 || status == 200) {
                Arrays.asList(closeResponse.getAllHeaders()).forEach(
                        header -> response.addHeader(header.getName(), header.getValue())
                );
                response.setHeader("Access-Control-Allow-Origin", "*");
                response.setHeader("Location", userInfoUri);
                response.setStatus(200);
                response.sendRedirect(userInfoUri);
            } else {
                throw new RuntimeException("erro ao fazer signout");
            }
        }
    }
}
