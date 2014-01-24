package com.buzzinate.adx.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 
 * @author feeling.yang
 * date 2013-8-2
 * 
 */
public class HtmlUtil {

    public static Boolean isLegalHtml(String html , String url) {
        if (null == html || null == url) {
            return false;
        }
        
/*        try {
            Document doc = Jsoup.parse(html, url);
        } catch (Exception e) {
            return false;
        }*/
        return true;
    }

}
