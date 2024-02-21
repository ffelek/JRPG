package characters;

import equipments.Weapon;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Boss extends ACharacterAttributes
{
	private int level;
	private String name;
	private Weapon activeWeapon;

	public Boss(int id, int level, String name, String nickname, int hp, int mp, int stamina)
	{
		this.setId(id);
		this.setLevel(level);
		this.setName(name);
		this.setNickname(nickname);
		this.setHp(hp);
		this.setInitialHp(hp);
		this.setMp(mp);
		this.setInitialMp(mp);
		this.setStamina(stamina);
		this.setInitialStamina(stamina);
	}
}
