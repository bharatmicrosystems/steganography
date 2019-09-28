package com.github.bharatmicrosystems.steganography;

import java.io.File;
import java.io.IOException;

/**
 * Hello world!
 *
 */

public class App {
	public static void main(String[] args) throws InterruptedException, IOException {
		Steganography steg = new Steganography();
		steg.encryptImage(new File("input/bloom.jpg"), new File("output/bloom.jpg"), "What a beautiful flower!!");
		System.out.println(steg.decryptImage(new File("output/bloom.jpg")));
	}
	
	
}
