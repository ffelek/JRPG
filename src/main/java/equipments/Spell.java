package equipments;

import lombok.Getter;
import lombok.Setter;
import enums.SpellType;

/**
 *
 */
@Getter
@Setter
public class Spell
{
	private int id;
	private String name;
	private String damage;
	private int cost;
	private SpellType spellType;

	public Spell(int id, String name, String damage, int cost, SpellType spellType)
	{
		this.setId(id);
		this.setName(name);
		this.setDamage(damage);
		this.setCost(cost);
		this.setSpellType(spellType);
	}
}
