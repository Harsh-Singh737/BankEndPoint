package com.bankingtransactions.bankingendpoints.service;

import com.bankingtransactions.bankingendpoints.model.Notification;
import com.bankingtransactions.bankingendpoints.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(String message, String recipient, String type) {
        Notification n = new Notification();
        n.setMessage(message);
        n.setRecipientEmail(recipient);
        n.setType(type);
        return notificationRepository.save(n);
    }

    public List<Notification> getAllNotifications(String recipient) {
        return notificationRepository.findAll().stream()
                .filter(n -> n.getRecipientEmail().equals(recipient))
                .toList();
    }
}

