package com.buzzinate.buzzads.common.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-11-20
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1698029651780685148L;
    
    private Throwable cause;
    
    public ServiceException(String msg) {
        super(msg);
    }
    
    public ServiceException() {
        super();
    }
    
    public ServiceException(Throwable ex) {
        this.cause = ex;
    }
    
    public ServiceException(String msg, Throwable ex) {
        super(msg);
        this.cause = ex;
    }
    
    @Override
    public Throwable getCause() {
        return this.cause == null ? null : this.cause;
    }

    @Override
    public String getMessage() {
        if (this.cause == null || this.cause == this) {
            return super.getMessage();
        } else {
            return super.getMessage() + "; Exception is " + 
                       this.cause.getClass().getName() + ": " + this.cause.getMessage();
        }
    }

    @Override
    public void printStackTrace(PrintStream ps) {
        if (this.cause == null || this.cause == this) {
            super.printStackTrace(ps);
        } else {
            ps.println(this);
            this.cause.printStackTrace(ps);
        }
    }

    @Override
    public void printStackTrace(PrintWriter pw) {
        if (this.cause == null || this.cause == this) {
            super.printStackTrace(pw);
        } else {
            pw.println(this);
            this.cause.printStackTrace(pw);
        }
    }
}
