package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Notification {
        LocalDate sendDate;
        @Column(length = 1000)
        String message;
        @Enumerated(EnumType.STRING)
        @Column(length = 50)
        NotificationType type;
        @ManyToOne
        User user;
        @Id
        UUID id;
}
