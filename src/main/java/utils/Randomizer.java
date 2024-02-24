package utils;

import java.util.Random;

/**
 * Manages the randomness in the game
 * @author felek
 */
public class Randomizer
{
	private static final Random random = new Random();

	public static int randomInRange(int max, int min)
	{
		return Randomizer.random.nextInt((max - min) + min) + 1;
	}
}
