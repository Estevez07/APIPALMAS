package com.laspalmas.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import com.laspalmas.api.model.TokenUsuario;
import com.laspalmas.api.repository.TokenRepository;
import com.laspalmas.api.service.TokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
private final TokenRepository tokenRepository;
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void eliminarToken(TokenUsuario token) {
        tokenRepository.delete(token);
    }
    
}
