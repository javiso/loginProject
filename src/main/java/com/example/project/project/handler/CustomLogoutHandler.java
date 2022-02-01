package com.example.project.project.handler;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.project.project.cache.CacheToken;
import com.example.project.project.service.LogUrlService;
import com.example.project.project.util.TokenUtil;
import com.example.project.project.util.WriteUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.project.project.security.SecurityParameter.TOKEN_BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@AllArgsConstructor
@Component
public class CustomLogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private LogUrlService logUrlService;
    private final CacheToken cacheToken;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        logUrlService.registerUrl(request.getRequestURL().toString());
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_BEARER_PREFIX)) {
            try {
                String token = authorizationHeader.substring(TOKEN_BEARER_PREFIX.length());
                DecodedJWT decodedJWT = TokenUtil.getDecodedJWT(token);
                cacheToken.addTokenToBlackList(token, decodedJWT.getSubject());
            } catch(Exception e){
                WriteUtil.writeErrorResponse(response, e.getMessage(), HttpStatus.FORBIDDEN);
            }
        } else {
            WriteUtil.writeErrorResponse(response, "Please, input an existing token", HttpStatus.FORBIDDEN);
        }
    }
}