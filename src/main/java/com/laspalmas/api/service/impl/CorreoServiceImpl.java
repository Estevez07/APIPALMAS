package com.laspalmas.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import com.laspalmas.api.service.CorreoService;

import jakarta.mail.internet.MimeMessage;

@Service
public class CorreoServiceImpl implements CorreoService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    
    @Override
    public void sendOtpEmail(String correo, String verificationToken) {
        String subject = "Código de verificación";
        String message = "Usa el siguiente código para verificar tu cuenta:";
        sendEmail(correo, verificationToken, subject, message);
    }

    @Override
        public void sendForgotPasswordEmail(String email, String resetToken) {
        String subject = "Solicitud de restablecimiento de contraseña";
        String message = "Usa el siguiente código para restablecer su contraseña:";
        sendEmail(email, resetToken, subject, message);
    }



    
     private void sendEmail(String email, String token, String subject, String message) {
        try {
         
            String content = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                        <h2 style="color: #333;">%s</h2>
                        <p style="font-size: 16px; color: #555;">%s</p>
                        <h1 style="color: #007bff;">%s</h1>
                        <p style="font-size: 14px; color: #777;">Este código expira en 10 minutos.</p>
                        <p style="font-size: 12px; color: #aaa;">Este es un mensaje automático. Por favor, no responda.</p>
                    </div>
                """.formatted(subject, message,token);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom(from);
            helper.setText(content, true);
            mailSender.send(mimeMessage);

        } catch (Exception e) {
            System.err.println("Fallo al mandar el correo: " + e.getMessage());
        }
    }
}
