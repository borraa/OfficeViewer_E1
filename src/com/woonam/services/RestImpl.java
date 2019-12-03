package com.woonam.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Consumes("application/json")
@Produces("application/json")
public class RestImpl {

	private API api = null;
	
	public RestImpl() {
		this.api = new API();
	}
	
	@POST
	  @Path("/Run")
	  public Response postRun(@Context UriInfo uriInfo) {
		MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		
		return Result_Proc(queryParams);
	  }

	  @GET
	  @Path("/Run")
	  public Response getRun(@Context UriInfo uriInfo) {
		  MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
		  
		  return Result_Proc(queryParams);
	  }
	  
	  private Response Result_Proc(Map params) {
			
		  JsonObject objRes = api.Run_WebService(convert(params));
			
		  return Response.ok(objRes.toString()).build();
	  }
	  
	  public Map<String, String[]> convert(Map<String, ArrayList<?>> oldMap) {
	        Map<String, String[]> ret = new HashMap<String, String[]>();
	        for (String key : oldMap.keySet()) {
	            ret.put(key, oldMap.get(key).toArray(new String[0]));
	        }
	        return ret;
	    }
}
