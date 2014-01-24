package com.buzzinate.buzzads.client;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.thrift.AdItem;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public final class AdsGenerateClient {
    public static String LINK = "link";
    ;
    public static String IMG = "img";
    public static String TITLE = "title";
    public static String DISPLAYURL = "displayUrl";
    public static String DESCRIPTION = "description";
    private static String CLICL_LINK = ConfigurationReader.getString("click.path");

    public static Map<String, String> generate(AdItem item, String adsInfoStr) {
        Map<String, String> adsMap = new HashMap<String, String>();
        String adLink = CLICL_LINK + "?p=" + encode(adsInfoStr) + "&link=" + encode(item.getUrl());
        adsMap.put(LINK, adLink);
        adsMap.put(TITLE, item.getTitle());
        if (item.getPicOption().isDefined()) {
            adsMap.put(IMG, item.getPic().replace("!buzz", ""));
        }
        if (item.getDescriptionOption().isDefined()) {
            adsMap.put(DESCRIPTION, item.getDescription());
        }
        if (item.getDisplayUrlOption().isDefined()) {
            adsMap.put(DISPLAYURL, item.getDisplayUrl());
        }

        return adsMap;
    }

    private static String encode(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }
}
