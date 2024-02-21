package equipments;

import lombok.Getter;
import lombok.Setter;
import enums.WeaponType;

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

	private void setDamageCapability()
	{
		String[] d = this.damage.split("d");
		int diceNumber = Integer.parseInt(d[0]);
		int diceFaces = Integer.parseInt(d[1]);
		int maxDamage = diceNumber * diceFaces;
		this.damageCapability = "(" + diceNumber + " - " + maxDamage + ")";
	}

	private void reduceDurability()
	{
		this.durability --;
	}

	private void increaseDurability()
	{
		this.durability ++;
	}

}
