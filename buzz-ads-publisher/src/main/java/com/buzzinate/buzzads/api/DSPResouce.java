package com.buzzinate.buzzads.api;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

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
@Path("dsp.j")
@Component
@XmlRootElement
@Scope("request")
public class DSPResouce {
    
    @DefaultValue("")
    @QueryParam("bid")
    private String bidInfo;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMethod() {
        JSONObject parse = parseInfo();
        String requestId = parse.getString("requestId");
        JSONArray banners = parse.getJSONArray("bid");
        return installback(requestId, banners).toJSONString();
    }
    
    @POST
    @Path("dsppost.j")
    @Produces(MediaType.APPLICATION_JSON)
    public String postMethd() {
        JSONObject parse = parseInfo();
        String requestId = parse.getString("requestId");
        JSONArray banners = parse.getJSONArray("bid");
        return installback(requestId, banners).toJSONString();
    }
    
    
    public JSONObject parseInfo() {
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
            bidObject.put("price", 3.05);
            bidObject.put("segment", "<html></html>");
            bidArray.add(bidObject);
        }
        back.put("bid", bidArray);
        return back;
    }

}
