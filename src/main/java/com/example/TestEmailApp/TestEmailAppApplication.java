package com.example.TestEmailApp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
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
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream("src/main/resources/application.properties");
			prop.load(input);
			return prop.getProperty(templateID);
		} catch (IOException ex) {
			ex.printStackTrace();
			return "no template";
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
			// send status info about email
			transport.addTransportListener(new TransportListener() {

				@Override
				public void messageDelivered(TransportEvent e) {
					// TODO Auto-generated method stub
					System.out.println("Successful in sending to email server");

				}

				@Override
				public void messageNotDelivered(TransportEvent e) {
					// TODO Auto-generated method stub
					System.out.println("Unsuccessful in sending to email server");
				}

				@Override
				public void messagePartiallyDelivered(TransportEvent e) {
					// TODO Auto-generated method stub
					System.out.println("Unsuccessful in sending to email server");
				}

			});
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (AddressException ae) {
			ae.printStackTrace();
		} catch (MessagingException me) {
			me.printStackTrace();
		}
	}

}
