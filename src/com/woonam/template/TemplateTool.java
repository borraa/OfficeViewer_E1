package com.woonam.template;

import com.woonam.image.Office3GL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class TemplateTool {
	
	//수직정렬
	public int VTOP	 	= 0;
	public int VCENTER 	= 1;
	public int VBOTTOM 	= 2;
	
	//수평정렬
	public int LEFT	 	= 0;
	public int CENTER	 	= 1;
	public int RIGHT	 	= 2;
	
	//font style
	public int NORMAL		=	0;
	public int BOLD		=	1;
	public int ITALIC		=	2;
	
	//IMAGE_TYPE
	public String IMAGE_JPEG	=	"JPEG";
	public String IMAGE_JPG		=	"JPG";
	public String IMAGE_BMP		=	"BMP";
	public String IMAGE_PNG		=	"PNG";
	
	
	private Graphics2D 	m_grapchics 	= 	null;
	private BufferedImage	m_bBackgroud	=	null;
	  
    private Font 			m_Font  		= 	null;
	
	private int			m_nFontSize	=	20;

	private static Logger logger = LogManager.getLogger("Woonam");
	
	public TemplateTool(String strFilePath)
	{
		setBackgroudImage(strFilePath);
	}
	
	public TemplateTool(BufferedImage bformImage)
	{
		setBackgroudImage(bformImage);
	}
	public TemplateTool(BufferedImage bformImage, int nHeight)
	{
		setBackgroudImage(bformImage, nHeight);
	}
	
	public TemplateTool(String strFilePath, int nHeight)
	{
		setBackgroudImage(strFilePath, nHeight);
	}
	
	public void setBackgroudImage(BufferedImage bformImage)
	{
		try 
		{
			int nWidth	=	bformImage.getWidth();
			int nHeight	=	bformImage.getHeight();
			
			m_bBackgroud 	= 	new BufferedImage( nWidth, nHeight, BufferedImage.TYPE_3BYTE_BGR);
			m_grapchics 		= 	m_bBackgroud.createGraphics();  
			m_grapchics.drawImage( bformImage, null, 0, 0 );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setBackgroudImage(BufferedImage bformImage, int nHeight)
	{
		try 
		{
			int nWidth	=	bformImage.getWidth();
	//		int nHeight	=	bformImage.getHeight();
			
			m_bBackgroud 	= 	new BufferedImage( nWidth, nHeight, BufferedImage.TYPE_3BYTE_BGR);
			m_grapchics 		= 	m_bBackgroud.createGraphics();  
			m_grapchics.drawImage( bformImage, null, 0, 0 );
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean setBackgroudImage(String strFilePath)
	{
		boolean bRes = false;
		try 
		{
			BufferedImage bformImage = ImageIO.read(new File(strFilePath));
			
			int nWidth	=	bformImage.getWidth();
			int nHeight	=	bformImage.getHeight();
			
			m_bBackgroud 		= 	new BufferedImage( nWidth, nHeight, BufferedImage.TYPE_3BYTE_BGR);
			m_grapchics 		= 	m_bBackgroud.createGraphics();  
			m_grapchics.drawImage( bformImage, null, 0, 0 );
			
			m_grapchics.setColor(Color.BLACK);
			m_Font  		= new Font("맑은 고딕", Font.BOLD, 20);
			
			bRes = true;
			
		} catch (Exception e) {
			logger.error("TempleteTool - setBackroundImage", e);
			bRes = false;
		}
		return bRes;
	}
	
	private boolean setBackgroudImage(String strFilePath, int nHeight)
	{
		boolean bRes = false;
		try 
		{
			BufferedImage bformImage = ImageIO.read(new File(strFilePath));
			
			int nWidth	=	bformImage.getWidth();
	//		int nHeight	=	bformImage.getHeight();
			
			m_bBackgroud 		= 	new BufferedImage( nWidth, nHeight, BufferedImage.TYPE_3BYTE_BGR);
			m_grapchics 		= 	m_bBackgroud.createGraphics();  
			m_grapchics.drawImage( bformImage, null, 0, 0 );
			
			m_grapchics.setColor(Color.BLACK);
			m_Font  		= new Font("맑은 고딕", Font.BOLD, 20);
			
			bRes = true;
			
		} catch (Exception e) {
			logger.error("TempleteTool - setBackroundImage", e);
			bRes = false;
		}
		return bRes;
	}
	
	public void setFontStyle(String strFontName, int nFontStyle, int nSize)
	{
		
		if( this.BOLD == nFontStyle)
		{
			this.m_Font	=	new Font(strFontName, Font.BOLD, nSize);
			m_grapchics.setFont( m_Font );
		}
		else if (this.ITALIC == nFontStyle)
		{
			this.m_Font	=	new Font(strFontName, Font.ITALIC, nSize);
			m_grapchics.setFont( m_Font );
		}
		else
		{
			this.m_Font	=	new Font(strFontName, Font.PLAIN, nSize);
			m_grapchics.setFont( m_Font );
		}
		
		
	}
	
	public void setFontColor(Color color) 
	{
		m_grapchics.setColor(color);
	}
	
	public void DrawText(String strDrawText, int nX, int nY) 
	{		
	    FontRenderContext 	frc =  	new FontRenderContext(null, true, true);
	    Rectangle2D 		r2D = 	this.m_Font.getStringBounds(strDrawText, frc);
	    
	    int rY 			= (int) Math.round(r2D.getY());
	    
	    m_grapchics.setFont(m_Font);
	   
  		m_grapchics.drawString(strDrawText, nX, nY - rY);
  	
	}
	
	public void DrawLine(int nX, int nY, int nWidth, int nHeight)
	{	
		
		 m_grapchics.setColor(new Color(170,170,170));
		m_grapchics.drawRect(nX, nY, nWidth, nHeight);
		if(nHeight > 1)
		{
			m_grapchics.fillRect(nX, nY, nWidth, nHeight);
		}
		m_grapchics.setColor(new Color(0,0,0));
	}
	
	public int getBackgroundWidth()
	{
		return m_bBackgroud.getWidth();
	}
	
	public int getBackgroundHeight()
	{
		return m_bBackgroud.getHeight();
	}
	
	public boolean insertImage(Image image, int nX, int nY, int nWidth, int nHeight)
	{
		return m_grapchics.drawImage(image, nX, nY, nWidth, nHeight, null);
	}
	
	
	public int DrawSplittedText(String strText, int nLeft, int nTop, int nRight, int nBottom,  int nMaxWidth, int nPadding, int nAlign) 
	{	
		int nHeight = nTop;
		if(strText == null)
		{
			strText = "";
		}
		int nTextWidth = getTextWidth(strText);
		
		if(nMaxWidth < nTextWidth)
		{
			StringBuffer sbCurLine = new StringBuffer();
			for(int nTextIndex = 0; nTextIndex < strText.length() ; nTextIndex++)
			{
				if(getTextWidth(sbCurLine.toString()) < nMaxWidth - 50)
				{
					sbCurLine.append(strText.charAt(nTextIndex));
				}
				else
				{
					sbCurLine.append("...");
					DrawText(sbCurLine.toString(), nLeft, nHeight, nRight, nHeight + getTextHeight("")  , nAlign);
					nHeight += getTextHeight("") + nPadding;
					
					break;
			//		sbCurLine.setLength(0);
				}
				
				if(nTextIndex >= strText.length() - 1)
				{
					DrawText(sbCurLine.toString(), nLeft, nHeight, nRight, nHeight + getTextHeight("") , nAlign);
					nHeight += getTextHeight("") + nPadding;
					sbCurLine.setLength(0);
				}
			}
		}
		else
		{
			DrawText(strText, nLeft, nTop, nRight, nBottom ,nAlign);
		}
		
		return nHeight;
	}
	
	public int DrawMultiLineText(String strText, int nLeft, int nTop, int nRight, int nBottom,  int nMaxWidth, int nPadding, int nAlign) 
	{	
		int nHeight = nTop;
		int nTextWidth = getTextWidth(strText);
		
		if(nMaxWidth < nTextWidth)
		{
			StringBuffer sbCurLine = new StringBuffer();
			for(int nTextIndex = 0; nTextIndex < strText.length() ; nTextIndex++)
			{
				if(getTextWidth(sbCurLine.toString()) < nMaxWidth)
				{
					sbCurLine.append(strText.charAt(nTextIndex));
				}
				else
				{
					DrawText(sbCurLine.toString(), nLeft, nHeight, nRight, nHeight + getTextHeight("")  , nAlign);
					nHeight += getTextHeight("") + nPadding;
					sbCurLine.setLength(0);
				}
				
				if(nTextIndex >= strText.length() - 1)
				{
					DrawText(sbCurLine.toString(), nLeft, nHeight, nRight, nHeight + getTextHeight("") , nAlign);
					nHeight += getTextHeight("") + nPadding;
					sbCurLine.setLength(0);
				}
			}
		}
		else
		{
			DrawText(strText, nLeft, nTop, nRight, nBottom ,nAlign);
		}
		
		return nHeight;
	}
	
	public int DrawMultiLineText(String strText, int nLeft, int nTop, int nRight, int nBottom,  int nMaxWidth, int nPadding, int nMaxLine, int nAlign) 
	{	
		if(strText == null) return 0;
		int nHeight = nTop;
		int nTextWidth = getTextWidth(strText);
		
		if(nMaxWidth < nTextWidth)
		{
			int nLineWidth = nTextWidth / nMaxLine;
			int nCurLine = 0;
			StringBuffer sbCurLine = new StringBuffer();
			for(int nTextIndex = 0; nTextIndex < strText.length() ; nTextIndex++)
			{
				if(getTextWidth(sbCurLine.toString()) < nMaxWidth)
				{
					sbCurLine.append(strText.charAt(nTextIndex));
					if(nTextIndex  >= strText.length() - 1)
					{
						DrawText(sbCurLine.toString(), nLeft, nHeight );
						break;
					}
				/**/
				}
				else
				{
					if(nCurLine < nMaxLine -1)
					{
						DrawText(sbCurLine.toString(), nLeft, nHeight, nRight, nHeight + getTextHeight("")  , LEFT);
						nHeight += getTextHeight("") + nPadding;
						sbCurLine.setLength(0);
						sbCurLine.append(strText.charAt(nTextIndex));
						nCurLine++;
					}
					else
					{
				//		int nCurWidth = getTextWidth(sbCurLine.toString());
						DrawText(sbCurLine.toString() + strText.substring(nTextIndex, strText.length()), nLeft, nHeight );
						break;
					}
					/*if(nCurLine >= nMaxLine -1)
					{
						DrawText(sbCurLine.toString()+"..", nLeft, nHeight, nRight, nHeight + getTextHeight("")  , nAlign);
						nHeight += getTextHeight("") + nPadding;
						break;
					}
					else
					{
						DrawText(sbCurLine.toString(), nLeft, nHeight, nRight, nHeight + getTextHeight("")  , nAlign);
						nHeight += getTextHeight("") + nPadding;
						sbCurLine.setLength(0);
					}
					
					nCurLine++;*/
				}
				
			/*	if(nTextIndex  >= strText.length() - 1)
				{
					DrawText(sbCurLine.toString() + strText.substring(nTextIndex, strText.length()), nLeft, nHeight );
				}*/
			}
			if(sbCurLine.length() > 0)
			DrawText(sbCurLine.toString(), nLeft, nHeight, nRight, nHeight + getTextHeight("")  , LEFT);
			
		}
		else
		{
			DrawText(strText, nLeft, nTop, nRight, nBottom ,nAlign);
		}
		
		return nHeight;
	}
	
	public int Get_TextHeight(String strText, int nMaxWidth) {
		int nTextWidth = getTextWidth(strText);
		int nRes = getTextHeight("");
		if(nMaxWidth < nTextWidth)
		{
			StringBuffer sbCur = new StringBuffer();
			for(int nTextIndex = 0; nTextIndex < strText.length() ; nTextIndex++)
			{
				sbCur.append(strText.charAt(nTextIndex));
				
				if(getTextWidth(sbCur.toString()) >= nMaxWidth)
				{
					nRes += getTextHeight("");
					sbCur.setLength(0);
				}
				if(sbCur.toString().contains("\n")) {
					nRes += getTextHeight("");
					sbCur.setLength(0);
				}
			}
		}
		
//		if(nRes == 0) {
//			nRes = getTextHeight("");
//		}
		return nRes;
	}
	
	public int Draw_MultiLineText(String strText, int nLeft, int nTop, int nRight, int nBottom,  int nMaxWidth, int sectionHeight, int nAlign) 
	{	
		int drawnHeight = 0;
		int nTextWidth = getTextWidth(strText);
		int stringHeight = getTextHeight("");
		
		if(nMaxWidth < nTextWidth)
		{
			ArrayList<String> listStr = new ArrayList<>();
			
			StringBuffer sbCur = new StringBuffer();
			for(int nTextIndex = 0; nTextIndex < strText.length() ; nTextIndex++)
			{
				sbCur.append(strText.charAt(nTextIndex));
				
				String text = sbCur.toString();
				if(getTextWidth(text) >= nMaxWidth)
				{
					listStr.add(text);
//					DrawText(text, nLeft, nTop + drawnHeight);
					sbCur.setLength(0);
					drawnHeight += stringHeight;
				}
				
				if(text.contains("\n")) {
					listStr.add(text);
//					DrawText(text, nLeft, nTop + drawnHeight);
					sbCur.setLength(0);
					drawnHeight += stringHeight;
				}
//				
				if(nTextIndex  >= strText.length() - 1) {
					listStr.add(text);
//					DrawText(text, nLeft, nTop + drawnHeight);
					
					drawnHeight += stringHeight;
				}
			}
			
			int topPadding = (sectionHeight - drawnHeight) / 2;
			if(topPadding < 0) topPadding = 0;
			
			for(int i = 0; i < listStr.size(); i++) {
				DrawText(listStr.get(i), nLeft, nTop + (i * stringHeight) + topPadding);
			}
			
//			int nLine = listStr.size();
//			int textHeight = getTextHeight("");
//			
//			int topPadding = ((nBottom - nTop) - (nLine * textHeight)) / 2;
//			
//			
//			int resTop = nTop - topPadding * 2;
//			
//			for(int i = 0; i < listStr.size(); i++) {
//				//sbRes.append(listStr.get(i));
//				
//				DrawText(listStr.get(i), nLeft, resTop + (i * (textHeight * 2)), nRight, nBottom ,nAlign);
//			}
		}
		else
		{
			DrawText(strText, nLeft, nTop, nRight, nBottom ,nAlign);
			return nBottom;
		}
		
		return nTop + drawnHeight;
	}
	
	public void DrawText(String strDrawText, int nLeft, int nTop, int nRight, int nBottom, int nAlign) 
	{
		Rectangle r	=	new Rectangle();
		r.x			=	nLeft;
		r.y			=	nTop;
		r.width 	= 	nRight - nLeft;
		r.height 	=	nBottom - nTop;
		
		if(strDrawText == null)
		{
			strDrawText = "";
		}
		
	    FontRenderContext 	frc =  	new FontRenderContext(null, true, true);
	    Rectangle2D 		r2D = 	this.m_Font.getStringBounds(strDrawText, frc);
	    
	    int rHeight 		= (int) Math.round(r2D.getHeight());

	    m_grapchics.setFont(m_Font);
	    
	    String[] arrDrawText	=	strDrawText.split("\n"); //getCalAreaText(rWidth, r.width, strDrawText);
	    int		 nLineCnt		=	arrDrawText.length;
	    
	    //한줄일 때
	    if(nLineCnt == 1)
	    {
	    	DrawString(arrDrawText[0], r,  nAlign, this.VCENTER);
	    }
	    //멀티라인일 때
	    else
	    {
	    	int ry		=	r.y;
	    	
	    	int topPadding = (r.height - (rHeight * nLineCnt)) / 2;
	    	ry -= topPadding;
	    			
	    	for(int i=0 ; i<nLineCnt; i++)
	    	{
	    		r.y	=	ry + (i*rHeight);
	    		
	    		DrawString(arrDrawText[i], r,  nAlign, this.VCENTER);
	    	}
	    	
	    }
	}
	
	public void DrawText(String strDrawText, int nLeft, int nTop, int nRight, int nBottom, int nAlign, int nValign) 
	{
		Rectangle r	=	new Rectangle();
		r.x			=	nLeft;
		r.y			=	nTop;
		r.width 	= 	nRight - nLeft;
		r.height 	=	nBottom - nTop;
		
	    FontRenderContext 	frc =  	new FontRenderContext(null, true, true);
	    Rectangle2D 		r2D = 	this.m_Font.getStringBounds(strDrawText, frc);
	    
	    int rWidth 			= (int) Math.round(r2D.getWidth());
	    int rHeight 		= (int) Math.round(r2D.getHeight());

	    m_grapchics.setFont(m_Font);
	    
	    String[] arrDrawText	=	getCalAreaText(rWidth, r.width, strDrawText);
	    int		 nLineCnt		=	arrDrawText.length;
	    
	    //한줄일 때
	    if(nLineCnt == 1)
	    {
	    	DrawString(arrDrawText[0], r,  nAlign, nValign);
	    }
	    //멀티라인일 때
	    else
	    {
	    	int ry		=	r.y;
	    	
	    	for(int i=0 ; i<nLineCnt; i++)
	    	{
	    		r.y	=	ry + (i*rHeight);
	    		
	    		DrawString(arrDrawText[i], r,  nAlign, nValign);
	    	}
	    }
	}
	
	
	/*public void DrawText(String strDrawText, int nLeft, int nTop, int nRight, int nBottom, int nAlign) 
	{
		Rectangle r	=	new Rectangle();
		r.x			=	nLeft;
		r.y			=	nTop;
		r.width 	= 	nRight - nLeft;
		r.height 	=	nBottom - nTop;
		
	    FontRenderContext 	frc =  	new FontRenderContext(null, true, true);
	    Rectangle2D 		r2D = 	this.m_Font.getStringBounds(strDrawText, frc);
	    
	    int rWidth 		= (int) Math.round(r2D.getWidth());
	    int rHeight 	= (int) Math.round(r2D.getHeight());
	    int rX 			= (int) Math.round(r2D.getX());
	    int rY 			= (int) Math.round(r2D.getY());

	    int nAlignX		= 	0;
	    int nAlignY		= 	(r.height / 2) - (rHeight / 2) - rY;	// 수직정렬 Center
	    

	    m_grapchics.setFont(m_Font);
	    
	    //가운데정렬
  		if(this.CENTER == nAlign)
  		{
  			nAlignX		= 	(r.width / 2) - (rWidth / 2) - rX;
  			m_grapchics.drawString(strDrawText, r.x + nAlignX, r.y + nAlignY);
  		}
  		//오른쪽 정렬
  		else if(this.RIGHT == nAlign)
  		{
  			nAlignX		= 	r.width-rWidth-rX-2;
  			m_grapchics.drawString(strDrawText, r.x + nAlignX, r.y + nAlignY);
  		}
  		//왼쪽정렬
  		else
  		{
  			m_grapchics.drawString(strDrawText, r.x, r.y + nAlignY);
  		}
	}*/
	
	private void DrawString(String strDrawText, Rectangle rect, int nAlign, int nValign) 
	{
		
	    FontRenderContext 	frc =  	new FontRenderContext(null, true, true);
	    Rectangle2D 		r2D = 	this.m_Font.getStringBounds(strDrawText, frc);
	    
	    int rWidth 		= (int) Math.round(r2D.getWidth());
	    int rHeight 	= (int) Math.round(r2D.getHeight());
	    int rX 			= (int) Math.round(r2D.getX());
	    int rY 			= (int) Math.round(r2D.getY());

	    int nAlignX		= 	0;	    

	    m_grapchics.setFont(m_Font);
	    
	    //위로정렬
	    if(this.VTOP == nValign)
	    {
	    	//가운데정렬
	  		if(this.CENTER == nAlign)
	  		{
	  			nAlignX		= 	(rect.width / 2) - (rWidth / 2) - rX;
	  			m_grapchics.drawString(strDrawText, rect.x + nAlignX, rect.y);
	  		}
	  		//오른쪽 정렬
	  		else if(this.RIGHT == nAlign)
	  		{
	  			nAlignX		= 	rect.width-rWidth-rX-2;
	  			m_grapchics.drawString(strDrawText, rect.x + nAlignX, rect.y);
	  		}
	  		//왼쪽정렬
	  		else
	  		{
	  			m_grapchics.drawString(strDrawText, rect.x, rect.y);
	  		}	    	
	    }
	    //수직정렬 아래
	    else if(this.VBOTTOM == nValign)
	    {
	    	
	    	//가운데정렬
	  		if(this.CENTER == nAlign)
	  		{
	  			nAlignX		= 	(rect.width / 2) - (rWidth / 2) - rX;
	  			m_grapchics.drawString(strDrawText, rect.x + nAlignX, (rect.y + rect.height -rHeight));
	  		}
	  		//오른쪽 정렬
	  		else if(this.RIGHT == nAlign)
	  		{
	  			nAlignX		= 	rect.width-rWidth-rX-2;
	  			m_grapchics.drawString(strDrawText, rect.x + nAlignX, (rect.y + rect.height -rHeight));
	  		}
	  		//왼쪽정렬
	  		else
	  		{
	  			m_grapchics.drawString(strDrawText, rect.x, (rect.y + rect.height -rHeight));
	  		}	  		
	    }
	    //수직정렬 가운데
	    else
	    {

	    	int nAlignY		= 	(rect.height / 2) - (rHeight / 2) - rY;	// 수직정렬 Center
	    	
	    	//가운데정렬
	  		if(this.CENTER == nAlign)
	  		{
	  			nAlignX		= 	(rect.width / 2) - (rWidth / 2) - rX;
	  			m_grapchics.drawString(strDrawText, rect.x + nAlignX, rect.y + nAlignY);
	  		}
	  		//오른쪽 정렬
	  		else if(this.RIGHT == nAlign)
	  		{
	  			nAlignX		= 	rect.width-rWidth-rX-2;
	  			m_grapchics.drawString(strDrawText, rect.x + nAlignX, rect.y + nAlignY);
	  		}
	  		//왼쪽정렬
	  		else
	  		{
	  			m_grapchics.drawString(strDrawText, rect.x, rect.y + nAlignY);
	  		}	      	
	    }	    
	}
	
	public int getTextWidth(String strText)
	{
		FontRenderContext 	frc =  	new FontRenderContext(null, true, true);
    	Rectangle2D 		r2D 	= 	this.m_Font.getStringBounds(strText, frc);
    	    
    	return (int) Math.round(r2D.getWidth());
		
	}
	
	public int getTextHeight(String strText)
	{
		FontRenderContext 	frc =  	new FontRenderContext(null, true, true);
    	Rectangle2D 		r2D 	= 	this.m_Font.getStringBounds(strText, frc);
    	    
    	return (int) Math.round(r2D.getHeight());		
	}
	
	private String[] getCalAreaText(int nTextWidth, int nAreaWidth, String strContents) 
    { 
         String strText     =     ""; 
         if(nTextWidth < nAreaWidth) 
         { 
              strText     =     strContents; 
         } 
         else  
         { 
              String strTemp     =     ""; 
               
              int nContentLength     =     strContents.length(); 
              int nIdx     =     0; 
              for(int i=0; i<nContentLength; i++) 
              { 
                   strTemp               += strContents.charAt(i); 

	           	   FontRenderContext 	frc =  	new FontRenderContext(null, true, true);
	           	   Rectangle2D 		r2D 	= 	this.m_Font.getStringBounds(strTemp, frc);
	           	    
	           	   int nTempWidth 		= (int) Math.round(r2D.getWidth());
                    
                   if( nAreaWidth <  nTempWidth ) 
                   { 
                        String strTemp2     =     strTemp; 
                        for(int k=strTemp2.length(); k>0; k-- ) 
                        { 
                              
                             strTemp2     =     strTemp2.substring(0, k); 
                             
                             frc =  	new FontRenderContext(null, true, true);
          	           	     r2D = 	this.m_Font.getStringBounds(strTemp2, frc);
          	           	    
          	           	     nTempWidth 		= (int) Math.round(r2D.getWidth());                            
                             if(nAreaWidth > nTempWidth) 
                             { 
                                  //arrStrTxtLst.add(strTemp2); 
                                  if("".equals(strText)) 
                                  { 
                                       strText     =     strTemp2; 
                                  } 
                                  else 
                                  { 
                                       strText     +=     "\n" + strTemp2; 
                                  } 
                                  strTemp     =     ""; 
                                  nIdx++; 
                                  i = i -     1; 
                                   
                                  break; 
                             }                               
                        }                          
                                   
                   } 
                   else 
                   { 
                        if( (nContentLength-1) == i) 
                        { 
                             if("".equals(strText)) 
                             { 
                                  strText     =     strTemp; 
                             } 
                             else 
                             { 
                                  strText     +=     "\n" + strTemp; 
                             } 

                        } 
                   } 
              }           
         } 
         
         String[] arrStrTxt     =     strText.split("\n"); 
         return arrStrTxt;
    }
	
	public byte[] getSaveImageFile(String strImgFormat)
	{
		byte[] 					bufIamge	=	null;
		ByteArrayOutputStream 	baos 		=	null;
		try 
		{
			baos 		= new ByteArrayOutputStream();
			ImageIO.write( m_bBackgroud, strImgFormat, baos );
			baos.flush();
			bufIamge 	= baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return bufIamge;
	}
	
	public BufferedImage getSaveImageFile()
	{
		return this.m_bBackgroud;
	}
	
	public long saveImageFile(String strSavePath, String strImgFormat)
	{
		long lResFileSize = 0;
		String orgPath = strSavePath;
		Image image;
		int	imageOrgWidth;
        int	imageOrgHeight; 
		int newWidth; 
		int w;
		int h;
		double ratio;
		
		try {
			File file = new File(strSavePath);
			ImageIO.write(m_bBackgroud, "JPEG", file);

			image = ImageIO.read(new File(orgPath));
			imageOrgWidth = image.getWidth(null);
			imageOrgHeight = image.getHeight(null);
	        newWidth = imageOrgWidth / 2;    
	        ratio = (double)newWidth / (double)imageOrgWidth;
            w = (int)(imageOrgWidth * ratio);
            h = (int)(imageOrgHeight * ratio);
            
            Image resizeImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
			file.delete();
			m_bBackgroud = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics g = m_bBackgroud.getGraphics();
            g.drawImage(resizeImage, 0, 0, null);
            g.dispose();

			Office3GL Of3gl	=	new Office3GL(m_bBackgroud);
			Of3gl.setSaveQuality(92);
			file = Of3gl.getConvertFile("jpg", strSavePath);
		//file = Of3gl.getThumbNail(500,"jpg",strSavePath);
           // ImageIO.write(m_bBackgroud, "JPEG", file);
			lResFileSize = file.length();
		}
		catch (Exception e) {
			logger.error("Failed to save template.", e);
			lResFileSize = 0;
		}

		return lResFileSize;
	}
	
//	public long saveImageFile(String strSavePath, String strImgFormat)
//	{
//		long lResFileSize = 0;
//		try {
//			File file = new File(strSavePath);
//			ImageIO.write(m_bBackgroud, "JPEG", file);
//			lResFileSize = file.length();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			lResFileSize = 0;
//		}	
//		
//		return lResFileSize;
//	}


}
