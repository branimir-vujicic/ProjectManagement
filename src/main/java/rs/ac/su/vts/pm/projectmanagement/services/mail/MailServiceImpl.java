package rs.ac.su.vts.pm.projectmanagement.services.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import rs.ac.su.vts.pm.projectmanagement.exception.SendMailException;
import rs.ac.su.vts.pm.projectmanagement.model.common.Mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("mailService")
@Slf4j
public class MailServiceImpl
		implements MailService
{

	private final String mailFrom;
	private final JavaMailSender mailSender;

	public MailServiceImpl(@Value("${mail.from:eprotokol.test@gmail.com}") String mailFrom,
			JavaMailSender mailSender)
	{
		this.mailFrom = mailFrom;
		this.mailSender = mailSender;
	}

	@Override
	public void sendEmail(String to, String body, String subject)
	{
		log.debug("sending email to {} ; subject {} \n {}", to, subject, body);

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setFrom(mailFrom);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setText(body);

			mailSender.send(mimeMessageHelper.getMimeMessage());
		}
		catch (MessagingException e) {
			log.error("Error sending email", e);
		}
	}

	@Override
	public void sendEmail(Mail mail)
	{
		log.debug("sending email to {} ; subject {} \n {}", mail.getMailTo(), mail.getMailSubject(), mail.getMailContent());

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
			mimeMessageHelper.setSubject(mail.getMailSubject());
			mimeMessageHelper.setFrom(mailFrom);
			mimeMessageHelper.setTo(mail.getMailTo());
			mimeMessageHelper.setText(mail.getMailContent(), true);

			mailSender.send(mimeMessageHelper.getMimeMessage());
		}
		catch (MessagingException e) {
			log.error("Error sending email", e);
			throw new SendMailException();
		}
	}
}
