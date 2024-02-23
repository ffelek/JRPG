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

	public boolean isDead()
	{
		return hp == 0;
	}

	public void attackReceived(int damageDealt)
	{
		this.hp -= damageDealt;
		if (hp < 0)
		{
			this.hp = 0;
		}
	}

	public void regenHealth(int healthPoints)
	{
		this.hp += healthPoints;
		if (this.hp > this.initialHp)
		{
			this.hp = this.initialHp;
		}
	}

	public boolean isManaOut()
	{
		return this.mp == 0;
	}

	public void regenMana(int manaPoints)
	{
		this.mp += manaPoints;
		if (this.mp > this.initialMp)
		{
			this.mp = this.initialMp;
		}
	}

	public void depleteMana(int manaPoints)
	{
		this.mp -= manaPoints;
		if (this.mp <= 0)
		{
			this.mp = 0;
		}
	}

	public boolean isStaminaOut()
	{
		if (this.stamina <= 0)
		{
			this.stamina = 0;
		}
		return this.stamina == 0;
	}

	public void regenStamina(int staminaPoints)
	{
		this.stamina += staminaPoints;
		if (this.stamina > this.initialStamina)
		{
			this.stamina = this.initialStamina;
		}
	}

	public void depleteStamina(int staminaPoints)
	{
		this.stamina -= staminaPoints;
		if (this.stamina < 0)
		{
			this.stamina = 0;
		}
	}

	public <Target extends ACharacterAttributes> void attack(Target t, Weapon w, boolean isGameMasterForcedAttack)
	{
		String message;
		int totalDamage = 0;
		String[] damage = w.getDamage().split("d");
		int diceNumber = Integer.parseInt(damage[0]);
		int diceFaces = Integer.parseInt(damage[1]);
		for (int i = 0; i < diceNumber; i++)
		{
			totalDamage += Randomizer.randomInRange(diceFaces, 1);
		}
		int hitType = !isGameMasterForcedAttack ? this.hitManagement() : 2;

		switch (hitType)
		{
			case 0 ->
			{
				totalDamage = 0;
				message = "Critical Miss! %s dealt the huge amount of %d point of damage to %s. %s\n";
			}
			case 2 ->
			{
				totalDamage = (int) Math.round(totalDamage * Double.parseDouble(Objects.requireNonNull(PropertiesFile.getProperty(Constants.CRITICAL_MULTIPLIER_KEY))));
				message = "Critical Hit! %s dealt %d point(s) of damage to %s. %s\n";
			}
			default -> message = "%s dealt %d point(s) of damage to %s. %s\n";
		}

		if (t.isHasParried())
		{
			totalDamage = (int) Math.ceil((double) totalDamage / 2);
		}

		t.attackReceived(totalDamage);
		System.out.printf(message, this.getNickname(), totalDamage, t.getNickname(), t.isHasParried() ? "Half damage because " + t.getNickname() + " parried." : "");
		this.depleteStamina(this.getActiveWeapon().getCost());
		if (this.isStaminaOut())
		{
			System.out.println(ConsoleColors.BLACK_BACKGROUND + ConsoleColors.RED_UNDERLINED + "Careful! Your stamina is depleted. You will have to rest a bit for another attack." + ConsoleColors.RESET);
		}
	}

	public void parry()
	{
		this.hasParried = !this.hasParried;
		int staminaPointsRegenerated = (int) (double) Math.round((float) this.getInitialStamina() / 4);
		this.regenStamina(staminaPointsRegenerated);
		System.out.println("Parrying. " + staminaPointsRegenerated + " stamina points have been recovered doing so.");
	}

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

	public void addWeapon(Weapon weapon)
	{
		this.weapons.add(weapon);
	}

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

	public void resetValues()
	{
		this.setHp(this.initialHp);
		this.setMp(this.initialMp);
		this.setStamina(this.initialStamina);
	}
}
