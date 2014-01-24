package com.buzzinate.adx.actors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import akka.actor.UntypedActor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.buzzinate.adx.entity.UserInfo;
import com.buzzinate.adx.message.BidRequestMessage;
import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.adx.util.HBaseClient;
import com.buzzinate.adx.util.UserInfoUtils;
import com.buzzinate.common.util.codec.BASE62Encoder;
import com.buzzinate.common.util.string.CookieIdUtil;

/**
 * @author Martin
 * Created with IntelliJ IDEA.
 * Date: 13-7-22
 * Time: 下午6:37
 * Cookie Lookup Actor.
 */
public class CookieActor extends UntypedActor {

    private static Log log = LogFactory.getLog(CookieActor.class);

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof RTBMessage) {
            log.info("cookieActor start time = " + System.currentTimeMillis());
            UserInfo userInfo = new UserInfo();
            // get cookie id from rtb message
            String cookieId = ((RTBMessage) message).getCookieId();
            if (StringUtils.isNotEmpty(cookieId)) {
                // judge the lawfulness of cookie
                String decodeCookieId = BASE62Encoder.decode(cookieId);
                if (CookieIdUtil.isValidCookieId(decodeCookieId)) {
                    try {
                        String uid = "";
                        // get user info from hbase
                        HTableInterface profileTable = HBaseClient.getTable("weibo_profile");
                        HTableInterface uidTable = HBaseClient.getTable("weibo_cookie_uid");
                        if (uidTable != null) {
                            Result r = uidTable.get(new Get(Bytes.toBytes(decodeCookieId)));
                            uid = Bytes.toString(r.getValue(Bytes.toBytes("uid"), Bytes.toBytes("1")));
                        }
                        if (profileTable != null && StringUtils.isNotEmpty(uid)) {
                            Result r = profileTable.get(new Get(Bytes.toBytes(uid)));
                            String data = Bytes.toString(r.getValue(Bytes.toBytes("data"), Bytes.toBytes("1")));
                            JSONObject obj = JSON.parseObject(data);
                            String gender = obj.getString("gender");
                            userInfo.setGender(UserInfoUtils.getGender(gender));
                        }
                    } catch (Exception e) {
                        log.error("Get user info error!", e);
                    }
                }
            }
            getSender().tell(new BidRequestMessage((RTBMessage) message, userInfo));
            getContext().stop(getSelf());
            log.info("cookieActor end time = " + System.currentTimeMillis());
        } else {
            log.error("receive unknown  message==>[" + message + "]");
        }
    }
}
