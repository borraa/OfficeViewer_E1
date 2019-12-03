package com.woonam.connect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;



public class SocketConnect
{
	public Socket socket=null;
	ObjectInputStream ininmsgeam = null;
	public OutputStream os = null;
	public OutputStreamWriter osw = null;
	private BufferedReader reader = null;
	private String msg;
	private String inmsg;
	private static Logger logger = LogManager.getLogger("Woonam");
	 
	public String request(String ip, int port, String outputstream, String ResultFlag, String strCharset) 
	{
		try 
		{
			msg = outputstream;
			socket	=	new Socket(ip, port);
				
			os = socket.getOutputStream();
			osw = new OutputStreamWriter(os,strCharset);
				
			osw.write(msg);
			osw.flush();
				
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),strCharset));
	            
			inmsg = (String) reader.readLine();
					
			 int ResultCnt =  Integer.parseInt(inmsg.substring(2,14));
				
			 if(!ResultFlag.equals("full"))			inmsg = inmsg.substring(14,inmsg.length());		
				
		} 
		catch (Exception e) 
		{
			logger.error("request Exception", e);
		}
		finally
		{
			try 
			{
				reader.close();
				socket.close();
				os.close();
				osw.close();
			} 
			catch (Exception e) 
			{
				logger.error("request Exception", e);
			}
		}
		return inmsg;
	}
}
