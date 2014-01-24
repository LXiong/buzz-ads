package com.buzzinate.buzzads.util.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * Tag library to add http:// to links that do not have it in front.
 * 
 * @author John Chen
 *
 */
public class GetHrefTag extends ComponentTagSupport {

    private static final long serialVersionUID = 2030578355707703318L;
    
    private String value;
    private boolean escape;
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }  
    public void setEscape(boolean escape) {
        this.escape = escape;
    }
    public boolean getEscape() {
        return escape;
    }
    
    @Override
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new GetHref(stack);
    }
    
    protected void populateParams() {  
        super.populateParams();
        GetHref getHref = (GetHref) component;
        getHref.setValue(value);
        getHref.setEscape(escape);
    }
}
