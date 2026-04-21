package com.three_tech_solutions.slot_app.utils;

public class EmailUtils {

    private static final int VISIBLE_CHARACTERS = 3;

    private EmailUtils() {

    }

    public static String ofuscateEmail(String email) {
        int atIndex = email.indexOf("@");
        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        return email.substring(0, VISIBLE_CHARACTERS).concat("*".repeat(localPart.length() - VISIBLE_CHARACTERS)).concat(domain);
    }

    public static String RESTORE_PASSWORD_SUBJECT = "Restablecimiento de contraseña";
    public static String getRestorePasswordEmailContent(String username, String code) {
        return String.format("""
                
                <!DOCTYPE html>
                <html lang='es'>
                <head>
                <meta charset='UTF-8'>
                <title>Recuperación de contraseña</title>
                </head>
                
                <body style='margin:0;padding:0;background-color:#f4f6f8;font-family:Arial,Helvetica,sans-serif;'>
                
                <table width='100%%' cellpadding='0' cellspacing='0' border='0' style='background-color:#f4f6f8;padding:20px;'>
                <tr>
                <td align='center'>
                
                <table width='520' cellpadding='0' cellspacing='0' border='0'
                style='background:#ffffff;border-radius:8px;padding:40px;max-width:520px;box-shadow:0 2px 8px rgba(0,0,0,0.08);'>
                
                <tr>
                <td align='center'>
                
                <h1 style='margin:0 0 20px 0;font-size:24px;color:#2c3e50;font-weight:600;'>
                Restablecer contraseña
                </h1>
                
                <p style='margin:0 0 20px 0;color:#555;font-size:16px;line-height:1.5;'>
                Hola <strong>%s</strong>, recibimos una solicitud para restablecer la contraseña de tu cuenta.
                </p>
                
                <p style='margin:0 0 10px 0;color:#555;font-size:16px;'>
                Ingresá el siguiente código en la aplicación:
                </p>
                
                </td>
                </tr>
                
                <tr>
                <td align='center'>
                
                <div style='
                font-size:34px;
                font-weight:bold;
                letter-spacing:10px;
                color:#1f2937;
                background-color:#f3f4f6;
                padding:18px 30px;
                border-radius:6px;
                display:inline-block;
                margin:20px 0;
                '>
                %s
                </div>
                
                </td>
                </tr>
                
                <tr>
                <td align='center'>
                
                <p style='margin:20px 0 0 0;color:#666;font-size:14px;'>
                Este código expirará en <strong>10 minutos</strong>.
                </p>
                
                <p style='margin:10px 0 0 0;color:#888;font-size:14px;'>
                Si no solicitaste este cambio, podés ignorar este correo.
                </p>
                
                </td>
                </tr>
                
                <tr>
                <td align='center' style='padding-top:35px;border-top:1px solid #eee;'>
                
                <p style='margin:0;color:#999;font-size:12px;'>
                © 2026 SlotApp
                </p>
                
                </td>
                </tr>
                
                </table>
                
                </td>
                </tr>
                </table>
                
                </body>
                </html>
                
                """, username, code);
    }
}
