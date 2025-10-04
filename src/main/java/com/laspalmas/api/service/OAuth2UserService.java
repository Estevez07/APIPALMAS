package com.laspalmas.api.service;


import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;



public interface OAuth2UserService {

 Map<String, String> login(String providerStr, OAuth2User oAuth2User); 
    
}
