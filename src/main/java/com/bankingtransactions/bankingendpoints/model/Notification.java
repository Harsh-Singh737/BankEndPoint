package com.bankingtransactions.bankingendpoints.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String type; // INFO, ALERT, WARNING, etc.
    private String recipientEmail;
    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean read = false;
}
