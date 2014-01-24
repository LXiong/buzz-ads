package com.buzzinate.adx.enums;

/**
 * Created with IntelliJ IDEA. User: kun Date: 13-6-29 Time: 下午10:38 性别.
 */
public enum Gender {
    male(1, "M"), female(2, "F"), unknown(99, "O");

    private int code;

    private String nick;

    private Gender(int code, String nick) {
        this.code = code;
        this.nick = nick;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }
}
