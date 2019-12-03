package com.woonam.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;

public class Office3GL
{
	private BufferedImage  m_bufferImage		=	null;
	private String 		m_strImageFormat	=	"";
	private Image			m_image				=	null;
	private float			m_fImgQuality		=	0.86f;

	;	public Office3GL(String strFilePath) throws Exception
{


	//getFormat(fFile);
	//this.m_image = ImageIO.read(fFile);

	File fFile	  = new File(strFilePath);
	checkFormat(fFile);

	if("jpeg".equalsIgnoreCase(m_strImageFormat) || "jpeg".equalsIgnoreCase(m_strImageFormat))
	{
		m_image = new ImageIcon(strFilePath).getImage();
		this.m_bufferImage = ImagetoBufferedImage( this.m_image );
	}
	else if("png".equalsIgnoreCase(m_strImageFormat))
	{
		m_image = ImageIO.read(fFile);
		this.m_bufferImage = ImagetoBufferedImage( this.m_image, true );
	}
	else
	{
		m_image = ImageIO.read(fFile);
		this.m_bufferImage = ImagetoBufferedImage( this.m_image );
	}





}

	public Office3GL(File fFile) throws Exception
	{


		//getFormat(fFile);
		checkFormat(fFile);

		String strFilePath	=	fFile.getAbsolutePath();
		if("jpeg".equalsIgnoreCase(m_strImageFormat) || "jpeg".equalsIgnoreCase(m_strImageFormat))
		{
			m_image = new ImageIcon(strFilePath).getImage();
			this.m_bufferImage = ImagetoBufferedImage( this.m_image );
		}
		else if("png".equalsIgnoreCase(m_strImageFormat))
		{
			m_image = ImageIO.read(fFile);
			this.m_bufferImage = ImagetoBufferedImage( this.m_image, true );
		}
		else
		{
			m_image = ImageIO.read(fFile);
			this.m_bufferImage = ImagetoBufferedImage( this.m_image );
		}
	}

	public Office3GL(byte[] bufImage) throws Exception
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(bufImage);
		ByteArrayInputStream bis2 = new ByteArrayInputStream(bufImage);

		//this.m_bufferImage = ImageIO.read(bis);

		checkFormat(bis2);

