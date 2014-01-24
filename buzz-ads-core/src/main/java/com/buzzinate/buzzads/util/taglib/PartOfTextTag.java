package com.buzzinate.buzzads.util.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * Tag library to shorten text with ...
 * Can also handle html escaping
 * 
 * @author John Chen
 *
 */
public class PartOfTextTag extends ComponentTagSupport {
    
    private static final long serialVersionUID = -200366043517070259L;
    
    private String value;
    private String textmaxlength;
    private boolean escape;

    public String getTextmaxlength() {
        return textmaxlength;
    }
    public void setTextmaxlength(String textmaxlength) {
        this.textmaxlength = textmaxlength;
    }
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
        return new PartOfText(stack);  
    }
    
    protected void populateParams() {  
        super.populateParams();
        PartOfText partOfText = (PartOfText) component;
        partOfText.setValue(value);
        partOfText.setTextmaxlength(textmaxlength);
        partOfText.setEscape(escape);
    }
}
