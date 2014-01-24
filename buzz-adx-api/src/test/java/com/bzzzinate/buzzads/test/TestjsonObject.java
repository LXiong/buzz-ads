package com.bzzzinate.buzzads.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class TestjsonObject {

    /**
     * @param args
     */
    public static void main(String[] args) {

        JSONObject object = new JSONObject();
        try {
            object.put("requestId", "12345");
            JSONArray array = new JSONArray();
            for (int i = 0; i < 5; i++) {
                JSONObject banner = new JSONObject();
                banner.put("slotId", i);
                banner.put("adId", 0);
                array.add(banner);
            }
            object.put("bid", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(object.toJSONString());
        JSONObject rt = new JSONObject();
        try {
            rt.put("cId", "12345");
            JSONArray array = new JSONArray();
            for (int i = 0; i < 5; i++) {
                JSONObject banner = new JSONObject();
                banner.put("id", i);
                banner.put("pos", 1);
                banner.put("size", 2);
                banner.put("btype", 1);
                array.add(banner);
            }
            rt.put("banner", array);
            JSONObject media = new JSONObject();
            media.put("url", "bshare.cn");
            media.put("price", "2");
            media.put("blockUrl", new String[] {"1.c", "2.c", "3.c" });
            media.put("keyWords", new String[] {"1", "2", "3" });
            rt.put("media", media);
            rt.put("ip", "127.0.0.1");
            rt.put("agent", "agent");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(rt.toJSONString());

    }

}
