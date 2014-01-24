package com.buzzinate.buzzads.core.service;
import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.domain.AdEntry;
import com.buzzinate.buzzads.util.MailGunClient;
import com.pennywise.framework.email.MailInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Service
public class AdminEmailService {
    private static Log log = LogFactory.getLog(AdminEmailService.class);

    public void sendAdEntryVerifyEmail(AdEntry entry) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setFrom(ConfigurationReader.getString("ads.buzzinate.email"), ConfigurationReader.getString("ads.buzzinate.email.user"));
        mailInfo.addTOAddress(ConfigurationReader.getString("ads.buzzinate.email"), ConfigurationReader.getString("ads.buzzinate.email.user"));
        mailInfo.setSubject(ConfigurationReader.getString("ads.buzzinate.adentry.email.verify.subject"));
        mailInfo.setContentType("html");
        Map<String, String> map = new HashMap<String, String>();
        map.put("&id&", String.valueOf(entry.getId()));
        map.put("&name&", entry.getName());
        map.put("&companyName&", entry.getAdvertiserName());

        String message = replacePlaceHolders(ConfigurationReader.getString("ads.buzzinate.adentry.email.verify.body"), map);
        mailInfo.setMessage(message);
        try {
            MailGunClient.sendMail(mailInfo);
        } catch (Exception e) {
            log.error("Failed to send verify email", e);
        }
    }

    /**
     * Replace the place holders in the given string and return the result
     *
     * @param body
     * @param map
     * @return
     */
    private String replacePlaceHolders(String body, Map<String, String> map) {
        String result = body;
        Set<Entry<String, String>> s = map.entrySet();
        Iterator<Entry<String, String>> iterator = s.iterator();
        while (iterator.hasNext()) {
            String temp = iterator.next().getKey();
            result = result.replace(temp, map.get(temp));
        }
        return result;
    }

}
