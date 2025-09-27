package com.laspalmas.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.laspalmas.api.service.CorreoService;

import jakarta.mail.internet.MimeMessage;

public class CorreoServiceImpl implements CorreoService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    
    @Override
    public void sendVerificationEmail(String correo, String verificationToken) {
        String subject = "Verificación de correo electrónico";
        String path = "/auth/register/verify";
        String message = "Haga clic en el botón a continuación para verificar su dirección de correo electrónico:";
        sendEmail(correo, verificationToken, subject, path, message);
    }

    @Override
        public void sendForgotPasswordEmail(String email, String resetToken) {
        String subject = "Solicitud de restablecimiento de contraseña";
        String path = "/auth/reset-password";
        String message = "Haga clic en el botón a continuación para restablecer su contraseña:";
        sendEmail(email, resetToken, subject, path, message);
    }



    
     private void sendEmail(String email, String token, String subject, String path, String message) {
        try {
            String actionUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(path)
                    .queryParam("token", token)
                    .toUriString();

            String content = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border-radius: 8px; background-color: #f9f9f9; text-align: center;">
                        <h2 style="color: #333;">%s</h2>
                        <p style="font-size: 16px; color: #555;">%s</p>
                        <a href="%s" style="display: inline-block; margin: 20px 0; padding: 10px 20px; font-size: 16px; color: #fff; background-color: #007bff; text-decoration: none; border-radius: 5px;">Proceder</a>
                        <p style="font-size: 14px; color: #777;">O copie y pegue este enlace en su navegador:</p>
                        <p style="font-size: 14px; color: #007bff;">%s</p>
                        <p style="font-size: 12px; color: #aaa;">Este es un mensaje automático. Por favor, no responda.</p>
                    </div>
                """.formatted(subject, message, actionUrl, actionUrl);

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
