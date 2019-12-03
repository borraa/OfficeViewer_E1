package com.woonam.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

import com.sun.xml.txw2.annotation.XmlElement;

//@XmlElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SoapObject {

	private Map<String, String[]> params = new HashMap<String, String[]>();

	
	
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	
	  public  Map<String, String[]> getParams()  {
		  return params;
	  }
}
