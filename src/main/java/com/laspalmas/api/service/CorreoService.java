package com.laspalmas.api.service;

public interface CorreoService {
void sendOtpEmail(String correo, String verificationToken);

void sendForgotPasswordEmail(String correo, String resetToken);
}
