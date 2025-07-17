package com.example.NotificationService.worker;

import com.example.Common.model.UserIdentifier;
import com.example.NotificationService.utility.EmailTemplate;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailWorker
{
    @Autowired
    JavaMailSender javaMailSender;

    public void sendEmailNotification(String name, String email, UserIdentifier userIdentifier, String userIdentifierValue) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            System.out.println("Inside the send email notification method");
            MimeMessageHelper mail = new MimeMessageHelper(mimeMessage, true);
            String emailContent = EmailTemplate.EMAIL_TEMPLATE;
            emailContent = emailContent.replaceAll("==name==", name);
            emailContent = emailContent.replaceAll("==document==", userIdentifier.name());
            emailContent = emailContent.replaceAll("==documentNo==", userIdentifierValue);
            mail.setText(emailContent, true);
            mail.setTo(email);
            mail.setFrom("m.ansari9044@gmail.com");
            mail.setSubject("Welcome to JBDL E Wallet Application");
            javaMailSender.send(mimeMessage);
            System.out.println("Email has been sent to User");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
