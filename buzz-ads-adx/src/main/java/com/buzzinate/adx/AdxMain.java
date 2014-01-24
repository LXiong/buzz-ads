package com.buzzinate.adx;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.buzzinate.adx.actors.DispatcherActor;
import com.buzzinate.adx.message.RTBMessage;
import com.buzzinate.adx.util.IdGenerator;

/**
 * Created with IntelliJ IDEA.
 * User: chris
 * Date: 13-7-22
 * Time: 上午10:42
 * 启动main class
 */
public class AdxMain {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("adxSystem");
        ActorRef actorRef = system.actorOf(new Props(DispatcherActor.class), "dispatcher");
        actorRef.tell(new RTBMessage(IdGenerator.getInstance().getRequestId() , "123" , null , null , null , null , "" ,
                null));
        actorRef.tell(new RTBMessage(IdGenerator.getInstance().getRequestId() , "123" , null , null , null , null , "" ,
                null));
        actorRef.tell(new RTBMessage(IdGenerator.getInstance().getRequestId() , "123" , null , null , null , null , "" ,
                null));
        actorRef.tell(new RTBMessage(IdGenerator.getInstance().getRequestId() , "123" , null , null , null , null , "" ,
                null));
        actorRef.tell(new RTBMessage(IdGenerator.getInstance().getRequestId() , "123" , null , null , null , null , "" ,
                null));
        actorRef.tell(new RTBMessage(IdGenerator.getInstance().getRequestId() , "123" , null , null , null , null , "" ,
                null));
        actorRef.tell(new RTBMessage(IdGenerator.getInstance().getRequestId() , "123" , null , null , null , null , "" ,
                null));
    }
}
