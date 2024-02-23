package characters;

import lombok.Getter;
import lombok.Setter;
import equipments.Equipment;
import java.util.List;

/**
 * Defines a playable character
 * @author felek
 */
@Getter
@Setter
public final class Character extends ACharacterAttributes
{
	private int weight;
	private List<Equipment> equipments;

	/**
	 * Constructor for character (player)
	 * @param id id
	 * @param name name
	 * @param hp Health Points
	 * @param mp Mana Points
	 * @param stamina Stamina Points
	 * @param weight Weight Capacity
	 */
	public Character(int id, String name, int hp, int mp, int stamina, int weight)
	{
		super(id, name, "", hp, mp, stamina);
		this.setWeight(weight);
	}
}
