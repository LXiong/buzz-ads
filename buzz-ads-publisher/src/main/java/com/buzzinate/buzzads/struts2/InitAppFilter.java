package com.buzzinate.buzzads.struts2;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter;

import com.buzzinate.buzzads.core.util.ConfigurationReader;
import com.buzzinate.buzzads.util.SessionKeys;

/**
 * 
 * @author zyeming
 *
 */
public class InitAppFilter extends StrutsPrepareAndExecuteFilter {


    public void init(FilterConfig filterConfig) throws ServletException {
        
        super.init(filterConfig);

        // Get host for static content
        String mainHost = ConfigurationReader.getString("buzzads.publisher.server");
        if (mainHost.endsWith("/")) {
            mainHost = mainHost.substring(0, mainHost.length() - 1);
        }
        
        String authHost = ConfigurationReader.getString("auth.server");
        if (authHost.endsWith("/")) {
            authHost = authHost.substring(0, authHost.length() - 1);
        }
        
        String lezhiHost = ConfigurationReader.getString("lezhi.server");
        if (lezhiHost.endsWith("/")) {
        	lezhiHost = authHost.substring(0, authHost.length() - 1);
        }
        
        String staticHost = ConfigurationReader.getString("static.path");
        if (staticHost.endsWith("/")) {
        	staticHost = authHost.substring(0, authHost.length() - 1);
        }
        
        
        // Get context, useful for dev/test enviroment
        ServletContext ctx = filterConfig.getServletContext();
        String ctxPath = ctx.getContextPath();
        if (ctxPath.equals("/")) {
            ctxPath = "";
        }
        
        
        ctx.setAttribute(SessionKeys.CTX_PATH, ctxPath);
        ctx.setAttribute(SessionKeys.MAIN_CTX_PATH, mainHost);
        ctx.setAttribute(SessionKeys.ONE_CTX_PATH, authHost);
        ctx.setAttribute(SessionKeys.LEZHI_CTX_PATH, lezhiHost);
        
        ctx.setAttribute(SessionKeys.STATIC_CTX_PATH, staticHost);
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        super.doFilter(req, res, chain);
    }

    public void destroy() {
        super.destroy();
    }
}
