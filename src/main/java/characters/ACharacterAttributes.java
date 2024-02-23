package characters;

import equipments.Equipment;
import equipments.Weapon;
import lombok.Getter;
import lombok.Setter;
import utils.ConsoleColors;
import utils.Constants;
import config.PropertiesFile;
import utils.Randomizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class defining variables & methods for any character (playable or not)
 * @author felek
 */
@Getter
@Setter
public abstract class ACharacterAttributes
{
	private int id;
	private String name;
	private String nickname;
	private int hp;
	private int initialHp;
	private int mp;
	private int initialMp;
	private int stamina;
	private int initialStamina;
	private List<Weapon> weapons = new ArrayList<>();
	private List<Equipment> equipments = new ArrayList<>();
	private Weapon activeWeapon;
	private boolean hasParried;

	/**
	 * Constructor
	 * @param id id
	 * @param name character name
	 * @param nickname character nickname
	 * @param hp character Health Points
	 * @param mp character Mana Points
	 * @param stamina character Stamina Points
	 */
	public ACharacterAttributes(int id, String name, String nickname, int hp, int mp, int stamina)
	{
		this.setId(id);
		this.setName(name);
		this.setNickname(nickname);
		this.setHp(hp);
		this.setInitialHp(hp);
		this.setMp(mp);
		this.setInitialMp(mp);
		this.setStamina(stamina);
		this.setInitialStamina(stamina);
	}

	/**
	 * Checks if the character is dead (reduced to 0)
	 * @return boolean to acknowledge if the character is dead
	 */
	public boolean isDead()
	{
		return hp == 0;
	}

	/**
	 * Deduces the character's HP by the specified amount and adjusts the HP to 0 if negative
	 * @param damageDealt amount of received damage (int)
	 */
	public void attackReceived(int damageDealt)
	{
		this.hp -= damageDealt;
		if (hp < 0)
		{
			this.hp = 0;
		}
	}

	/**
	 * Adds to the character's HP, the specified amount and adjusts the HP to the initial value if superior to it
	 * @param healthPoints amount of health points (int)
	 */
	public void regenHealth(int healthPoints)
	{
		this.hp += healthPoints;
		if (this.hp > this.initialHp)
		{
			this.hp = this.initialHp;
		}
	}

	/**
	 * Checks if the character is out of mana (reduced to 0)
	 * @return boolean to acknowledge if the character is out of mana
	 */
	public boolean isManaOut()
	{
		return this.mp == 0;
	}

	/**
	 * Adds to the character's mana, the specified amount and adjusts the mana to the initial value if superior to it
	 * @param manaPoints amount of mana points (int)
	 */
	public void regenMana(int manaPoints)
	{
		this.mp += manaPoints;
		if (this.mp > this.initialMp)
		{
			this.mp = this.initialMp;
		}
	}

	/**
	 * Deduces the character's mana by the specified amount and adjusts the mana to 0 if negative
	 * @param manaPoints amount of mana (int)
	 */
	public void depleteMana(int manaPoints)
	{
		this.mp -= manaPoints;
		if (this.mp <= 0)
		{
			this.mp = 0;
		}
	}

	/**
	 * Checks if the character is out of stamina (reduced to 0)
	 * @return boolean to acknowledge if the character is out of stamina
	 */
	public boolean isStaminaOut()
	{
		if (this.stamina <= 0)
		{
			this.stamina = 0;
		}
		return this.stamina == 0;
	}

	/**
	 * Adds to the character's stamina, the specified amount and adjusts the stamina to the initial value if superior to it
	 * @param staminaPoints amount of stamina points (int)
	 */
	public void regenStamina(int staminaPoints)
	{
		this.stamina += staminaPoints;
		if (this.stamina > this.initialStamina)
		{
			this.stamina = this.initialStamina;
		}
	}

	/**
	 * Deduces the character's stamina by the specified amount and adjusts the stamina to 0 if negative
	 * @param staminaPoints amount of stamina (int)
	 */
	public void depleteStamina(int staminaPoints)
	{
		this.stamina -= staminaPoints;
		if (this.stamina < 0)
		{
			this.stamina = 0;
		}
	}

