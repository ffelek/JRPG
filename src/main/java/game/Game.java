package game;

import characters.ACharacterAttributes;
import characters.Boss;
import characters.Character;
import equipments.Weapon;
import utils.ConsoleColors;
import bootstrap.Loader;
import config.PropertiesFile;
import utils.Randomizer;
import java.util.Scanner;

/**
 * Contains game rules and the game itself
 */
public class Game
{
	public static String playerName;
	private static final Scanner s = new Scanner(System.in);
	private static Character p;
	private static Boss b;

	public static void main(String[] args)
	{
		try
		{
			// Initialization of the game (preloading data file and classes linked)
			PropertiesFile.loadProperties();
			Game.init();
			// Retrieve player name
			Game.getPlayerName();
			//
			Game.pickClass();
			Thread.sleep(3000);
			Game.giveWeapon();
			Thread.sleep(10000);
			Game.initRules();
			Thread.sleep(4000);
			Game.initRoomOne();
			Game.initRoomTwo();
		}
		catch (InterruptedException error)
		{
			System.out.println("An error occurred. Here is the detailed message: " + error.getLocalizedMessage());
			System.exit(0);
		}
	}

	/**
	 * Initialize the game and launch it
	 */
	public static void init()
	{
		Loader.init();
		System.out.println(Loader.stories.get("intro").getAsString());
		System.out.println("-".repeat(60));
	}

	/**
	 * Asks the player name
	 */
	private static void getPlayerName()
	{
		String prompt = ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK + "Enter your player name:" + ConsoleColors.RESET + " ";
		String output = Game.getPlayerActionAnswer(prompt, "^(?=.*[a-zA-Z]).*$");
		while (output.trim().isEmpty())
		{
			output = getPlayerActionAnswer(prompt, "^(?=.*[a-zA-Z]).*$");
		}
		Game.playerName = output;
		System.out.println("-".repeat(60));
	}

	/**
	 * Shows playable classes and asks the player to choose one
	 */
	public static void pickClass()
	{
		Game.displayPlayableClasses();
		String prompt = ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK + "Pick your class (using the associated numbers):" + ConsoleColors.RESET + " ";
		String output = Game.getPlayerActionAnswer(prompt, "[0-9]+");
		int choice;
		if (output.trim().isEmpty())
		{
			System.out.println("Don't want to pick? Let's do it randomly then!");
			choice = Randomizer.randomInRange(Loader.playableClasses.size() - 1, 0);
		}
		else
		{
			choice = Integer.parseInt(output);
			switch (choice)
			{
				case 1 -> System.out.println("Want to break necks? Barbarian is made for this.");
				case 2 -> System.out.println("Want to shoot fireballs at your enemies? Good choice.");
				case 3 -> System.out.println("Rogue? You sneaky bastard.");
				default ->
				{
					System.out.println("Don't want to choose one of those? Too bad you had a choice, because now, it will be a random one!");
					choice = Randomizer.randomInRange(Loader.playableClasses.size() - 1, 0);
				}
			}
			choice --;
		}
		// Affecting the chosen class to the static variable
		p = Loader.playableClasses.get(choice);
		p.setNickname(Game.playerName);
		System.out.printf("Your class is: " + ConsoleColors.BLUE_BACKGROUND + ConsoleColors.GREEN_BRIGHT + "%s" + ConsoleColors.RESET + "\n", p.getName());
		System.out.println("-".repeat(60));
	}

	/**
	 * Displays to the player the list of playable classes
	 */
	private static void displayPlayableClasses()
	{
		int i = 0;
		System.out.printf("OK %s, it is now time to choose your class among these ones: \n", Game.playerName);
		for (Character pc : Loader.playableClasses)
		{
			System.out.printf(ConsoleColors.BLACK_UNDERLINED + "%d" + ConsoleColors.RESET + " - %s\n\tHP: %d\n\tMP: %d\n\tStamina: %d\n\tWeight: %d\n", i + 1, pc.getName(), pc.getHp(), pc.getMp(), pc.getStamina(), pc.getWeight());
			i++;
		}
		System.out.println("-".repeat(60));
	}

	/**
	 * Gives a weapon to the player
	 */
	private static void giveWeapon()
	{
		Weapon w = Loader.weapons.get(0);
		System.out.printf("""
				Since you are about to get crushed, let's give you a little chance of survival.
				Here's a weapon, on the house.
				Name: %s
				\tDamage: %s %s
				\tDurability: %s
				\tCost: %s
				\tType: %s
				""", w.getName(), w.getDamage(), w.getDamageCapability(), w.getDurability(), w.getCost(), w.getWeaponType());
		p.addWeapon(w);
		p.setActiveWeapon(w);
		System.out.println("-".repeat(60));
	}

