package com.example.project.project.cache;

import com.example.project.project.exception.TokenFoundException;
import lombok.extern.slf4j.Slf4j;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static com.example.project.project.security.SecurityParameter.TOKEN_EXPIRATION_TIME_CACHE;

@Slf4j
@Component
public class CacheToken {

    private final ExpiringMap<String, String> cacheToken = ExpiringMap.builder().variableExpiration().build();

    public void containToken(final String token) throws TokenFoundException {
        if(cacheToken.containsKey(token)){
            throw new TokenFoundException("The token has already been consumed. Please, log in again.");
        }
    }

    public void addTokenToBlackList(final String token, final String username){
        cacheToken.put(token, username, ExpirationPolicy.CREATED, TOKEN_EXPIRATION_TIME_CACHE, TimeUnit.MILLISECONDS);
    }

    public void cleanCache(){
        this.cacheToken.clear();
    }
}