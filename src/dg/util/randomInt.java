package dg.util;

import java.util.Random;


public class randomInt {
	public static int generate(int min, int max) {
		Random rand = new Random();
		int randomNumber = rand.nextInt((max - min) + 1) + min;
		return randomNumber;
	}
	
	public static void main(String[] args){
		for(int i = 0; i < 10000; i++) {
			int num = generate(25,30);
			assert (num >= 25 && num <= 30); 
		}	
	}
}
