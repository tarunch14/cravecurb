package org.cravecurb.notifications;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.Message.Status;
import com.twilio.type.PhoneNumber;

import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
	
    @Value("${twilio.account-SID}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.number}")
    private String twilioNumber;
    
    @PostConstruct
    void init() {
    	Twilio.init(accountSid, authToken);
    }

	public static final TemplateLoader TEMPLATE_LOADER = new ClassPathTemplateLoader("/email-templates", ".html");

	public static final Handlebars HANDLEBARS = new Handlebars(TEMPLATE_LOADER);

	private final JavaMailSender sender;

	@Value("${spring.mail.username}")
	private String sendFrom;

	public void sendEmailNotification(String recipient, String subject, String templateName,
			Map<String, Object> context) {
		try {
			String html = HANDLEBARS.compile(templateName).apply(context);

			MimeMessage mail = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mail, true, "utf-8");
			helper.setFrom(sendFrom); // NOSONAR
			helper.setTo(recipient);
			helper.setSubject(subject);
			helper.setText(html, true);

			sender.send(mail);
		} catch (Exception e) {
			System.out.println("Failed to send mail" + e);
		}
	}
	
	public Status sendSmsNotification(String toNumber, String message) {
		try {
			return Message.creator(new PhoneNumber(toNumber), new PhoneNumber(twilioNumber), message).create().getStatus();
			
//			ResourceSet<Message> messages = Message.reader().read(); //getting all sent messages
//			for (Message msg : messages) {
//				if (msg.getTo().equals(toNumber)) return msg.getStatus();
//			}
		} catch (Exception e) {
			throw new IllegalArgumentException("sms not sent !");
		}
//		return null;
	}


}
