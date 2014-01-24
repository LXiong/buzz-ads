package com.buzzinate.adx.api;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.buzzinate.adx.JsonParser;
import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.adx.message.WinnerMessage;
import com.buzzinate.adx.util.IdGenerator;
import com.buzzinate.adx.util.RealBidUtil;

/**
 * * Format:
 *         http://<HOST/CXT_PATH>/api/dsp.j
 * post with parameter bid :{}
 * @author james.chen
 *
 */
//PATH!!! remember to use an extension, or struts will filter it!
@Path("bidrequest.j")
@Component
@XmlRootElement
@Scope("request")
public class BidRequestResource {
    
    @DefaultValue("")
    @QueryParam("rtb")
    private String rtbInfo;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMethod() {
        JSONObject rtbjson = parseInfo();
        String requestId = IdGenerator.getInstance().getRequestId();
        RTBMessage rtb = JsonParser.getRTBMessage(rtbjson, requestId);
        JSONObject returnJson = new JSONObject();
        WinnerMessage winner = RealBidUtil.rtb(rtb);
        if (winner != null) {
            returnJson.put("DSPBID", winner.getBidResponseMessage().getAdInfos());
            returnJson.put("WINNER", winner.getWinners().toString());
        }
        return returnJson.toJSONString();
    }
    
    private JSONObject parseInfo() {
        return JSONObject.parseObject(rtbInfo);
    }

}
