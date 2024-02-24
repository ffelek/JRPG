package equipments;

import lombok.Getter;
import lombok.Setter;
import enums.WeaponType;

/**
 * Defines a weapon
 * @author felek
 */
@Getter
@Setter
public class Weapon
{
	private int id;
	private String name;
	private String damage;
	private String damageCapability;
	private int durability;
	private int initialDurability;
	private int cost;
	private WeaponType weaponType;
	private Rarity rarity;

	/**
	 * Constructor for a weapon
	 * @param id id
	 * @param name name
	 * @param damage damage (respects the DnD format with dices)
	 * @param durability durability value (int)
	 * @param cost stamina cost value (int)
	 * @param wt weapon type (see enum)
	 */
	public Weapon(int id, String name, String damage, int durability, int cost, WeaponType wt)
	{
		this.setId(id);
		this.setName(name);
		this.setDamage(damage);
		this.setDurability(durability);
		this.setInitialDurability(durability);
		this.setCost(cost);
		this.setWeaponType(wt);
		this.setDamageCapability();
	}

	/**
	 * Preload minimum and maximum damage in a string to show what the weapon can deal)
	 */
	private void setDamageCapability()
	{
		String[] d = this.damage.split("d");
		int diceNumber = Integer.parseInt(d[0]);
		int diceFaces = Integer.parseInt(d[1]);
		int maxDamage = diceNumber * diceFaces;
		this.damageCapability = "(" + diceNumber + " - " + maxDamage + ")";
	}

	/**
	 * Reduces the durability
	 */
	private void reduceDurability()
	{
		this.durability --;
	}

	/**
	 * Increases the durability
	 */
	private void increaseDurability()
	{
		this.durability ++;
	}

}
