package rs.ac.su.vts.pm.projectmanagement.config.dev;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Message;
import javax.mail.internet.MimeMessage;

@Configuration
@Slf4j
@RequiredArgsConstructor
@Profile("test || test_it")
public class MailConfiguration
{

	private final Environment env;

	@Bean
	public JavaMailSender getMailSender() {
		return new JavaMailSenderImpl() {
			@SneakyThrows
			public void send(MimeMessage mimeMessage) throws MailException {
				log.info("SENDING EMAIL to: {}; subject : {}; body : {}", mimeMessage.getRecipients(Message.RecipientType.TO), mimeMessage.getSubject(), mimeMessage.getContent());
			}
		};
	}
}