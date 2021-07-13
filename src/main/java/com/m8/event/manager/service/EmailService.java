package com.m8.event.manager.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    public void enviarCorreo(String to, String subject, String text) throws MessagingException {

        new Thread(() -> {

            try {
                System.out.println("Enviando el correo a " + to);
                String[] para = {to}; //Consultarle a Fer
                MimeMessage message = sender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message); 
                helper.setFrom(from);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(text);
                sender.send(message);

            } catch (MessagingException e) {
                System.out.println("Error al enviar el correo a " + to);
            }
        
        }).start();
    }
    
}
