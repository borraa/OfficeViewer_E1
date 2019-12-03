package com.woonam.services;

import javax.jws.WebService;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@WebService
public interface SoapDetails {
	  SoapObject Run(SoapObject run);
//	  void test(@Context UriInfo uriInfo);
	}