	/**
	 * Attacks a specified target
	 * @param t target to the attack
	 * @param w weapon used for the attack
	 * @param isGameMasterForcedAttack attack from the GM or not
	 * @param <Target> Character or Boss
	 */
	public <Target extends ACharacterAttributes> void attack(Target t, Weapon w, boolean isGameMasterForcedAttack)
	{
		String message;
		int totalDamage = 0;
		// Retrieves weapon damage and splits it
		String[] damage = w.getDamage().split("d");
		// Retrieves the number of dices to throw
		int diceNumber = Integer.parseInt(damage[0]);
		// Retrieves the number of faces for each dice
		int diceFaces = Integer.parseInt(damage[1]);
		// Generates damage for the dice throw(s)
		for (int i = 0; i < diceNumber; i++)
		{
			totalDamage += Randomizer.randomInRange(diceFaces, 1);
		}

		// Checks if it is an attack made by the game master
		int hitType = !isGameMasterForcedAttack ? this.hitManagement() : 2;

		// Alters the damage to inflict and the personalized message depending on the hit type (critical miss, critical hit or normal hit)
		switch (hitType)
		{
			// Critical Miss
			case 0 ->
			{
				totalDamage = 0;
				message = "Critical Miss! %s dealt the huge amount of %d point of damage to %s. %s\n";
			}
			// Critical Hit
			case 2 ->
			{
				// If critical, the damage will be multiplied by a configurable multiplier
				totalDamage = (int) Math.round(totalDamage * Double.parseDouble(Objects.requireNonNull(PropertiesFile.getProperty(Constants.CRITICAL_MULTIPLIER_KEY))));
				message = "Critical Hit! %s dealt %d point(s) of damage to %s. %s\n";
			}
			// Normal Hit
			default -> message = "%s dealt %d point(s) of damage to %s. %s\n";
		}

		// Checks the target is parrying
		if (t.isHasParried())
		{
			// Halve damage if parrying
			totalDamage = (int) Math.ceil((double) totalDamage / 2);
		}

		// Inflicts damage
		t.attackReceived(totalDamage);
		System.out.printf(message, this.getNickname(), totalDamage, t.getNickname(), t.isHasParried() ? "Half damage because " + t.getNickname() + " parried." : "");
		// Reduces stamina
		this.depleteStamina(this.getActiveWeapon().getCost());
		// Warns player if out of stamina
		if (this instanceof Character && this.isStaminaOut())
		{
			System.out.println(ConsoleColors.BLACK_BACKGROUND + ConsoleColors.RED_UNDERLINED + "Careful! Your stamina is depleted. You will have to rest a bit for another attack." + ConsoleColors.RESET);
		}
	}

	/**
	 * Switches the character's parry status and reduces damage by 4 if attacked next turn
	 */
	public void parry()
	{
		this.hasParried = !this.hasParried;
		int staminaPointsRegenerated = (int) (double) Math.round((float) this.getInitialStamina() / 4);
		this.regenStamina(staminaPointsRegenerated);
		System.out.println("Parrying. " + staminaPointsRegenerated + " stamina points have been recovered doing so.");
	}

	/**
	 * Restores half of HP pool & half of stamina pool
	 */
	public void rest()
	{
//		this.regenHealth((int) (double) (this.initialHp / 2));
		this.regenMana((int) (double) (this.initialMp / 2));
		this.regenStamina((int) (double) (this.initialStamina / 2));
		if (this instanceof Character)
		{
			System.out.printf("""
				%s has now %d mana points and %d stamina points.
				""", this.getNickname(), this.getMp(), this.getStamina());
		}
		else
		{
			System.out.printf("%s rested.\n", this.getNickname());
		}
	}

	/**
	 * Determines if the attack is critical, missed or normal
	 * @return integer to know if it is a critical miss, critical hit or a normal hit.
	 */
	private int hitManagement()
	{
		int result = Randomizer.randomInRange(100, 1);
		if (result >= 90)
		{
			// Critical Hit
			return 2;
		}
		else if (result <= 10)
		{
			// Critical Miss
			return 0;
		}
		// Normal Hit
		return 1;
	}

	/**
	 * Adds a weapon the character's inventory
	 * @param weapon Weapon to add to the character's inventory
	 */
	public void addWeapon(Weapon weapon)
	{
		this.weapons.add(weapon);
	}

	/**
	 * Displays the list of all the character's weapons
	 */
	public void displayWeapons()
	{
		System.out.printf("Here is the list of all the weapons from %s:\n", this.getNickname());
		System.out.println("Any other choice that the underlined ones will bring you back to the previous screen.");
		for (int i = 0; i < this.getWeapons().size(); i ++)
		{
			Weapon w = this.getWeapons().get(i);
			System.out.printf("%s%d%s - Name: %s\n\tDamage: %s %s\n\tDurability: %d\n\tType: %s\n\t%s\n",
					ConsoleColors.BLACK_BACKGROUND, i + 1, ConsoleColors.RESET, w.getName(), w.getDamage(), w.getDamageCapability(), w.getDurability(), w.getWeaponType(),
					w == this.getActiveWeapon() ? ConsoleColors.BLACK_UNDERLINED + "Active" + ConsoleColors.RESET : "");
		}
	}

	/**
	 * Restore health points, mana points and stamina points
	 */
	public void resetValues()
	{
		this.setHp(this.initialHp);
		this.setMp(this.initialMp);
		this.setStamina(this.initialStamina);
	}
}
