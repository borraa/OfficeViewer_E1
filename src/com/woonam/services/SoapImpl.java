package com.woonam.services;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@WebService(endpointInterface = "com.woonam.services.SoapDetails")
public class SoapImpl implements SoapDetails{
	
	private API api = null;
	
	public SoapImpl() {
		this.api = new API();
	}
	
	public SoapObject Run(SoapObject run) {
		
		Map params = run.getParams();
	
		JsonObject objRes = api.Run_WebService(params);
		Map map = (Map<String,Object>)(new Gson()).fromJson(objRes, (new HashMap<String,Object>()).getClass());
		
		run.setParams(map);
		
	    return run;
    }
}
