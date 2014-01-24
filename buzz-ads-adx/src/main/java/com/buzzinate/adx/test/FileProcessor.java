package com.buzzinate.adx.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import akka.actor.PoisonPill;
import akka.actor.UntypedActor;
import akka.util.Duration;

public class FileProcessor extends UntypedActor {
	
    private String name ;
    
    
    public FileProcessor(){
    }
    
    public FileProcessor(String name) {
        this.name = name;
    }

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void onReceive(Object arg0) throws Exception {
        if (arg0 instanceof Integer) {
            long start = System.currentTimeMillis();
            Integer value = (Integer) arg0;
            Thread.sleep(10);
            List<Integer> values = new ArrayList<Integer>();
            values.add(value++);
            values.add(value++);
            values.add(value++);
            long end = System.currentTimeMillis();
            getSender().tell(values);
            getContext().stop(getSelf());
        } else if (arg0 instanceof List) {
            System.out.println(arg0);
        }
    }
}
