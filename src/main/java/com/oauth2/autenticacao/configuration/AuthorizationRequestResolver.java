package com.oauth2.autenticacao.configuration;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationRequestResolver  implements OAuth2AuthorizationRequestResolver {


    private final OAuth2AuthorizationRequestResolver delegatedRequestResolver;

    public AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository, String authorizeUri ) {
        this.delegatedRequestResolver = new DefaultOAuth2AuthorizationRequestResolver( clientRegistrationRepository, authorizeUri );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest req = delegatedRequestResolver.resolve( request );
        return customizeRequest(req, request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest req = delegatedRequestResolver.resolve( request, clientRegistrationId );
        return customizeRequest(req, request);
    }

    private OAuth2AuthorizationRequest customizeRequest( OAuth2AuthorizationRequest request, HttpServletRequest httpRequest ) {
        if (request != null) {
            Map<String, Object> params = new HashMap<>(request.getAdditionalParameters());
            params.put(OAuth2ParameterNames.RESPONSE_TYPE, request.getResponseType().getValue() + " id_token token");
            return OAuth2AuthorizationRequest
                    .from(request)
                    .additionalParameters(params)
                    .build();
        }

        return null;
    }

}
