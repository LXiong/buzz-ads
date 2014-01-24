package com.buzzinate.adx.entity;


import com.buzzinate.adx.enums.Gender;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kun
 * Date: 13-6-29
 * Time: 下午10:38
 * 用户信息
 */
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 4547265605501110960L;
    private String cookieId;
    private String ip;
    private String userAgent;
    private Gender gender = Gender.unknown;
    private List<String> keywords;
    //TODO

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
