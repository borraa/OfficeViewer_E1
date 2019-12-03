package com.woonam.filter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woonam.util.Common;
import com.woonam.util.Profile;
import com.woonam.util.RequestWrapper;

@WebFilter(urlPatterns = {"/*"})
public class CrossScriptingFilter implements Filter {
	 
	private FilterConfig filterConfig = null;
	private Common m_C = null;
	Profile m_Profile			= null;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
 
    public void destroy() {
        this.filterConfig = null;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
 
    	this.m_C 		= new Common();
		this.m_Profile 	= new Profile(m_C.getRootPath(request.getServletContext()) + "/conf/conf.ini");
		
        HttpServletRequest req 		= (HttpServletRequest) request;
        HttpServletResponse res 	= (HttpServletResponse) response;
        
        request.getParameterMap();
        
        res.setHeader("Access-Control-Allow-Methods", "GET, OPTIONS, POST");
        res.setHeader("Access-Control-Max-Age", "0");
        res.setHeader("Access-Control-Allow-Headers", "origin, x-requested-with, content-type, accept");
  
        HashMap<String, String > mapWhiteList = m_Profile.GetAllSectionValues("CORS_WHITELIST");
        
        if("T".equalsIgnoreCase(mapWhiteList.get("ALLOW_ALL")))
        {
        	res.addHeader("Access-Control-Allow-Origin","*");
        }
        
        else
        {
	        String strOrigin = req.getHeader("origin");
	            
		    if(!m_C.isBlank(strOrigin))
		    {
		          for(String strKey : mapWhiteList.keySet())
		        {
		        	  String strWhiteListURL = mapWhiteList.get(strKey);
		        	  if(strOrigin.equalsIgnoreCase(strWhiteListURL))
		        	  {
		        		  res.addHeader("Access-Control-Allow-Origin",strOrigin);
		        	  }  
		        }
		    }
        }
    	
        chain.doFilter(new RequestWrapper((HttpServletRequest) req), res);
 
    }

 
}

