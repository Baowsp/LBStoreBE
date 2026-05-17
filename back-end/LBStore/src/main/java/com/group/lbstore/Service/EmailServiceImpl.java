package com.group.lbstore.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("nhe6060@gmail.com", "LBStore");
            helper.setTo(toEmail);
            helper.setSubject("Mã xác nhận đăng ký tài khoản LBStore");
            helper.setText(buildHtmlEmail(otp), true);

            mailSender.send(message);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Không thể gửi email xác nhận: " + e.getMessage());
        }
    }

    private String buildHtmlEmail(String otp) {
        return """
            <!DOCTYPE html>
            <html lang="vi">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
            </head>
            <body style="margin:0;padding:0;background-color:#f4f4f4;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,sans-serif;">
              <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f4f4f4;padding:32px 0;">
                <tr><td align="center">
                  <table width="480" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:24px;overflow:hidden;box-shadow:0 4px 24px rgba(0,0,0,0.08);">
                    <!-- Header -->
                    <tr>
                      <td style="background:linear-gradient(135deg,#dc2626,#b91c1c);padding:32px 40px;text-align:center;">
                        <h1 style="margin:0;color:#ffffff;font-size:28px;font-weight:900;letter-spacing:-0.5px;">LBStore</h1>
                        <p style="margin:8px 0 0;color:rgba(255,255,255,0.85);font-size:14px;">Xác nhận đăng ký tài khoản</p>
                      </td>
                    </tr>
                    <!-- Body -->
                    <tr>
                      <td style="padding:40px 40px 24px;">
                        <p style="margin:0 0 24px;color:#374151;font-size:15px;line-height:1.6;">
                          Xin chào! Bạn vừa yêu cầu tạo tài khoản trên <strong>LBStore</strong>.<br/>
                          Vui lòng sử dụng mã OTP bên dưới để hoàn tất đăng ký:
                        </p>
                        <!-- OTP Box -->
                        <div style="background:#fef2f2;border:2px dashed #fca5a5;border-radius:16px;padding:24px;text-align:center;margin:0 0 24px;">
                          <p style="margin:0 0 8px;color:#6b7280;font-size:13px;font-weight:600;text-transform:uppercase;letter-spacing:1px;">Mã xác nhận của bạn</p>
                          <span style="display:inline-block;font-size:40px;font-weight:900;letter-spacing:10px;color:#dc2626;font-family:'Courier New',monospace;">%s</span>
                          <p style="margin:12px 0 0;color:#9ca3af;font-size:12px;">Mã có hiệu lực trong <strong style="color:#dc2626;">5 phút</strong></p>
                        </div>
                        <p style="margin:0;color:#6b7280;font-size:13px;line-height:1.6;border-top:1px solid #f3f4f6;padding-top:20px;">
                          Nếu bạn không thực hiện yêu cầu này, hãy bỏ qua email này.<br/>
                          Không chia sẻ mã này với bất kỳ ai.
                        </p>
                      </td>
                    </tr>
                    <!-- Footer -->
                    <tr>
                      <td style="background:#f9fafb;padding:20px 40px;text-align:center;border-top:1px solid #f3f4f6;">
                        <p style="margin:0;color:#9ca3af;font-size:12px;">© 2025 LBStore. All rights reserved.</p>
                      </td>
                    </tr>
                  </table>
                </td></tr>
              </table>
            </body>
            </html>
            """.formatted(otp);
    }
}
