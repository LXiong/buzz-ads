package com.buzzinate.buzzads.api;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.unitils.UnitilsTestNG;

import com.buzzinate.common.util.http.SimpleHttpClient;

public class AdxDspRegisterApiActionTest extends UnitilsTestNG {

    private static String registerUrl = "";

    @Test
    public void register() {
        Map<String, String> params = new HashMap<String, String>();
        long epoch = System.currentTimeMillis() / 1000;
        params.put("ts", String.valueOf(epoch));
        SimpleHttpClient client = new SimpleHttpClient(1, 10, 10000, 10000);
        client.post(registerUrl, params);

    }
}
