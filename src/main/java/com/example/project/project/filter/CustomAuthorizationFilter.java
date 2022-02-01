package com.example.project.project.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.project.project.cache.CacheToken;
import com.example.project.project.util.TokenUtil;
import com.example.project.project.util.WriteUtil;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.example.project.project.security.SecurityParameter.LOGIN_URL;
import static com.example.project.project.security.SecurityParameter.TOKEN_BEARER_PREFIX;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@NoArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private CacheToken cacheToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals(LOGIN_URL) ) {
            filterChain.doFilter(request, response);
        } else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_BEARER_PREFIX)) {
                try {
                    String token = authorizationHeader.substring(TOKEN_BEARER_PREFIX.length());
                    DecodedJWT decodedJWT = TokenUtil.getDecodedJWT(token);
                    String username = decodedJWT.getSubject();
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    cacheToken.containToken(token);
                    filterChain.doFilter(request, response);
                } catch (Exception e){
                    WriteUtil.writeErrorResponse(response, e.getMessage(), HttpStatus.FORBIDDEN);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}