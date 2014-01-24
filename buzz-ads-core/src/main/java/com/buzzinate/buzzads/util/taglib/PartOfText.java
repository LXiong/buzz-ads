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
 * Tag library to shorten text with ...
 * Can also handle html escaping
 * 
 * @author John Chen
 *
 */
public class PartOfText extends Component {
    
    private static Log log = LogFactory.getLog(PartOfText.class);
    
    private String value;
    private String textmaxlength;
    private boolean escape;

    public PartOfText(ValueStack stack) {
        super(stack);
    }
    
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getTextmaxlength() {
        return textmaxlength;
    }
    public void setTextmaxlength(String textmaxlength) {
        this.textmaxlength = textmaxlength;
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
                log.debug("PartOfText:start(" + writer + ") started");
            }
            String originalText = (String) value;
            String partOfText = null;
            
            int maxLength = Integer.parseInt(getTextmaxlength());
            if (maxLength < originalText.length()) {
                partOfText = originalText.substring(0, maxLength - 3);    
                partOfText = partOfText.concat("...");
            } else {
                partOfText = originalText;
            }
            
            if (getEscape()) {
                partOfText = StringEscapeUtils.escapeHtml(partOfText);
            }
            
            if (log.isDebugEnabled()) {
                log.debug("PartOfText:(" + partOfText + ") ended");
            }
            writer.write(partOfText);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        return true;  
    }
}
