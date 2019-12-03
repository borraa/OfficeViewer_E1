package com.woonam.connect;
 
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class AgentParser
{
	private InputSource is 			=	null;
	private Document document 	=	null;
	private XPath xpath 			=	null;
	private static Logger logger = LogManager.getLogger("Woonam");
	
	public AgentParser(String xml)
	{
		try
		{ 
		
			// XML Document 객체 생성
	        is = new InputSource(new StringReader(xml));
	        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
	 
	        // 인터넷 상의 XML 문서는 요렇게 생성하면 편리함.
	        //Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder()
	        //                               .parse("http://www.example.com/test.xml");
	         
	        // xpath 생성
	        xpath = XPathFactory.newInstance().newXPath();
		}
		catch(Exception e)
		{
			logger.error("AgentParser Exception", e);
		}
	}
        
	public int GetNodeLength(String Nodepath)
	{
		NodeList cols = null;
		
		try
		{
			// NodeList 가져오기 : row 아래에 있는 모든 col1 을 선택
			cols = (NodeList)xpath.evaluate(Nodepath, document, XPathConstants.NODESET);
		}
		catch(Exception e)
		{
			logger.error("GetNodeLength Exception", e);
		}
		
		return cols.getLength();
	}
	
	public String[][] GetSelectList(String Nodepath)	//ex) "row/col1"
	{
		NodeList cols 	= null; 
		int ncols			=	0;
		int nCcols		=	0;
		
		try
		{
			cols = (NodeList)xpath.evaluate(Nodepath, document, XPathConstants.NODESET);
			ncols 	= cols.getLength();
			nCcols 	= cols.item(0).getChildNodes().getLength();
		}
		catch(Exception e)
		{
		//	Logger.writeException("GetSelectList Exception", e, 1);
		}		
		
		String[][] arrNodeList = new String[ncols][nCcols];
		
		try
		{
			// NodeList 가져오기 : row 아래에 있는 모든 col1 을 선택
			
			for( int idx=0; idx<cols.getLength(); idx++ )
			{	
				NodeList Ccols = cols.item(idx).getChildNodes();
				
				for( int i=0; i<Ccols.getLength(); i++ )
				{	
					arrNodeList[idx][i] =  Ccols.item(i).getNodeName() + "=" + Ccols.item(i).getTextContent();
				}
			}
		}
		catch(Exception e)
		{
			logger.error("GetSelectList Exception", e);
		}
		// 값1   값3  이 출력됨
		return arrNodeList;
	}
	 
	public String getNodeList(String Nodepath)	//ex) "row/col1"
	{
		String arrNodeList = ""; 
		try
		{
			// NodeList 가져오기 : row 아래에 있는 모든 col1 을 선택
			NodeList cols = (NodeList)xpath.evaluate(Nodepath, document, XPathConstants.NODESET);
			
			for( int idx=0; idx<cols.getLength(); idx++ )
			{	
				arrNodeList = arrNodeList + cols.item(idx).getTextContent();
			}
		}
		catch(Exception e)
		{
			logger.error("getNodeList Exception", e);
		}
		// 값1   값3  이 출력됨
		return arrNodeList;
	}
	 
	public String getAttribute(String Nodepath, String attribute)	//ex) "//*[@id='c2']",  "val"
	{
		String ret = "";
		try
		{
			// id 가 c2 인 Node의 val attribute 값 가져오기
			Node col2 = (Node)xpath.evaluate(Nodepath, document, XPathConstants.NODE);
			ret = col2.getAttributes().getNamedItem(attribute).getTextContent();
			// val2 출력
		}
		catch(Exception e)
		{
			logger.error("getAttribute Exception", e);
		}
		
		return ret;
	} 
	 
	public String getValue(String NodePath)	// ex) "//*[@id='c3']"
	{
		String ret = "";
		try
		{
			// id 가 c3 인 Node 의 value 값 가져오기
			ret = (String) xpath.evaluate(NodePath, document, XPathConstants.STRING);
			// 값3 출력
		}
		catch(Exception e)
		{
			logger.error("getValue Exception", e);
		}
		
		return ret;
	}
}
