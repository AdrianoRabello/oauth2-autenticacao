package com.oauth2.autenticacao.configuration;


import com.oauth2.autenticacao.exception.AutenticacaoException;
import com.oauth2.autenticacao.dto.ErroDto;
import com.oauth2.autenticacao.service.AutenticacaoService;
import net.minidev.json.JSONObject;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private MessageSource messageSource;
    private AutenticacaoService autenticacaoService;

    @Autowired
    public SecurityFilter(MessageSource messageSource, AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
        this.messageSource = messageSource;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String url = request.getRequestURI();
            String token = getToken(request);
            if (url.equals("/teste") || url.endsWith("/eeftDocumentos") || url.endsWith("/anexo")) {
                filterChain.doFilter(request, response);
            } else if (autenticacaoService.isValidToken(token)) {
                filterChain.doFilter(request, response);
            } else {
                throw new AutenticacaoException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            ErroDto erroDto = new ErroDto(messageSource.getMessage(e.getMessage(), null, LocaleContextHolder.getLocale()));
            JSONObject erroJON = new JSONObject((Map<String, ?>) erroDto);
            response.getWriter().write(erroJON.toString());
        }
    }

    private String getToken(HttpServletRequest request) throws AutenticacaoException {
        String token = request.getHeader("Authorization");
        String url = request.getRequestURI();
        if (token == null && url.endsWith("/signout")) {
            token = request.getParameter("token");
        }

        if (token == null) {
            throw new AutenticacaoException();
        }

        if (!token.startsWith("Bearer ")) {
            throw new AutenticacaoException();
        }

        return token;
    }
}