package com.three_tech_solutions.slot_app.components.notifications;

import com.three_tech_solutions.slot_app.data.models.MonthlyFee;
import com.three_tech_solutions.slot_app.data.models.SpecificSlot;
import com.three_tech_solutions.slot_app.data.models.Student;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class NotificationContentBuilder {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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

    public static String buildMonthlyFeeExpirationMessage(Student student, MonthlyFee monthlyFee, String businessName){
        return """
                Hola %s 👋

                Desde %s queremos informarte que tu cuota mensual ya se encuentra vencida.

                💳 Monto: $%.2f
                📅 Fecha de vencimiento: %s
                
                Cuando tengas un momento, podés ponerte al día para seguir disfrutando de las clases sin interrupciones 😉

                ¡Te esperamos en clase! 🚲💪
                """.formatted(
                student.getName(),
                businessName,
                monthlyFee.getAmount(),
                monthlyFee.getExpirationDate().format(FORMATTER)
        );
    }

    public static String buildSlotRecoveryMessage(Student student, SpecificSlot specificSlot, String businessName){
        return """
        Hola %s 👋

        Desde %s queremos informarte que fuiste inscripto en un nuevo turno para recuperar una clase perdida.

        📅 Fecha del turno: %s
        🕒 Horario: %s a %s

        Te esperamos 💪🚲
        """.formatted(
                student.getName(),
                businessName,
                specificSlot.getSlotDate().format(FORMATTER),
                specificSlot.getStartTime().format(TIME_FORMATTER),
                specificSlot.getEndTime().format(TIME_FORMATTER)
        );
    }
    public static String buildSlotCanceledMessage(Student student, String businessName, LocalDate date, LocalTime startTime, boolean hasRecovery) {
        String recoveryMessage = hasRecovery
                ? "Se te ha acreditado una clase de recuperación ✅"
                : "Esta clase no podrá recuperarse.";

        return """
        Hola %s 👋

        Desde %s queríamos avisarte que la clase del día %s a las %s hs. fue cancelada.
        %s

        Ante cualquier duda, podés comunicarte con nosotros.

        ¡Nos vemos la próxima clase! 💪
        """.formatted(
                student.getName(),
                businessName,
                date.format(FORMATTER),
                startTime.format(TIME_FORMATTER),
                recoveryMessage
        );
    }

    public static String buildMonthlyFeeExpiringSoonMessage(Student student, MonthlyFee monthlyFee, String businessName){
        String expirationDate = monthlyFee
                .getExpirationDate()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        return """
            Hola %s 👋

            Desde %s queremos recordarte que tu cuota mensual todavía se encuentra pendiente y vence pronto📅

            ⏰ Tenés tiempo para abonarla hasta el %s.

            ¡Te esperamos en clase! 🚲💪
            """.formatted(
                student.getName(),
                businessName,
                expirationDate
        );
    }

}