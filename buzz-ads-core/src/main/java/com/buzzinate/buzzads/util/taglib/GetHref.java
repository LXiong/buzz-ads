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
 * Tag library to add http:// to links that do not have it in front.
 * 
 * @author John Chen
 *
 */
public class GetHref extends Component {
    
    private static Log log = LogFactory.getLog(GetHref.class);
    
    private String value;
    private boolean escape;
    
    public GetHref(ValueStack stack) {
        super(stack);
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
    
    public boolean start(Writer writer)  {  
        try {
            if (log.isDebugEnabled()) {
                log.debug("GetHref:start(" + writer + ") started");
            }
            String originalText = (String) value;
            String getHref = null;
            
            if (originalText.startsWith("http://") || originalText.startsWith("https://")) {
                getHref = originalText;
            } else {
                getHref = "http://" + originalText;
            }
            
            if (getEscape()) {
                getHref = StringEscapeUtils.escapeHtml(getHref);
            }
            
            if (log.isDebugEnabled()) {
                log.debug("GetHref:(" + getHref + ") ended");
            }
            writer.write(getHref);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        return true;  
    }
}
