package com.framework.util;

import net.sourceforge.htmlunit.corejs.javascript.ast.SwitchCase;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Properties;
import javax.mail.*;
import javax.mail.search.FlagTerm;

public class RetriveEmailMessageContent {

    public static void main(String[] args) throws Exception {
        RetriveEmailMessageContent sample = new RetriveEmailMessageContent();
        sample.readMail();
    }

    Properties properties = null;
    private Session session = null;
    private Store store = null;
    private Folder inbox = null;
    private String userName = "user";
    private String password = "password";

    public RetriveEmailMessageContent() {}

    public void readMail() throws Exception {
        properties = new Properties();
        properties.setProperty("mail.host", "outlook.office365.com");
        properties.setProperty("mail.port", "993");
        properties.setProperty("mail.transport.protocol", "imaps");
        session = Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(userName, password);
                    }
                });
        try {
            store = session.getStore("imaps");
            store.connect();
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message messages[] = inbox.search(new FlagTerm(
                    new Flags(Flags.Flag.SEEN), false));
            String body;
            System.out.println("Number of emails = " + messages.length);

            for (int i = messages.length - 1; i >= 0; i--) {
           // Message message = messages[messages.length - 1];

            Address[] from = message.getFrom();
            System.out.println("-------------------------------");
            System.out.println("Date: " + message.getSentDate());
            System.out.println("From: " + from[0]);
            System.out.println("Subject: " + message.getSubject());
            Object content = message.getContent();

            if (content instanceof String) {
                body = (String) content;
                Parsing HTML body to String or any other specific element and printing it
                System.out.println("Body: " + parseHtml(body.toString()));

            } else if (content instanceof Multipart) {
                Multipart multiPart = (Multipart) content;
            }
            System.out.println("--------------------------------");
            }
            inbox.close(true);
            store.close();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public String parseHtml(String bodyText) {
        String html = bodyText;
        Document doc = Jsoup.parse(html);
        Element paragraph = doc.select("p").first();
        String text = doc.body().text();
        return text;
    }

    // Converting multipart email
    public String processMultiPart(Multipart content) throws Exception {
        int multiPartCount = content.getCount();
        for (int i = 0; i < multiPartCount; i++) {
            BodyPart bodyPart = content.getBodyPart(i);
            Object body;
            body = bodyPart.getContent();
            if (body instanceof String) {
                System.out.println(body);
              return  body;
            } else if (body instanceof Multipart) {
                return processMultiPart((Multipart) body);
            }
        }
        return null;
    }
}