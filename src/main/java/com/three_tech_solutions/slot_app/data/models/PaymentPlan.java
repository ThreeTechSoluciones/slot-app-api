package com.three_tech_solutions.slot_app.data.models;

import com.three_tech_solutions.slot_app.data.enums.PaymentPlanName;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class PaymentPlan {
    Byte paymentDay;
    PaymentPlanName paymentPlanName;
    @ManyToOne
    Plan plan;
    @Id
    UUID id = UUID.randomUUID();

    public PaymentPlan(Byte paymentDay, PaymentPlanName paymentPlanName, Plan plan) {
        this.paymentDay = paymentDay;
        this.paymentPlanName = paymentPlanName;
        this.plan = plan;
    }
}
