package com.buzzinate.buzzads.common.service;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.buzzinate.buzzads.common.exception.ServiceException;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.pennywise.framework.email.MailInfo;



/**
 * 
 * @author John Chen Nov 3, 2009 Copyright 2009 Buzzinate Co. Ltd.
 * 
 */
public class MailServices {

    private static Log log = LogFactory.getLog(MailServices.class);

    private static final String HOST = ConfigurationReader.getString("mail.host");
    private static final int PORT = ConfigurationReader.getInt("mail.port");
    private static final String USER = ConfigurationReader.getString("mail.user");
    private static final String PASSWORD = ConfigurationReader.getString("mail.password");
    private static Session session;
    static {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.port", PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", ConfigurationReader.getString("mail.ssl.factory"));
        props.put("mail.smtp.socketFactory.fallback", "false");

        session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER , PASSWORD);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static void sendMail(MailInfo mailInfo) throws ServiceException {

        try {
            Message msg = new MimeMessage(session);
            InternetAddress fromAddress = mailInfo.getFrom();

            // To:
            List<InternetAddress> recipientsList = mailInfo.getTOAddresses();
            if (recipientsList != null) {
                InternetAddress[] addressTo = new InternetAddress[recipientsList.size()];
                for (int i = 0; i < recipientsList.size(); i++) {
                    String a = recipientsList.get(i).getAddress();
                    /*
                     * It will fail to send to FROM address if it's also in TO
                     * list. In this case, we change from to our
                     * DoNotReply@bshare...
                     */
                    if (fromAddress != null && fromAddress.getAddress().equalsIgnoreCase(a)) {
                        fromAddress.setAddress(ConfigurationReader.getString("mail.user"));
                    }
                    addressTo[i] = new InternetAddress(a);
                }
                msg.setRecipients(Message.RecipientType.TO, addressTo);
            }

            // From:
            if (fromAddress != null) {
                msg.setFrom(fromAddress);
            }

            // CC:
            List<InternetAddress> recipientsListCc = mailInfo.getCCAddresses();
            if (recipientsListCc != null) {
                InternetAddress[] addressToCc = new InternetAddress[recipientsListCc.size()];
                for (int i = 0; i < recipientsListCc.size(); i++) {
                    addressToCc[i] = new InternetAddress(recipientsListCc.get(i).getAddress());
                }
                msg.setRecipients(Message.RecipientType.CC, addressToCc);
            }

            // BCC:
            List<InternetAddress> recipientsListBcc = mailInfo.getBCCAddresses();
            if (recipientsListBcc != null) {
                InternetAddress[] addressToBcc = new InternetAddress[recipientsListBcc.size()];
                for (int i = 0; i < recipientsListBcc.size(); i++) {
                    addressToBcc[i] = new InternetAddress(recipientsListBcc.get(i).getAddress());
                }
                msg.setRecipients(Message.RecipientType.BCC, addressToBcc);
            }
            try {
                msg.setSubject(MimeUtility.encodeText(mailInfo.getSubject(), "UTF-8", "B"));
            } catch (Exception e) {
                log.error("set subject error", e);
            }
            msg.setContent(mailInfo.getMessage(), "text/html; charset=UTF-8");

            Address replyto = mailInfo.getReplyTo();
            if (replyto != null) {
                msg.setReplyTo(new Address[] {replyto });
            }

            Transport transport = session.getTransport("smtps");
            transport.connect(HOST, PORT, USER, PASSWORD);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

            log.debug("send mail success: to=" + mailInfo.getTOAddresses() + " cc=" + mailInfo.getCCAddresses() +
                    " bcc=" + mailInfo.getBCCAddresses());
        } catch (MessagingException e) {
            throw new ServiceException("Failed to send email." , e);
        }
    }

}