package com.buzzinate.adx.api;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * Format:
 *         http://<HOST/CXT_PATH>/api/dsp.j
 * post with parameter bid :{}
 * 
 * Returns:
 *         success or fail.
 * 
 * @author james.chen
 *
 */
// PATH!!! remember to use an extension, or struts will filter it!

@Component
@Path("dsp.j")
@XmlRootElement
@Scope("request")
public class DSPResouce {
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public String getMethod(@FormParam("bid") String bidInfo) {
        JSONObject parse = parseInfo(bidInfo);
        String requestId = parse.getString("rid");
        JSONArray banners = parse.getJSONArray("banner");
        return installback(requestId, banners).toJSONString();
    }
    
    
    public JSONObject parseInfo(String bidInfo) {
        return (JSONObject) JSONObject.parse(bidInfo);
    }
    
    /**
     * 
     * @param requestId
     * @param array
     * @return
     */
    public JSONObject installback(String requestId , JSONArray array) {
        JSONObject back = new JSONObject();
        back.put("requestId", requestId);
        JSONArray bidArray = new JSONArray();
        for (int i = 0; i < array.size(); i++) {
            JSONObject info = array.getJSONObject(i);
            JSONObject bidObject = new JSONObject();
            bidObject.put("slotId", info.get("slotId"));
            bidObject.put("link", "http://bshare.cn");
            bidObject.put("adId", 0);
            bidObject.put("price", getRandomFloat(5));
            bidObject.put("segment", "<div>Hello World'</div>");
            bidArray.add(bidObject);
        }
        back.put("bid", bidArray);
        return back;
    }
    
    public float getRandomFloat(int maxValue) {
        int randomValue = new Float(RandomUtils.nextFloat() * maxValue * 100).intValue();
        return randomValue / 100.00f;
    }
}
