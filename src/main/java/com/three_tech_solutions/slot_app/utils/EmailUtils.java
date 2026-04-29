package com.three_tech_solutions.slot_app.utils;

public class EmailUtils {

    private static final int VISIBLE_CHARACTERS = 3;

    private EmailUtils() {}

    public static String ofuscateEmail(String email) {
        int atIndex = email.indexOf("@");
        String localPart = email.substring(0, atIndex);
        String domain = email.substring(atIndex);

        return email.substring(0, VISIBLE_CHARACTERS).concat("*".repeat(localPart.length() - VISIBLE_CHARACTERS)).concat(domain);
    }

    public static String formatEmail(String title, String content) {
        return String.format("""
            <!DOCTYPE html>
            <html lang='es'>
            <head>
                <meta charset='UTF-8'>
                <title>%s</title>
            </head>
            <body style='margin:0;padding:0;background-color:#f4f6f8;font-family:Arial,Helvetica,sans-serif;padding:20px;'>
    
                <table align='center' width='520' cellpadding='0' cellspacing='0' style='max-width:520px;border-radius:10px;overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,0.1);background-color:#ffffff;'>
                    <tr>
                        <td style='background-color:#f0e21e;padding:25px;text-align:center;'>
                            <h2 style='margin:0;color:#000000;font-size:20px;'>%s</h2>
                        </td>
                    </tr>
    
                    <tr>
                        <td style='padding:30px;color:#222222;font-size:15px;line-height:1.6;white-space:pre-line;'>
                            %s
                        </td>
                    </tr>
    
                    <tr>
                        <td style='background-color:#f0e21e;text-align:center;padding:15px;'>
                            <p style='margin:0;color:#000000;font-size:12px;font-weight:bold;'>
                                © 2026 Ceci Boroni Instructora
                            </p>
                        </td>
                    </tr>
    
                </table>
    
            </body>
            </html>
        """, title, title, content);
    }
}
