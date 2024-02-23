package characters;

import equipments.Weapon;
import lombok.Getter;
import lombok.Setter;

/**
 * Defines a boss
 * @author felek
 */
@Getter
@Setter
public class Boss extends ACharacterAttributes
{
	private int level;
	private Weapon activeWeapon;

	/**
	 * Constructor for a boss
	 * @param id id
	 * @param level level
	 * @param name name
	 * @param nickname nickname
	 * @param hp Health Points
	 * @param mp Mana Points
	 * @param stamina Stamina Points
	 */
	public Boss(int id, int level, String name, String nickname, int hp, int mp, int stamina)
	{
		super(id, name, nickname, hp, mp, stamina);
		this.setLevel(level);
	}
}
