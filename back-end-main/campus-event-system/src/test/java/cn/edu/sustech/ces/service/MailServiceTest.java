package cn.edu.sustech.ces.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(MockitoExtension.class)
public class MailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailService mailService;

    private final String to = "test@example.com";
    private final String subject = "Test Subject";

    @Test
    void testSendSimpleMessage() {
        String text = "Hello, this is a test email.";
        mailService.sendSimpleMessage(to, subject, text);

        // 验证是否创建了正确的邮件消息
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendHtmlMessage() {
        mailService.sendHtmlMessage(to, subject, "<h1>Hello</h1>");

        // 验证是否创建了正确的 HTML 邮件消息
        verify(mailSender).send(any(MimeMessagePreparator.class));
    }
}
