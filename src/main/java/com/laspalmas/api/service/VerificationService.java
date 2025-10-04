package com.laspalmas.api.service;

public interface VerificationService {
    String verificacionOTP(String correo,String otp);
    String verificacionOTPPassword(String correo,String otp,String newPassword);
}
