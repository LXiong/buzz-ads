package com.buzzinate.buzzads.util.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * Replaces last occurrence of strings in the given string.
 * 
 * @author John Chen
 *
 */
public class ReplaceStringsTag extends ComponentTagSupport {

    private static final long serialVersionUID = -6193038542507825867L;

    private String value;
    private String propertiesList;
    private boolean isUrl;
    private boolean escape;

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getPropertiesList() {
        return propertiesList;
    }
    public void setPropertiesList(String propertiesList) {
        this.propertiesList = propertiesList;
    }
    
    public void setIsUrl(boolean isUrl) {
        this.isUrl = isUrl;
    }
    public boolean getIsUrl() {
        return isUrl;
    }
    
    public void setEscape(boolean escape) {
        this.escape = escape;
    }
    public boolean getEscape() {
        return escape;
    }
    
    @Override
    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
        return new ReplaceStrings(stack);
    }
    
    protected void populateParams() {  
        super.populateParams();
        ReplaceStrings replaceStrings = (ReplaceStrings) component;
        replaceStrings.setValue(value);
        replaceStrings.setIsUrl(isUrl);
        replaceStrings.setPropertiesList(propertiesList);
        replaceStrings.setEscape(escape);
    }
}
