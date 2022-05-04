package rs.ac.su.vts.pm.projectmanagement.services.mail;

import rs.ac.su.vts.pm.projectmanagement.model.common.Mail;

public interface MailService
{
	void sendEmail(String to, String body, String subject);

	void sendEmail(Mail mail);
}