		if("jpeg".equalsIgnoreCase(m_strImageFormat) || "jpeg".equalsIgnoreCase(m_strImageFormat))
		{
			m_image = new ImageIcon(bufImage).getImage();
			this.m_bufferImage = ImagetoBufferedImage( this.m_image );
		}
		else if("png".equalsIgnoreCase(m_strImageFormat))
		{
			m_image = ImageIO.read(bis);
			this.m_bufferImage = ImagetoBufferedImage( this.m_image, true );
		}
		else
		{
			m_image = ImageIO.read(bis);
			this.m_bufferImage = ImagetoBufferedImage( this.m_image );
		}
	}



	public Office3GL(BufferedImage bufImage) throws Exception
	{
		this.m_image = bufImage;

		this.m_bufferImage = ImagetoBufferedImage( this.m_image, true );
	}


	private String checkFormat(Object ImgObj) throws Exception
	{
		ImageInputStream iis = ImageIO.createImageInputStream(ImgObj);

		if(iis == null)
		{
			return null;
		}

		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);

		if (!iter.hasNext())
		{
			return null;
		}

		ImageReader reader = iter.next();

		if(iis != null)
		{
			iis.close();
		}

		this.m_strImageFormat = reader.getFormatName();

		return this.m_strImageFormat;

	}

	public void setSaveQuality(int nQuality)
	{
		if( nQuality > 100 )
		{
			nQuality = 99;
		}

		this.m_fImgQuality = nQuality * 0.01f;
	}

	public String getFormat()
	{
		return this.m_strImageFormat;
	}

	public int getWidth()
	{
		return this.m_image.getWidth(null);
	}

	public int getHeight()
	{
		return this.m_image.getHeight(null);
	}

	public int getDPIWidth(int nDPI)
	{
		return pixel2pt(getWidth(), nDPI);
	}

	public int getDPIHeight(int nDPI)
	{
		return pixel2pt(getHeight(), nDPI);
	}

	private int pixel2pt(int pixel, int dpi)
	{

		return  ( pixel * 720 ) / dpi;
	}

	public byte[] getThumbNail(int nDestWidth, String strFormat) throws Exception
	{
		int nDestHeight	=	getThumNailHeight(nDestWidth);
		BufferedImage bufThumb	=	getResizeBufImage(nDestWidth, nDestHeight);

		return getByteArray(bufThumb, strFormat);
	}

	public File getThumbNail(int nDestWidth, String strFormat, String strSavePath) throws Exception
	{
		int nDestHeight	=	getThumNailHeight(nDestWidth);
		BufferedImage bufThumb	=	getResizeBufImage(nDestWidth, nDestHeight);

		return getFile(bufThumb, strFormat, strSavePath);
	}

	private int getThumNailHeight(int nThumNailWidth)
	{
		float 	nOriginalWidth	=	(float)getWidth();
		float	nOriginalHeight	=	(float)getHeight();
		float	fThumNailRatio	=	nThumNailWidth / nOriginalWidth;
		int		nHeight			=	(int)(nOriginalHeight * fThumNailRatio);

		return nHeight;
	}



	public void Resize(int nWidth, int nHeight) throws Exception
	{
		Image imgTarget = this.m_image.getScaledInstance(nWidth, nHeight, Image.SCALE_SMOOTH);
		int pixels[] = new int[nWidth * nHeight];
		PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, nWidth, nHeight, pixels, 0, nWidth);
		pg.grabPixels();
		BufferedImage destImg = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_RGB);
		destImg.setRGB(0, 0, nWidth, nHeight, pixels, 0, nWidth);
		this.m_bufferImage	=	destImg;
	}

	private BufferedImage getResizeBufImage(int nWidth, int nHeight) throws Exception
	{
		Image imgTarget = this.m_image.getScaledInstance(nWidth, nHeight, Image.SCALE_SMOOTH);
		int pixels[] = new int[nWidth * nHeight];
		PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, nWidth, nHeight, pixels, 0, nWidth);
		pg.grabPixels();
		BufferedImage destImg = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_RGB);
		destImg.setRGB(0, 0, nWidth, nHeight, pixels, 0, nWidth);
		return destImg;
	}

	private byte[] getByteArray (BufferedImage bufImg, String strFormat) throws IOException
	{
		ByteArrayOutputStream baos 		= 	new ByteArrayOutputStream();
		Iterator<ImageWriter> writers 	= 	ImageIO.getImageWritersByFormatName(strFormat);

		ImageWriter writer 				= 	writers.next();
		ImageWriteParam param 			= 	writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(this.m_fImgQuality);
		ImageOutputStream ios 			= 	ImageIO.createImageOutputStream(baos);
		writer.setOutput(ios);
		writer.write(null, new IIOImage(bufImg, null, null), param);


		byte[] data = baos.toByteArray();
		writer.dispose();

		return data;
	}


	private File getFile (BufferedImage bufImg, String strFormat, String strSavePath) throws IOException
	{
		byte[] bufImage	=	getByteArray(bufImg, strFormat);

		File saveFile	=	null;

		if(bufImage != null)
		{
			writeToFile(strSavePath, bufImage);
		}

		return saveFile;
	}

	public byte[] getConvertByteArray (String strFormat) throws IOException
	{
		ByteArrayOutputStream baos 		= 	new ByteArrayOutputStream();
		Iterator<ImageWriter> writers 	= 	ImageIO.getImageWritersByFormatName(strFormat);

		ImageWriter writer 				= 	writers.next();
		ImageWriteParam param 			= 	writer.getDefaultWriteParam();

		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(this.m_fImgQuality);
		ImageOutputStream ios 			= 	ImageIO.createImageOutputStream(baos);
		writer.setOutput(ios);
		writer.write(null, new IIOImage(m_bufferImage, null, null), param);


		byte[] data = baos.toByteArray();
		writer.dispose();

		return data;
	}


	public File getConvertFile (String strFormat, String strSavePath) throws IOException
	{
		byte[] bufImage	=	getConvertByteArray(strFormat);

		File saveFile	=	null;

		if(bufImage != null)
		{
			writeToFile(strSavePath, bufImage);
		}

		return saveFile;
	}

	private File writeToFile(String strSavePath, byte[] pData)
	{

		if(pData == null){

			return null;

		}

		File fOutFile = null;

		try
		{

			fOutFile = new File(strSavePath);

			FileOutputStream lFileOutputStream = new FileOutputStream(fOutFile);

			lFileOutputStream.write(pData);

			lFileOutputStream.close();

		}catch(Throwable e){

			e.printStackTrace(System.out);

		}

		return fOutFile;

	}

	private BufferedImage rotate(BufferedImage bi, int degree)
	{
		int width = bi.getWidth();
		int height = bi.getHeight();

		BufferedImage biFlip;
		if (degree == 90 || degree == 270)
			biFlip = new BufferedImage(height, width, bi.getType());
		else if (degree == 180)
			biFlip = new BufferedImage(width, height, bi.getType());
		else
			return bi;

		if (degree == 90) {
			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					biFlip.setRGB(height- j - 1, i, bi.getRGB(i, j));
		}

		if (degree == 180) {
			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					biFlip.setRGB(width - i - 1, height - j - 1, bi.getRGB(i, j));
		}

		if (degree == 270) {
			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					biFlip.setRGB(j, width - i - 1, bi.getRGB(i, j));
		}

		bi.flush();
		bi = null;

		return biFlip;
	}

	public void Rotate(int nDegree)
	{
		this.m_bufferImage = rotate(this.m_bufferImage,  nDegree);
		this.m_image	=	m_bufferImage;
	}

	private BufferedImage ImagetoBufferedImage(Image img)
	{
		if (img instanceof BufferedImage)
		{
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	private BufferedImage ImagetoBufferedImage(Image img, boolean bIsForceConvert)
	{
		if(bIsForceConvert)
		{
			if (img instanceof BufferedImage)
			{
				return (BufferedImage) img;
			}
		}


		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}
}
