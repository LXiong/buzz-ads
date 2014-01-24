package com.buzzinate.buzzads.sso;


import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Customized authentication success handler
 * @author Johnson
 *
 */

public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    
    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        String url = super.determineTargetUrl(request, response);
        String dynamicAction = request.getParameter("dynamicAction");
        if (dynamicAction != null && !dynamicAction.equals("")) {
            url = "/loginAction?dynamicAction=" + dynamicAction;
        }

        return url;
    }
    

}
