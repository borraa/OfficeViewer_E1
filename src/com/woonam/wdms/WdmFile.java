/*
 * Ver 1.0.1.2	2008.12.17
 * Ver 1.0.0.1	2008.11.27
 * Ver 0.0.0.1	2008.11.26	DBAgent Client
 * 
 */
package com.woonam.wdms;

import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;
import org.jdom.*;
import org.jdom.input.*;

import com.woonam.util.Common;
import com.woonam.util.Profile;

public class WdmFile {
	private String 	wdServer = null;
	private int		wdPort = 0;
	private char	wdOP = 'W';
	private String	wdRegKey = null;
	private String	wdDocTable = null;
	private Profile profile	= null;
	private String  strCharSet = null;
	private Common m_C = new Common();
	private static Logger logger = LogManager.getLogger("Woonam");
	
	public WdmFile(Profile profile){
		
		this.profile	=	profile;
		
		String strSvrMode	=	profile.getString("AGENT_INFO", "SERVER", "");
		String _RegKey		=	profile.getString(strSvrMode, "KEY", "");
		this.strCharSet		=	profile.getString("AGENT_INFO", "CHARSET", "EUC-KR");
				
		
		int _len = _RegKey.length();
		if ( _len > 32){
			return;
		}
		else{
			StringBuffer _regkey = new StringBuffer()
			.append(_RegKey);
			if (_len < 32){
				_regkey.append("                                ".toCharArray(), 0, 32 - _len);
			}
			this.wdRegKey = _regkey.toString();
		}
		this.wdServer 	= profile.getString(strSvrMode, "IP", "");
		this.wdPort 	= Integer.parseInt(profile.getString(strSvrMode, "PORT", ""));
		
	}
	
