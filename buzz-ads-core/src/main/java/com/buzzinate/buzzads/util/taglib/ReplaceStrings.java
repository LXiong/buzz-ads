package com.buzzinate.buzzads.util.taglib;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.components.Component;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * 
 * Replaces last occurrence of strings in the given string.
 * 
 * @author John Chen
 *
 */
public class ReplaceStrings extends Component {
    
    private static Log log = LogFactory.getLog(ReplaceStrings.class);
    
    private String value;
    private String propertiesList;
    private boolean isUrl;
    private boolean escape;
    
    public ReplaceStrings(ValueStack stack) {
        super(stack);
    }

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
    
    public boolean start(Writer writer)  {  
        try {
            if (log.isDebugEnabled()) {
                log.debug("ReplaceStrings:start(" + writer + ") started");
            }
            String originalText = (String) value;
            if (originalText == null) {
                originalText = "";
            }
            String returnString;

            if (isUrl) {
                if (originalText.startsWith("http://") || originalText.startsWith("https://")) {
                    returnString = originalText;
                } else {
                    returnString = "http://" + originalText;
                }
            } else {
                returnString = originalText;
            }
            
            if (propertiesList == null || propertiesList.isEmpty()) {
                if (getEscape()) {
                    returnString = StringEscapeUtils.escapeHtml(returnString);
                }
                writer.write(returnString);
                return true;
            }
 
            String first = "";
            String[] sList = propertiesList.split(",");
            for (int i = 0; i < sList.length; i++) {
                if (i % 2 == 0) {
                    first = sList[i];
                    if (i == sList.length - 1) {
                        returnString = returnString.replace(first, "");
                    }
                } else {
                    returnString = returnString.replace(first, sList[i]);
                }
            }
            
            if (getEscape()) {
                returnString = StringEscapeUtils.escapeHtml(returnString);
            }
            
            if (log.isDebugEnabled()) {
                log.debug("ReplaceStrings:(" + returnString + ") ended");
            }
            writer.write(returnString);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        return true;  
    }
}
