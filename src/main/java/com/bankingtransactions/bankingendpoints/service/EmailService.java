package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.eventListener.AccountCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("harshsinghlo737@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleAccountCreatedEvent(AccountCreatedEvent event) {
        String subject = "Welcome to Harsh Bank!";
        String body = "Dear " + event.getFullName() + ",\n\n"
                + "Your new account has been created successfully.\n"
                + "Account Number: " + event.getAccountNumber() + "\n\n"
                + "Thank you for choosing Harsh Bank!";

        sendEmail(event.getEmail(), subject, body);
    }
}
