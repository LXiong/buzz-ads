package com.buzzinate.buzzads.util;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.pennywise.framework.email.MailInfo;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.multipart.FormDataMultiPart;

import javax.mail.internet.InternetAddress;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Mail Gun Client
 * @author Lucas
 *
 */
public final class MailGunClient {
    private static final String MAIL_GUN_API_KEY = ConfigurationReader.getString("mail.gun.api.key");
    private static final String MAIL_GUN_API_URL = ConfigurationReader.getString("mail.gun.api.url");
    private static final String MAIL_USER_ADDRESS = ConfigurationReader.getString("mail.user");

    private MailGunClient() {
    }

    @SuppressWarnings("unchecked")
    public static void sendMail(MailInfo mailInfo) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api" , MAIL_GUN_API_KEY));
        WebResource webResource = client.resource(MAIL_GUN_API_URL);
        FormDataMultiPart form = new FormDataMultiPart();

        // set From Address
        if (MAIL_USER_ADDRESS != null) {
            form.field("from", MAIL_USER_ADDRESS);
        }
        
        // set To Address
        List<InternetAddress> toAddrList = (List<InternetAddress>) mailInfo.getTOAddresses();
        if (toAddrList != null) {
            for (InternetAddress toAddr : toAddrList) {
                form.field("to", toAddr.getAddress());
            }
        }
        
        // set CC Address
        List<InternetAddress> ccAddrList = (List<InternetAddress>) mailInfo.getCCAddresses();
        if (ccAddrList != null) {
            for (InternetAddress ccAddress : ccAddrList) {
                form.field("cc", ccAddress.getAddress());
            }
        }
        
        // set BCC Address
        List<InternetAddress> bccAdrressList = (List<InternetAddress>) mailInfo.getBCCAddresses();
        if (bccAdrressList != null) {
            for (InternetAddress bccAdrress : bccAdrressList) {
                form.field("bcc", bccAdrress.getAddress());
            }
        }
        
        // set subject
        String subject = mailInfo.getSubject();
        if (subject != null) {
            form.field("subject", subject);
        }
        
        // set content type and mail message , the default type is html 
        if (mailInfo.getContentType().equalsIgnoreCase("text/plain")) {
            form.field("text", mailInfo.getMessage().toString());
        } else {
            form.field("html", mailInfo.getMessage().toString());
        }
        
        // send mail
        webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, form);
    }

}
