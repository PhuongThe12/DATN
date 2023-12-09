package luckystore.datn.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

@Service
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body, File attachment
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("qmaychem@gmail.com"); // Thay thế bằng địa chỉ email của bạn
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body);

        // Thêm tệp đính kèm
        if (attachment != null) {
            FileSystemResource file = new FileSystemResource(attachment);
            helper.addAttachment(file.getFilename(), file);
        }

        mailSender.send(message);
        System.out.println("Mail Send...");
    }

    public void sendEmailOrder(String toEmail, String subject, String body, File attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("qmaychem@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(body, true);

        // Thêm tệp đính kèm
        if (attachment != null) {
            FileSystemResource file = new FileSystemResource(attachment);
            helper.addAttachment(file.getFilename(), file);
        }

        mailSender.send(message);
        System.out.println("Mail Send...");
    }


    public void triggerMail() throws MessagingException {
        String toEmail = "quanchun11022@gmail.com";
        String subject = "This is email subject";
        String body = "This is email body";
        File attachmentFile = new File("file.xlsx");

        sendSimpleEmail(toEmail, subject, body, attachmentFile);
    }

    public static void main(String[] args) throws MessagingException {
        EmailSenderService emailSenderService = new EmailSenderService();
        emailSenderService.triggerMail();
    }


}
