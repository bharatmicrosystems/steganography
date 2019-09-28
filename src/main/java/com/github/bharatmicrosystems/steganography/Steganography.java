package com.github.bharatmicrosystems.steganography;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class Steganography {

	public void encryptImage(File imageIn, File imageOut, String message) {
		BufferedImage img = null;
		String format = null;
		try {
			format = getImageFormat(imageIn);
			img = ImageIO.read(imageIn);
		} catch (IOException e) {
			System.out.println(e);
		}

		int width = img.getWidth();
		int height = img.getHeight();

		char[] mess = stringToBinary(message).toCharArray();
		int c = 0;
		for (int x = 0; x < width; x++) {
			if (c - mess.length >= 8)
				break;
			for (int y = 0; y < height; y++) {
				if (c >= mess.length) {
					if (c - mess.length < 8) {
						// get pixel value
						int p = img.getRGB(x, y);
						// get alpha
						int a = (p >> 24) & 0xff;
						// get red
						int r = (p >> 16) & 0xff;
						// get green
						int g = (p >> 8) & 0xff;
						// get blue
						int b = p & 0xff;
						r = setLSB(r, 1);
						img.setRGB(x, y, getPixel(a, r, g, b));
						c++;
					} else {
						break;
					}

				} else {
					// get pixel value
					int p = img.getRGB(x, y);
					// get alpha
					int a = (p >> 24) & 0xff;
					// get red
					int r = (p >> 16) & 0xff;
					// get green
					int g = (p >> 8) & 0xff;
					// get blue
					int b = p & 0xff;
					r = setLSB(r, Integer.parseInt(""+mess[c]));
					img.setRGB(x, y, getPixel(a, r, g, b));
					c++;
				}
			}
		}

		// write image
		try {
			ImageIO.write(img, "png", imageOut);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public String decryptImage(File image) throws InterruptedException {
		BufferedImage img = null;
		try {
			img = ImageIO.read(image);
		} catch (IOException e) {
			System.out.println(e);
		}

		int width = img.getWidth();
		int height = img.getHeight();

		StringBuffer sb = new StringBuffer();
		for (int x = 0; x < width; x++) {
			if (!(sb.indexOf("011111111")==-1 || sb.indexOf("111111111")==-1)) {
				break;
			}
			for (int y = 0; y < height; y++) {
				// get pixel value
				int p = img.getRGB(x, y);
				// get red
				int r = (p >> 16) & 0xff;
				if (sb.indexOf("011111111")==-1 || sb.indexOf("111111111")==-1) {
					sb.append(getLSB(r));
				} else {
					break;
				}
			}
		}
		return binaryToString(sb.replace(sb.length()-9, sb.length()-1, "").toString());
	}

	private int getPixel(int a, int r, int g, int b) {
		int p = (a << 24) | (r << 16) | (g << 8) | b;
		return p;
	}

	private int setLSB(int num, int bit) {
		if (bit == 1)
			return (num | bit);
		else
			return (num & 254);
	}

	private int getLSB(int num) {
		return (num & 1);
	}

	private  String stringToBinary(String text) {
		String bString = "";
		String temp = "";
		for (int i = 0; i < text.length(); i++) {
			temp = Integer.toBinaryString(text.charAt(i));
			for (int j = temp.length(); j < 8; j++) {
				temp = "0" + temp;
			}
			bString += temp + "";
		}

		return bString;
	}

	private String binaryToString(String binaryCode) {
		String[] code = splitByNumber(binaryCode, 8);
		String word = "";
		for (int i = 0; i < code.length; i++) {
			word += (char) Integer.parseInt(code[i], 2);
		}
		return word;
	}
	
	private String[] splitByNumber(String str, int size) {
	    return (size<1 || str==null) ? null : str.split("(?<=\\G.{"+size+"})");
	}
	
	private String getImageFormat(File file) throws IOException{
		ImageInputStream iis = ImageIO.createImageInputStream(file);

		Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

		while (imageReaders.hasNext()) {
		    ImageReader reader = (ImageReader) imageReaders.next();
		    return reader.getFormatName();
		}
		return null;
	}

}