	public String GetXMLData(String strDocIRN)
	{
		String strRes 			= null;
		String strDocTable	= "IMG_SLIPDOC_X";
		
		byte[] bXMLData = 	Download(strDocIRN, "1", strDocTable);
		
		try
		{
			if(bXMLData != null)
			{
				String strCharSet = profile.getString("AGENT_INFO", "CHARSET", "utf-8");
				strRes = new String(bXMLData, strCharSet);
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			logger.error("GetXMLData", e);
			strRes = null;
		}
		
		return strRes;
	}
	
	public byte[] Download(String _wdDocId, String _DocTable)
	  {
		this.wdDocTable = _DocTable;
		String _wdOp = "download";
		String 	content = null;
		int 	contLen = 0;
		byte[] 	buf = null;
		
		if (this.wdServer.equals("") || wdPort == 0 || this.wdRegKey.equals("") || this.wdDocTable.equals("") || _wdDocId.equals(""))
		{
			return null;
		}
		
	    BufferedOutputStream out = null;
	    BufferedInputStream in = null;
	    
	    Socket socket = null;
	    
	    try
	    {
	      socket = new Socket( wdServer, wdPort );
	      out = new BufferedOutputStream( socket.getOutputStream() );
	      
	      out.write( stream (_wdOp, _wdDocId) );
	      out.flush();
	      
	      in = new BufferedInputStream( socket.getInputStream() );
	      buf = new byte[2];  
	      in.read( buf );
	      content  = new String(buf);
	      if (content.charAt(0) != 'W' || content.charAt(1) != 'T'){
	    	  return null;
	      }
	      
	      buf = new byte[12]; 
	      in.read( buf );
	      contLen = Integer.parseInt(new String(buf));
	      
	      buf = new byte[contLen];  
	      in.read( buf );
	      content  = new String(buf);

	      SAXBuilder sax= new SAXBuilder();
	      Document doc = sax.build(new InputSource(new StringReader(content)));

	      Element xelRoot = doc.getRootElement();
	      //Element xelListData = xelRoot.getChild("ListData");
	      
	      Attribute xatFileSize = xelRoot.getAttribute("FileSize");
	      
	      buf = new byte[xatFileSize.getIntValue()];  
	      //int cc = in.read( buf );
	      //System.out.println( "dcd : " + cc );
	      
	      byte[] _buf = new byte[1024];
		  int readlen = 0;
		  int pos = 0;
		  while( (readlen =in.read( _buf )) != -1 )
		  {
			  System.arraycopy(_buf, 0,  buf, pos, readlen);
			  pos += readlen;
		  }
		  
		  return buf;
	    }
	    catch( Exception e )
	    {
	      try{ out.close(); } catch( Exception se ){}
	      try{ in.close();  }catch( Exception se ){}
	      try{ socket.close(); } catch( Exception s ){}
	    }
	    return null;
	}
	
	public byte[] Download(String _wdDocIrn, String _wdDocNo, String _DocTable)
	  {
		this.wdDocTable = _DocTable;
		String _wdOp = "download";
		String 	content = null;
		int 	contLen = 0;
		boolean bSuccess = false;
		byte[] 	buf = null;
		
		if (this.wdServer.equals("") || wdPort == 0 || this.wdRegKey.equals("") || this.wdDocTable.equals("") || _wdDocIrn.equals("") || _wdDocNo.equals(""))
		{
			return null;
		}
		
	    BufferedOutputStream out = null;
	    BufferedInputStream in = null;
	    
	    Socket socket = null;
	    
	    try
	    {
	      socket = new Socket( wdServer, wdPort );
	      out = new BufferedOutputStream( socket.getOutputStream() );
	      
	      out.write( stream (_wdOp, _wdDocIrn, _wdDocNo) );
	      out.flush();
	      
	      in = new BufferedInputStream( socket.getInputStream() );
	      buf = new byte[2]; 
	      in.read( buf );
	      content  = new String(buf);
	      if (content.charAt(0) != 'W' || content.charAt(1) != 'T'){
	    	  return null;
	      }
	      
	      buf = new byte[12]; 
	      in.read( buf );
	      contLen = Integer.parseInt(new String(buf));
	      
	      buf = new byte[contLen];  
	      in.read( buf );
	      content  = new String(buf);
	      
//	      content	=	content.replaceAll("&", "&amp;");
//	      content	=	content.replaceAll("'", "&apos;");
//	      content	=	content.replaceAll("\"", "&quot;");
	      
	      SAXBuilder sax= new SAXBuilder();
	      Document doc = sax.build(new InputSource(new StringReader(content)));

	      Element xelRoot = doc.getRootElement();
	      //Element xelListData = xelRoot.getChild("ListData");
	      
	      Attribute xatFileSize = xelRoot.getAttribute("FileSize");
	      
	      buf = new byte[xatFileSize.getIntValue()];  
	      //int cc = in.read( buf );
	      //System.out.println( "dcd : " + cc );
	      
	      byte[] _buf = new byte[1024];
		  int readlen = 0;
		  int pos = 0;
		  while( (readlen =in.read( _buf )) != -1 )
		  {
			  System.arraycopy(_buf, 0,  buf, pos, readlen);
			  pos += readlen;
		  }
		  
		  bSuccess = true;

		  
	    }
	    catch( Exception e )
	    {
	     /* try{ out.close(); } catch( Exception se ){}
	      try{ in.close();  }catch( Exception se ){}
	      try{ socket.close(); } catch( Exception s ){}*/
	    }
	    finally
	    {
	        try{ out.close(); } catch( Exception se ){}
		    try{ in.close();  }catch( Exception se ){}
		    try{ socket.close(); } catch( Exception s ){}
	    }
	    
	    if(bSuccess)	return buf;
	    else		    return null;
	}
	
	public String Upload(String strFilePath, String _wdDocIrn, String _wdDocNo, String _DocTable)
	{
		
		this.wdDocTable 		= _DocTable;
		String _wdOp 			= "create";
		String 	content 		= null;
		int 	contLen 			= 0;
		String strRes 			= "F";
		byte[] 	buf 			= null;
		
		if (this.wdServer.equals("") || wdPort == 0 || this.wdRegKey.equals("") || this.wdDocTable.equals("") || _wdDocIrn.equals("") || _wdDocNo.equals(""))
		{
			return strRes;
		}
		
	    BufferedOutputStream out 	= null;
	    FileInputStream fis 				= null;
	    DataOutputStream dos 			= null;
	    BufferedInputStream bis 		= null;
	    
	    Socket socket = null;
		
	    try
	    {
	    	socket = new Socket( wdServer, wdPort );
	    	
		    int 	nFileIndex		=	strFilePath.lastIndexOf("\\");
		    if(nFileIndex <= 0)
		    {
		    	nFileIndex		=	strFilePath.lastIndexOf("/");
		    }
	      
		    String strFileName	=	strFilePath.substring(nFileIndex +1 );
		 		  
		    File upload_file	=	new File(strFilePath);
		  
		    byte[] bufFile	=	getBytesFromFile(upload_file);
		  
		    int 	 nFileSize	=	bufFile.length;
		  
		    if(socket != null)
		    {
		    	out = new BufferedOutputStream( socket.getOutputStream() );
		      
		      //out.write( stream (_wdOp, _wdDocIrn, _wdDocNo) );
		      byte[] bufUploadInfo	=	stream(_wdOp, _wdDocIrn, _wdDocNo, strFileName, nFileSize );
		      out.write(bufUploadInfo);
		      
		      fis = new FileInputStream( strFilePath );
		      bis = new BufferedInputStream( fis );

              int ch = 0;
              while((ch = bis.read()  ) != -1) 
              {
        		  out.write(ch);  
              }    
              
              out.flush();
	      
              bis 		= new BufferedInputStream( socket.getInputStream() );
		      buf 		= new byte[2]; 
		      
		      bis.read( buf );
		      content  	= new String(buf);
		      
		      if (content.charAt(0) != 'W' || content.charAt(1) != 'T')
		      {
		    	  strRes = "F";
		      }
		      else
		      {
		    	  strRes = "T";
		      }
		   }
	    }
	    catch( Exception e )
	    {
	    	logger.error("wdmAgent - Upload ", e);
	    	strRes = "N";
	    }
	    finally
	    {
	        try{ if(out!=null) 		out.close(); 				} 		catch( Exception se ){
	        	logger.error("wdmAgent - Upload ", se);}
		    try{ if(fis!=null) 			fis.close();  				}		catch( Exception se ){
				logger.error("wdmAgent - Upload ", se); }
		    try{ if(bis!=null) 			bis.close();  				}		catch( Exception se ){
				logger.error("wdmAgent - Upload ", se); }
		    try{ if(socket!=null) 	socket.close(); 	}		catch( Exception se ){
				logger.error("wdmAgent - Upload ", se); }
	    }
	    return strRes;
	}
	
	public String Delete(String strFilePath, String _wdDocIrn, String _wdDocNo, String _DocTable)
	{
		
		this.wdDocTable 		= _DocTable;
		String _wdOp 			= "delete";
		String 	content 		= null;
		int 	contLen 			= 0;
		String strRes 			= "F";
		byte[] 	buf 			= null;
		
		if (this.wdServer.equals("") || wdPort == 0 || this.wdRegKey.equals("") || this.wdDocTable.equals("") || _wdDocIrn.equals("") || _wdDocNo.equals(""))
		{
			return strRes;
		}
		
	    BufferedOutputStream out 	= null;
	    DataOutputStream dos 			= null;
	    BufferedInputStream bis 		= null;
	    
	    Socket socket = null;
		
	    try
	    {
	    	socket = new Socket( wdServer, wdPort );
	    	
		 /*   int 	nFileIndex		=	strFilePath.lastIndexOf("\\");
		    if(nFileIndex <= 0)
		    {
		    	nFileIndex		=	strFilePath.lastIndexOf("/");
		    }
	      
		    String strFileName	=	strFilePath.substring(nFileIndex +1 );
		 		  
		    File upload_file	=	new File(strFilePath);
		  
		    byte[] bufFile	=	getBytesFromFile(upload_file);
		  
		    int 	 nFileSize	=	bufFile.length;*/
		  
		    if(socket != null)
		    {
		    	out = new BufferedOutputStream( socket.getOutputStream() );
		      
		      //out.write( stream (_wdOp, _wdDocIrn, _wdDocNo) );
		      byte[] bufUploadInfo	=	stream(_wdOp, _wdDocIrn, _wdDocNo, null, -1 );
		      out.write(bufUploadInfo);
		      
		   /*   fis = new FileInputStream( strFilePath );
		      bis = new BufferedInputStream( fis );

              int ch = 0;
              while((ch = bis.read()  ) != -1) 
              {
        		  out.write(ch);  
              }    */
              
              out.flush();
	      
              bis 		= new BufferedInputStream( socket.getInputStream() );
		      buf 		= new byte[2]; 
		      
		      bis.read( buf );
		      content  	= new String(buf);
		      
		      if (content.charAt(0) != 'W' || content.charAt(1) != 'T')
		      {
		    	  strRes = "F";
		      }
		      else
		      {
		    	  strRes = "T";
		      }
		   }
	    }
	    catch( Exception e )
	    {
	    	logger.error("wdmAgent - Delete ", e);
	    	strRes = "N";
	    }
	    finally
	    {
	        try{ if(out!=null) 		out.close(); 				} 		catch( Exception se ){
	        	logger.error("wdmAgent - Delete ", se);}
		    try{ if(bis!=null) 			bis.close();  				}		catch( Exception se ){
				logger.error("wdmAgent - Delete ", se); }
		    try{ if(socket!=null) 	socket.close(); 	}		catch( Exception se ){
				logger.error("wdmAgent - Delete ", se); }
	    }
	    return strRes;
	}
	
	/*public boolean Upload(File UploadFile, String _wdDocIrn, String _wdDocNo, String _DocTable)
	  {
		this.wdDocTable = _DocTable;
		String _wdOp = "create";
		boolean bSuccess = false;
		
		if (this.wdServer.equals("") || wdPort == 0 || this.wdRegKey.equals("") || this.wdDocTable.equals("") || _wdDocIrn.equals("") || _wdDocNo.equals(""))
		{
			return false;
		}
		
	    BufferedOutputStream out = null;
	    BufferedInputStream in = null;
	    FileInputStream fis = null;
	    BufferedInputStream bis = null;
	    
	    Socket socket = null;
	    
	    try
	    {
	    	String strFileName	=	UploadFile.getName();
	        byte[] bufFile	=	getBytesFromFile(UploadFile);
	        int 	 nFileSize	=	bufFile.length;
	      
	      socket = new Socket( wdServer, wdPort );
	      out = new BufferedOutputStream( socket.getOutputStream() );
	      
	      //out.write( stream (_wdOp, _wdDocIrn, _wdDocNo) );
	      byte[] bufUploadInfo	=	stream(_wdOp, _wdDocIrn, _wdDocNo, strFileName, nFileSize );
	      //System.out.print(bufUploadInfo.toString());
	      out.write(bufUploadInfo);
	      
	      fis = new FileInputStream(UploadFile);
	      bis = new BufferedInputStream( fis );

	               int ch = 0;
	               while((ch = bis.read()  ) != -1) {
	            	   out.write(ch);        
	      }    
	      
         
	      out.flush();
	      
		  bSuccess = true;

		  
	    }
	    catch( Exception e )
	    {
	      try{ out.close(); } catch( Exception se ){}
	      try{ in.close();  }catch( Exception se ){}
	      try{ socket.close(); } catch( Exception s ){}
	    }
	    finally
	    {
	        try{ out.close(); } catch( Exception se ){}
		    try{ in.close();  }catch( Exception se ){}
		    try{ socket.close(); } catch( Exception s ){}
		    try {
				bis.close();
			} catch (IOException e) {			
				e.printStackTrace();
			}
	    }
	    
	    return bSuccess;
	}*/
	
	private byte[] stream(String _wdOp, String _wdDocId)
	{
		//WDPS5000                         000000000044op=download;table=GENDOC_T;doc_id=7234567896
		StringBuffer _msg = new StringBuffer()
		.append("op=")
		.append(_wdOp)
		.append(";table=")
		.append(wdDocTable)
		.append(";doc_id=") 
		.append(_wdDocId);
		System.out.println(_msg);
		
		String strMsg	=	_msg.toString();
		int Querylength	=	0;
		try {
			Querylength = strMsg.getBytes(strCharSet).length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Cm.Getlength(strQuery);
		
		StringBuffer buff = new StringBuffer()
	    .append( wdOP )
	    .append( wdRegKey )
	    .append((new DecimalFormat("000000000000")).format(Querylength))
	    .append(_msg);
		//System.out.println(buff);
		
	    return buff.toString().getBytes();
	}
	
	private byte[] stream(String _wdOp, String _wdDocIrn, String _wdDocNo)
	{
		//WDPS5000                         000000000054op=download;table=GENDOC_T;doc_no=0;doc_irn=7234567896
		StringBuffer _msg = new StringBuffer()
		.append("op=")
		.append(_wdOp)
		.append(";table=")
		.append(wdDocTable)
		.append(";doc_no=")
		.append(_wdDocNo)
		.append(";doc_irn=")
		.append(_wdDocIrn);
		//System.out.println(_msg);
		
		StringBuffer buff = new StringBuffer()
	    .append( wdOP )
	    .append( wdRegKey )
	    .append((new DecimalFormat("000000000000")).format(_msg.length()))
	    .append(_msg);
		//System.out.println(buff);
		
	    return buff.toString().getBytes();
	}
	
	private byte[] stream(String _wdOp, String _wdDocIrn, String _wdDocNo, String _wdFileName, int _wdFileSize ) throws UnsupportedEncodingException
	{
		//WDPS5000                         000000000054op=download;table=GENDOC_T;doc_no=0;doc_irn=7234567896
		//W<--FileName------ ------------->000000000080op=create;table=GENDOC_T;doc_no=0;doc_irn=5234567895;filename=xxx;filesize=26;&&ABCDEFGHIJKLMNOPQRSTUVqqqq
		StringBuffer _msg = new StringBuffer()
		.append("op=")
		.append(_wdOp)
		.append(";table=")
		.append(wdDocTable)
		.append(";doc_no=")
		.append(_wdDocNo)
		.append(";doc_irn=")
		.append(_wdDocIrn);
		if(!m_C.isBlank(_wdFileName))
		{
			_msg.append(";filename=");
			_msg.append(_wdFileName);
		}
		if(_wdFileSize > -1)
		{
			_msg.append(";filesize=");
			_msg.append(_wdFileSize);
		}
		_msg.append(";&&");
		//.append(_wdFile);
		//System.out.println(_msg);
		
		String strMsg	=	_msg.toString();
		int Querylength	=	0;
		try {
			Querylength = strMsg.getBytes(strCharSet).length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//Cm.Getlength(strQuery);
		
		
		
		StringBuffer buff = new StringBuffer()
	    .append( wdOP )
	    .append( wdRegKey )
	    .append((new DecimalFormat("000000000000")).format(Querylength))
	    .append(_msg);
		//System.out.println(buff);
		
	    return buff.toString().getBytes("euc-kr");
	}
	
	public static byte[] getBytesFromFile(File file) throws IOException 
	{
		InputStream is = new FileInputStream(file);
	    	
	       long length = file.length();
	    
	        if (length > Integer.MAX_VALUE) {
	        
	        }
	    
	        byte[] bytes = new byte[(int)length];
	        
	        int offset = 0;
	        int numRead = 0;
	        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) 
	        {
	            offset += numRead;
	        }
	    
	        if (offset < bytes.length) 
	        {
	            throw new IOException("Could not completely read file "+file.getName());
	        }
	    
	        is.close();
	        return bytes;
	}

	
}

