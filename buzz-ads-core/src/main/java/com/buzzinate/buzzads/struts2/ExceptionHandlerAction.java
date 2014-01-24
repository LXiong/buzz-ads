package com.buzzinate.buzzads.struts2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.Action;

/**
 * 
 * @author zyeming
 *
 */
public class ExceptionHandlerAction {
    
    private static Log log = LogFactory.getLog(ExceptionHandlerAction.class);
    
    private Exception exception;
    
    public String execute() {
        log.error("Got an exception! ", exception);
        return Action.SUCCESS;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
