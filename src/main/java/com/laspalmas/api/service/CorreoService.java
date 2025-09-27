package com.laspalmas.api.service;

public interface CorreoService {
void sendVerificationEmail(String correo, String verificationToken);

void sendForgotPasswordEmail(String correo, String resetToken);
}
