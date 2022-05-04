package rs.ac.su.vts.pm.projectmanagement.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Objects;
import java.util.Properties;

@Configuration
@Slf4j
@RequiredArgsConstructor
@Profile("!test & !test_it")
public class MailConfiguration
{

    private final Environment env;

    @Bean
    public JavaMailSender getMailSender()
    {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(env.getProperty("spring.mail.host"));
        mailSender.setPort(Integer.parseInt(Objects.requireNonNull(env.getProperty("spring.mail.port"))));
        mailSender.setUsername(env.getProperty("spring.mail.username"));
        mailSender.setPassword(env.getProperty("spring.mail.password"));

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", env.getProperty("spring.mail.properties.mail.smtp.starttls.enable", "true"));
        javaMailProperties.put("mail.smtp.auth", env.getProperty("spring.mail.properties.mail.smtp.auth", "true"));
        javaMailProperties.put("mail.transport.protocol", env.getProperty("spring.mail.properties.mail.transport.protocol", "smtp"));
        javaMailProperties.put("mail.debug", env.getProperty("spring.mail.properties.mail.debug", "false"));
        mailSender.setJavaMailProperties(javaMailProperties);

        return mailSender;
    }
}