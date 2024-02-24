package equipments;

import lombok.Getter;
import lombok.Setter;
import enums.RarityType;

/**
 * Defines a rarity (that applies to equipments)
 * @author felek
 */
@Getter
@Setter
public class Rarity
{
	private int id;
	private String name;
	private String color;
	private RarityType rarityType;

	/**
	 * Constructor for a rarity
	 * @param id id
	 * @param name name
	 * @param color color (hexadecimal value #000000)
	 * @param rarityType rarity type (see enum)
	 */
	public Rarity(int id, String name, String color, RarityType rarityType)
	{
		this.setId(id);
		this.setName(name);
		this.setColor(color);
		this.setRarityType(rarityType);
	}
}
