package com.three_tech_solutions.slot_app.components.notifications;

import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NotificationContentBuilder {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static String buildRestorePasswordMessage(String username, String code) {
        return """
            Hola %s 👋

            Recibimos una solicitud para restablecer tu contraseña.

            🔐 Código de verificación:
            %s

            ⏱ Este código expira en 10 minutos.

            Si no solicitaste este cambio, podés ignorar este mensaje.
            """.formatted(username, code);
    }

    public static String buildNewMonthlyFeeMessage(Student student, MonthlyFee fee, String businessName) {
        return """
                Hola %s 👋

                Desde %s queremos recordarte la cuota correspondiente a este mes 🚲🗓️

                💳 Monto: $%.2f
                📅 Tenés tiempo para abonar hasta el %s

                Si ya realizaste el pago, podés ignorar este mensaje 😉

                ¡Te esperamos en clase! 💪
                """.formatted(
                student.getName(),
                businessName,
                fee.getAmount(),
                fee.getExpirationDate().format(FORMATTER)

        );
    }

}