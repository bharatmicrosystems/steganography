package com.github.bharatmicrosystems.steganography;

import java.io.File;

/**
 * Hello world!
 *
 */

public class App {
	public static void main(String[] args) throws InterruptedException {
		Steganography steg = new Steganography();
		steg.encryptImage(new File("input/flower.png"), new File("output/flower.png"), "What a beautiful flower!!");
		System.out.println(steg.decryptImage(new File("output/flower.png")));
	}
}
