package com.buzzinate.buzzads.domain;

import java.io.Serializable;

/**
 * 
 * @author Johnson
 *
 */
public class AdvertiserContactInfo implements Serializable {
    private static final long serialVersionUID = -4135334332991169056L;

    private int id;
    
    private int advertiserId;
    
    private String address;
    
    private String email;
    
    private String mobile;
    
    private String name;
    
    private String qq = "";
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(int advertiserId) {
        this.advertiserId = advertiserId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

}