	/**
	 * Explains the game rules and context to the player
	 * @throws InterruptedException thrown if current thread is interrupted
	 */
	private static void initRules() throws InterruptedException
	{
		System.out.printf("""
				Here are the rules. You are about to go through rooms that all contains at least one boss. You can only go to the next one by defeating the boss.
				Each character in the room will play its turn in a specific order. During the turn, only a few actions can be taken, and none of them include fleeing the fight.
				Those actions will be accessible by the numbers associated to them (like the class choice). And don't try to be clever and type another thing than the numbers,
				otherwise, it will be randomized (or worse).
				I wish you good luck, %s.
				\n""", Game.playerName);
		String prompt = """
				Are you sure you want to go into the first room?
				%s 1 - YES %s
				%s 2 - NO %s
				""".formatted(ConsoleColors.BLACK, ConsoleColors.RESET, ConsoleColors.BLACK, ConsoleColors.RESET);

		String output = Game.getPlayerActionAnswer(prompt, "^[1-2]$");
		// Checks what the player typed in
		if (!output.trim().isEmpty())
		{
			int intAnswer = Integer.parseInt(output);
			if (intAnswer == 1)
			{
				System.out.println("You brave fool. Let's walk into the room then!");
			}
			else
			{
				System.out.println("OK. Let's go back to the beginning.\n");
				Thread.sleep(3000);
				System.out.println("No, just joking. In the room, you go!");
			}
		}
		else
		{
			System.out.println("Don't want to decide? That is OK. You will go into the room no matter what.");
		}
		System.out.println("-".repeat(60));
	}

	/**
	 * First round of the game (Room 1)
	 * @throws InterruptedException thrown if current thread is interrupted
	 */
	private static void initRoomOne() throws InterruptedException
	{
		// Get first boss
		b = Loader.bosses.get(0);
		// Get their weapon
		b.setActiveWeapon(Loader.weapons.get(3));
		System.out.println(Loader.stories.get("room1").getAsString());
		System.out.println("-".repeat(60));
		Thread.sleep(20000);
		Game.fight();
		System.out.println("-".repeat(60));
	}

	/**
	 * Second round of the game (Room 2)
	 * @throws InterruptedException thrown if current thread is interrupted
	 */
	private static void initRoomTwo() throws InterruptedException
	{
		// Heal the player as a gift
		p.resetValues();
		// Get second boss
		b = Loader.bosses.get(1);
		// Get their weapon
		b.setActiveWeapon(Loader.weapons.get(4));
		System.out.println(Loader.stories.get("room2").getAsString());
		System.out.println("-".repeat(60));
		Thread.sleep(20000);
		Game.fight();
		System.out.println("-".repeat(60));
	}

	/**
	 * Contains all the logic for the battle phase
	 * @throws InterruptedException thrown if current thread is interrupted
	 */
	private static void fight() throws InterruptedException
	{
		// Retrieve characters status
		boolean isPlayerDead = p.isDead();
		boolean isBossDead = b.isDead();
		System.out.println("OK, let's decide who is going to strike first!");
		// Random choice to decide who plays first
		boolean isPlayerTurn = Randomizer.randomInRange(1, 0) == 1;
		Thread.sleep(2000);
		System.out.printf(isPlayerTurn ? "%s" : "%s will take his turn first\n", "You will take your turn first.\n", b.getNickname());
		System.out.println("-".repeat(60));
		// Entering game loop
		while (!isPlayerDead || !isBossDead)
		{
			if (isPlayerTurn)
			{
				// Player turn
				Game.playerTurn();
			}
			else
			{
				// Boss turn
				Game.bossTurn();
			}
			// Switching turn
			isPlayerTurn = !isPlayerTurn;
			// Retrieving characters status to continue or not the game loop
			isPlayerDead = p.isDead();
			isBossDead = b.isDead();
			System.out.println("-".repeat(60));

			// Player's death
			if (isPlayerDead)
			{
				System.out.println("You died. That's a shame, or was it predictable?. Game Over.");
				System.exit(0);
			}

			// Boss' death
			if (isBossDead)
			{
				System.out.println(ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK_UNDERLINED + "The boss has died. Congratulations." + ConsoleColors.RESET);
				break;
			}
		}
	}

