package com.woonam.template;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.woonam.util.Common;
import com.woonam.util.Profile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Report {

	private Profile m_Profile = null;
	private Common m_C = new Common();
	private static Logger logger = LogManager.getLogger("Woonam");
	
	
	public Report(Profile profile) {
		this.m_Profile = profile;
	}
	
	public JsonObject run(JsonObject obj_data, String workPath) {
		
		String fileImg = obj_data.get("FileImg").getAsString();
		byte[] imgByte =m_C.hexStringToByteArray(fileImg);//new BigInteger(fileImg, 16).toByteArray();
		
		String sdocName = obj_data.get("SDOC_NAME").getAsString();
		String fileName = m_C.getIRN("");
		
		JsonObject obj_res = new JsonObject();
		obj_res.addProperty("CONVERT_KEY", obj_data.get("ConvertKey").getAsString());
		obj_res.addProperty("SDOC_KIND", obj_data.get("SDocKind").getAsString());
		obj_res.addProperty("SDOC_NAME", sdocName);
		
		//IMG_INFO
		File path = new File(workPath);
		if(!path.exists()) {
			path.mkdirs();
		}
		String absoluteFilePath = workPath+ File.separator + fileName+".JPG";
		try (FileOutputStream fos = new FileOutputStream(absoluteFilePath)) {
			   fos.write(imgByte);
			   
			   BufferedImage bformImage = ImageIO.read(new File(absoluteFilePath));
			   int width = bformImage.getWidth();//== null ? 0 : bformImage.getWidth();
			   int height = bformImage.getHeight();// == null ? 0 : bformImage.getHeight();
			   
			   JsonObject obj_imgData = new JsonObject();
			   obj_imgData.addProperty("DOC_IRN", fileName);
			   obj_imgData.addProperty("PATH", absoluteFilePath);
			   obj_imgData.addProperty("WIDTH", width);
			   obj_imgData.addProperty("HEIGHT", height);
			   obj_imgData.addProperty("NAME", "");
			   
			   File file = new File(absoluteFilePath);
			   long lFileSize = file.length();
//			   obj_imgData.addProperty("SIZE", file.length());
			   obj_imgData.addProperty("SIZE", lFileSize / 1024);
			   
			   JsonArray ar_data = new JsonArray();
			   ar_data.add(obj_imgData);
			   obj_res.add("IMG_INFO", ar_data);
		}
		catch(Exception e) {
			logger.error("Upload template - Failed to convert to file", e);
			
			return null;
		}
		return obj_res;
	}
}
