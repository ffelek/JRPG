package game;

import characters.ACharacterAttributes;
import characters.Boss;
import characters.Character;
import equipments.Weapon;
import utils.ConsoleColors;
import utils.Loader;
import utils.PropertiesFile;
import utils.Randomizer;
import java.util.Scanner;

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

	public static void init()
	{
		Loader.init();
		System.out.println(Loader.stories.get("intro").getAsString());
		System.out.println("-".repeat(60));
	}

	public static void pickClass()
	{
		Game.displayPlayableClasses();
		System.out.print(ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK + "Pick your class (using the associated numbers):" + ConsoleColors.RESET + " ");
		String input = s.nextLine();
		int choice;
		if (input.matches("^[0-9]+$"))
		{
			choice = Integer.parseInt(input);
			switch (choice)
			{
				case 1 -> System.out.println("Want to break necks? Barbarian is made for this.");
				case 2 -> System.out.println("Want to shoot fireballs at your enemies? Good choice.");
				case 3 -> System.out.println("Rogue? You sneaky bastard.");
				default ->
				{
					System.out.println("Don't want to pick? Let's do it randomly then!");
					choice = Randomizer.randomInRange(Loader.playableClasses.size() - 1, 0);
				}
			}
			choice--;
		}
		else
		{
			System.out.println("Think you're funny? Let's do it randomly then!");
			choice = Randomizer.randomInRange(Loader.playableClasses.size() - 1, 0);
		}
		p = Loader.playableClasses.get(choice);
		p.setNickname(Game.playerName);
		System.out.printf("Your class is: " + ConsoleColors.BLUE_BACKGROUND + ConsoleColors.GREEN_BRIGHT + "%s" + ConsoleColors.RESET + "\n", p.getName());
		System.out.println("-".repeat(60));
	}

	private static void getPlayerName()
	{
		String prompt = ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK + "Enter your player name:" + ConsoleColors.RESET + " ";
		int attempts = 0;
		System.out.println(prompt);
		String pn = s.nextLine();
		attempts++;
		while (pn.trim().isEmpty())
		{
			switch (attempts)
			{
				case 1 ->
				{
					System.out.println("It seems you forgot to type in your name.\n" + "Would you be a dear and type it again?");
					System.out.println("-".repeat(60));
					System.out.print(prompt);
					pn = s.nextLine();
				}
				case 2 ->
				{
					System.out.println("You are doing it on purpose, it would seem. One more chance before I decide for you.");
					System.out.println("-".repeat(60));
					System.out.print(prompt);
					pn = s.nextLine();
				}
				default ->
				{
					System.out.println("I am not that patient. Your name is now 'Sir Douchebag'");
					pn = "Sir Douchebag";
				}
			}
			attempts++;
		}

		if (pn.equalsIgnoreCase("no") && attempts >= 1)
		{
			System.out.println("Aren't you disrespectful? Let me decide for you then: 'Sir Douchebag'.");
			pn = "Sir Douchebag";
		}

		Game.playerName = pn;
		System.out.println("-".repeat(60));
	}

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

	private static void initRules()
	{
		System.out.printf("""
				Here are the rules. You are about to go through rooms that all contains at least one boss. You can only go to the next one by defeating the boss.
				Each character in the room will play its turn in a specific order. During the turn, only a few actions can be taken, and none of them include fleeing the fight.
				Those actions will be accessible by the numbers associated to them (like the class choice). And don't try to be clever and type another thing than the numbers,
				otherwise, it will be randomized (or worse).
				I wish you good luck, %s.
				Are you sure you want to go into the first room?
				%s 1 - YES %s
				%s 2 - NO %s
				\n""", Game.playerName, ConsoleColors.BLACK, ConsoleColors.RESET, ConsoleColors.BLACK, ConsoleColors.RESET);
		String answer = s.nextLine();
		if (answer.matches("^[1-2]$"))
		{
			int intAnswer = Integer.parseInt(answer);
			if (intAnswer == 1)
			{
				System.out.println("You brave fool. Let's walk into the room then!");
			}
			else
			{
				System.out.println("OK. Let's go back to the beginning.\nNo, just joking. In the room, you go!");
			}
		}
		else
		{
			System.out.println("Don't want to decide yet? That is OK. You will go into the room no matter what.");
		}
		System.out.println("-".repeat(60));
	}

	private static void initRoomOne() throws InterruptedException
	{
		b = Loader.bosses.get(0);
		b.setActiveWeapon(Loader.weapons.get(3));
		System.out.println(Loader.stories.get("room1").getAsString());
		System.out.println("-".repeat(60));
		Thread.sleep(20000);
		Game.fight();
		System.out.println("-".repeat(60));
	}

	private static void initRoomTwo() throws InterruptedException
	{
		p.resetValues();
		b = Loader.bosses.get(1);
		b.setActiveWeapon(Loader.weapons.get(4));
		System.out.println(Loader.stories.get("room2"));
		System.out.println("-".repeat(60));
		Thread.sleep(20000);
		Game.fight();
		System.out.println("-".repeat(60));
	}

	private static void fight() throws InterruptedException
	{
		boolean isPlayerDead = p.isDead();
		boolean isBossDead = b.isDead();
		System.out.println("OK, let's decide who is going to strike first!");
		boolean isPlayerTurn = Randomizer.randomInRange(1, 0) == 1;
		Thread.sleep(2000);
		System.out.printf(isPlayerTurn ? "%s" : "%s will take his turn first\n", "You will take your turn first.\n", b.getNickname());
		System.out.println("-".repeat(60));
		while (!isPlayerDead || !isBossDead)
		{
			if (isPlayerTurn)
			{
				Game.playerTurn();
			}
			else
			{
				Game.bossTurn();
			}
			isPlayerTurn = !isPlayerTurn;
			isPlayerDead = p.isDead();
			isBossDead = b.isDead();
			System.out.println("-".repeat(60));

			if (isPlayerDead)
			{
				System.out.println("You died. That's a shame, or was it predictable?. Game Over.");
				System.exit(0);
			}

			if (isBossDead)
			{
				System.out.println(ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK_UNDERLINED + "The boss has died. Congratulations." + ConsoleColors.RESET);
				break;
			}
		}
	}

	private static <Entity extends ACharacterAttributes> boolean changeWeapon(Entity e)
	{
		e.displayWeapons();
		System.out.println("Enter the number of the desired weapon: (nothing to go back the previous screen)");
		String answer = s.nextLine();
		if (answer.matches("^[0-9]+$"))
		{
			int choice = Integer.parseInt(answer);
			if (choice >= 0 && choice <= e.getWeapons().size())
			{
				if (!(e.getWeapons().get(choice - 1) == e.getActiveWeapon()))
				{
					e.setActiveWeapon(e.getWeapons().get(choice - 1));
					System.out.printf("Your active weapon is now %s.\n", e.getActiveWeapon().getName());
					return true;
				}
				else
				{
					System.out.println("No change happened. Return to the previous screen.");
				}
			}
		}
		else
		{
			System.out.println("No change happened. Return to the previous screen.");
		}
		return false;
	}

	private static int getPlayerActionAnswer()
	{
		System.out.printf("%s What will you do?%s ", ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK, ConsoleColors.RESET);
		String answer = s.nextLine();
		if (answer.matches("^[1-5]$"))
		{
			return Integer.parseInt(answer);
		}
		else
		{
			return 0;
		}
	}

	private static void playerTurn()
	{
		int choice;
		do
		{
			if (p.isHasParried())
			{
				p.setHasParried(false);
			}
			System.out.printf("%s Turn.%s\n", ConsoleColors.WHITE_BACKGROUND + ConsoleColors.BLACK_UNDERLINED + p.getNickname(), ConsoleColors.RESET);
			System.out.printf("""
							What do you want to do?
							%s1%s - Attack %s %s
							%s2%s - Parry (halve damage if attacked next turn & restore a bit of stamina)
							%s3%s - Change Weapon
							%s4%s - Rest (restore health, mana & stamina)
							%s5%s - Character status
							\n""", ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET, p.getActiveWeapon().getDamageCapability(),
					p.isStaminaOut() ? ConsoleColors.RED_UNDERLINED + "Cannot attack because of exhaustion." + ConsoleColors.RESET : "",
					ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET,
					ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET,
					ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET,
					ConsoleColors.BLACK_UNDERLINED, ConsoleColors.RESET);
			choice = Game.getPlayerActionAnswer();
			switch (choice)
			{
				case 1 ->
				{
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

	private static void bossTurn() throws InterruptedException
	{
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

		/*
		 Implement resting for boss as well
		 */
	}
}