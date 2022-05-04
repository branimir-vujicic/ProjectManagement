package rs.ac.su.vts.pm.projectmanagement.services.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.ac.su.vts.pm.projectmanagement.model.common.Mail;
import rs.ac.su.vts.pm.projectmanagement.model.entity.User;

@Service
@Slf4j
public class UserMailService
{

    private final String domain;

    private final MailService mailService;

    public UserMailService(@Value("${common.domain}") String domain,
            MailService mailService)
    {
        this.domain = domain;
        this.mailService = mailService;
    }

    @Async("asyncTaskExecutor")
    public void asyncSendCreatePasswordEmail(User user)
    {
        log.info("async sending email !");
        sendForgotPasswordEmail(user);
    }

    public void sendCreatePasswordEmail(User user)
    {
        log.info("sending email !");
        String body = "mail body";
        String subject = "mail subject";
        Mail mail = Mail.builder()
                .mailTo(user.getEmail())
                .mailSubject(subject)
                .contentType("text/html")
                .mailContent(body)
                .build();
        mailService.sendEmail(mail);
    }

    @Async("asyncTaskExecutor")
    public void asyncSendForgotPasswordEmail(User user)
    {
        log.info("async sending email !");
        sendForgotPasswordEmail(user);
    }

    public void sendForgotPasswordEmail(User user)
    {
        log.info("sending email !");
        String body = "mail body";
        String subject = "mail subject";
        Mail mail = Mail.builder()
                .mailTo(user.getEmail())
                .mailSubject(subject)
                .contentType("text/html")
                .mailContent(body)
                .build();
        mailService.sendEmail(mail);
    }
}
