package com.buzzinate.buzzads.sso;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 
 * @author zyeming
 *
 */
public class CustomAccessDeniedHandler extends AccessDeniedHandlerImpl {
    
    private String errorPage;
    
    
    public void handle(HttpServletRequest request, HttpServletResponse response, 
                    AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        if (!response.isCommitted()) {
            if (errorPage != null) {
                // Put exception into request scope (perhaps of use to a view)
                request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);

                // Set the 403 status code.
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                // redirect to error page.
                response.sendRedirect(request.getContextPath() + errorPage);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
            }
        }
    }
    
    
    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

}
