package characters;

import equipments.Weapon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Boss extends ACharacterAttributes
{
	private int level;
	private Weapon activeWeapon;

	public Boss(int id, int level, String name, String nickname, int hp, int mp, int stamina)
	{
		super(id, name, nickname, hp, mp, stamina);
		this.setLevel(level);
	}
}
