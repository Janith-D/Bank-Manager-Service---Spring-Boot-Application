package com.example.bankManager.Service;

import com.example.bankManager.Dto.EmailDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceIm implements EmailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void sendEmailAlert(EmailDTO emailDTO) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(emailDTO.getRecipient());
            message.setText(emailDTO.getMessageBody());
            message.setSubject(emailDTO.getSubject());

            javaMailSender.send(message);
            System.out.println("Email send SUCCESSFUL!");
        }catch (MailException e){
            throw new RuntimeException(e);
        }
    }
}