	/**
	 * Displays the character's list of owned weapon and allows them to change it
	 * @param e character (player or boss)
	 * @param <Entity> character (player or boss)
	 * @return true if weapon has changed, false if not changed or the same as before
	 */
	private static <Entity extends ACharacterAttributes> boolean changeWeapon(Entity e)
	{
		// Displays weapons list
		e.displayWeapons();
		String prompt = "Enter the number of the desired weapon: (nothing to go back the previous screen)";
		String output = Game.getPlayerActionAnswer(prompt, "^[0-9]+$");
		if (!output.trim().isEmpty())
		{
			int choice = Integer.parseInt(output);
			// Check if choice is indeed a valid choice
			if (choice >= 0 && choice <= e.getWeapons().size())
			{
				// If so, check if the choice is different from the current active weapon
				if (!(e.getWeapons().get(choice - 1) == e.getActiveWeapon()))
				{
					e.setActiveWeapon(e.getWeapons().get(choice - 1));
					System.out.printf("Your active weapon is now %s.\n", e.getActiveWeapon().getName());
					return true;
				}
			}
		}
		System.out.println("No change happened. Return to the previous screen.");
		return false;
	}

	/**
	 * Retrieves input from player and checks if it is valid (according to pattern)
	 * @param pattern RegEx pattern to check
	 * @return choice of player or 0 if not valid
	 */
	private static String getPlayerActionAnswer(String prompt, String pattern)
	{
		System.out.printf("%s%s%s ", ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK, prompt, ConsoleColors.RESET);
		String answer = s.nextLine();
		if (!answer.trim().isEmpty() && answer.matches(pattern))
		{
			return answer;
		}
		return "";
	}

	/**
	 * Describes the player turn in the game loop
	 */
	private static void playerTurn()
	{
		int choice;
		do
		{
			// Check if the player has parried previous turn
			if (p.isHasParried())
			{
				// If so, toggle it
				p.setHasParried(false);
			}
			System.out.printf("%s Turn.%s\n", ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK_UNDERLINED + p.getNickname(), ConsoleColors.RESET);
			// Displays the messages with the possible actions
			String prompt = """
							What do you want to do?
							%s1%s - Attack %s %s
							%s2%s - Parry (halve damage if attacked next turn & restore a bit of stamina)
							%s3%s - Change Weapon
							%s4%s - Rest (restore health, mana & stamina)
							%s5%s - Character status
							\n""".formatted(ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET, p.getActiveWeapon().getDamageCapability(),
					p.isStaminaOut() ? ConsoleColors.RED_UNDERLINED + "Cannot attack because of exhaustion." + ConsoleColors.RESET : "",
					ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET,
					ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET,
					ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET,
					ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET);
			choice = Integer.parseInt(Game.getPlayerActionAnswer(prompt, "^[1-5]$"));
			switch (choice)
			{
				case 1 ->
				{
					// In case of attack, check if not out of stamina
					if (!p.isStaminaOut())
					{
						p.attack(b, p.getActiveWeapon(), false);
					}
					else
					{
						System.out.println(ConsoleColors.BLACK_BACKGROUND + ConsoleColors.RED_UNDERLINED + "Cannot attack. You're exhausted! Change your weapon, parry or rest." + ConsoleColors.RESET);
						choice = 0;
					}
				}
				case 2 -> p.parry();
				case 3 ->
				{
					// If weapon not changed, lets player take its turn normally
					if (!Game.changeWeapon(p))
					{
						choice = 0;
					}
				}
				case 4 -> p.rest();
				case 5 ->
				{
					Weapon w = p.getActiveWeapon();
					System.out.printf("""
					Character name:	%s
						Class: %s
						HP: %d (%d currently)
						MP: %d (%d currently)
						Stamina: %d (%d currently)
						Active Weapon: %s
							Damage: %s %s
							Durability: %d (%d currently)
							Stamina usage cost: %d
							Type: %s
					""",
							Game.playerName,
							p.getName(),
							p.getInitialHp(),
							p.getHp(),
							p.getInitialMp(),
							p.getMp(),
							p.getInitialStamina(),
							p.getStamina(),
							w.getName(),
							w.getDamage(),
							w.getDamageCapability(),
							w.getInitialDurability(),
							w.getDurability(),
							w.getCost(),
							w.getWeaponType());
					choice = 0;
				}
				default -> System.out.println("-".repeat(60));
			}
		} while (choice == 0);
	}

	/**
	 * Describes the boss turn in the game loop
	 * @throws InterruptedException thrown if the current thread is interrupted
	 */
	private static void bossTurn() throws InterruptedException
	{
		// Check if the boss has parried previous turn
		if (b.isHasParried())
		{
			b.setHasParried(false);
		}
		System.out.printf("%s Turn.%s\n", ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK_UNDERLINED + b.getNickname(), ConsoleColors.RESET);
		Thread.sleep(5000);
		switch (Randomizer.randomInRange(20, 1) % 5)
		{
			case 0 -> b.attack(p, b.getActiveWeapon(), false);
			case 1 -> b.parry();
			default -> b.rest();
		}
	}
}