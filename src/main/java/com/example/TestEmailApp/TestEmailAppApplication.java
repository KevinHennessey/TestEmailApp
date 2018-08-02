package com.example.TestEmailApp;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestEmailAppApplication {

	private static String fromEmail = "JavaTestEmailXavient@gmail.com";
	private static String password = "Xav@12345";
	private static String customerEmail = "kevinh@xavient.com";
	private static String emailSubject = "{company} Appointment Reminder";
	private static String templateID = "reminderTime_0";

	public static void main(String[] args) {
		
		sendEmail(fromEmail, password, customerEmail, emailSubject, templateID);
		
		SpringApplication.run(TestEmailAppApplication.class, args);
	}

	static String getEmailBody(String templateID) {

		String template;
		// templates would be pulled from storage
		switch (templateID) {
		case "reminderTime_0":
			template = "template0";
			break;
		case "reminderTime_2":
			template = "template2";
			break;
		case "reminderTime_10":
			template = "template10";
			break;
		case "reminderTime_20":
			template = "template20";
			break;
		default:
			template = "invalid";
			break;
		}
		return template;
	}

	public static void sendEmail(String from, String pass, String to, String subject, String templateID) {
		Properties props = System.getProperties();
		String host = "smtp.gmail.com";
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getDefaultInstance(props);
		MimeMessage message = new MimeMessage(session);

		try {
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setSubject(subject);
			message.setText(getEmailBody(templateID));
			Transport transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

}
