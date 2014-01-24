package com.buzzinate.buzzads.domain;

import org.safehaus.uuid.UUID;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * 网站信息
 *
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *         <p/>
 *         2012-12-12
 */
public class Site implements Serializable {

    private static final long serialVersionUID = 552896770441461634L;
    protected static DecimalFormat format = new DecimalFormat("#,###,##0.00%");
    protected static DecimalFormat doubleformat = new DecimalFormat("#,###,##0.00");
    private int userId;
    private String uuid;
    private String secret;
    private String url;
    private String name;
    // 网站收入
    private int siteComm;
    // pv
    private int pageview;
    private int views;
    private int clicks;

    public Site(String uuid, String url) {
        super();
        this.uuid = uuid;
        this.url = url;
    }

    public Site(String uuid, String url, int siteComm) {
        this(uuid, url);
        this.siteComm = siteComm;
    }

    public Site() {
        super();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Used by Hibernate to get UUID from database.
     *
     * @return
     */
    public byte[] getUuidBytes() {
        if (this.uuid != null) {
            UUID uuidx = new UUID(this.uuid);
            return uuidx.asByteArray();
        } else {
            return null;
        }
    }

    /**
     * Used by Hibernate to set UUID in database.
     *
     * @param uid
     */
    public void setUuidBytes(byte[] uid) {
        if (uid != null) {
            UUID uuidx = new UUID(uid);
            this.uuid = uuidx.toString();
        } else {
            this.uuid = null;
        }
    }

    public String getSecret() {
        return secret;
    }

    public void setUuidSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Used by Hibernate to get UUID from database.
     *
     * @return
     */
    public byte[] getSecretBytes() {
        if (this.secret != null) {
            UUID secretx = new UUID(this.secret);
            return secretx.asByteArray();
        } else {
            return null;
        }
    }

    /**
     * Used by Hibernate to set UUID in database.
     *
     * @param secret
     */
    public void setSecretBytes(byte[] secret) {
        if (secret != null) {
            UUID secretx = new UUID(secret);
            this.secret = secretx.toString();
        } else {
            this.secret = null;
        }
    }

    public int getSiteComm() {
        return siteComm;
    }

    public void setSiteComm(int siteComm) {
        this.siteComm = siteComm;
    }

    public String getCommDouble() {
        return doubleformat.format(Double.valueOf(siteComm / 100.00));
    }

    public int getPageview() {
        return pageview;
    }

    public void setPageview(int pageview) {
        this.pageview = pageview;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public String getClickToView() {
        if (views == 0)
            return "-";
        return format.format((double) clicks / views);
    }

}
